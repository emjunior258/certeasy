package org.certeasy.backend.common.validation;

import java.util.Set;
import java.util.regex.Pattern;

public class Validator {

    private ValidationPath path;
    private Set<Violation> violations;

    private Validator(ValidationPath path, Set<Violation> violations){
        this.path = path;
        this.violations = violations;
    }

    public static Validator with(ValidationPath path, Set<Violation> violations){
        if(path==null)
            throw new IllegalArgumentException("path MUST not be null");
        if(violations==null)
            throw new IllegalArgumentException("violations set MUST not be null");
        return new Validator(path,violations);
    }


    public Set<Violation> getViolations(){
        return this.violations;
    }

    public StringValidation string(String field, String value){
        if(field==null || field.isEmpty())
            throw new IllegalArgumentException("field name MUST not be null nor empty");
        return new StringValidation(field, value);
    }

    public ObjectValidation object(String field, Object value){
        if(field==null || field.isEmpty())
            throw new IllegalArgumentException("field name MUST not be null nor empty");
        return new ObjectValidation(field, value);
    }

    public final class StringValidation {

        private final String fieldName;
        private final String value;

        private StringValidation(String fieldName, String value){
            this.fieldName = fieldName;
            this.value = value;
        }

        public StringValidation lengthGreaterThan(int threshold){
            if(value != null && value.length() <= threshold )
                Validator.this.violations.add(new Violation(path, fieldName, ViolationType.LENGTH, fieldName + " must have length greater than "+ threshold));
            return this;
        }

        public StringValidation lengthLessThan(int threshold){
            if(value != null && value.length() >= threshold )
                Validator.this.violations.add(new Violation(path, fieldName, ViolationType.LENGTH, fieldName + " must have length less than "+ threshold));
            return this;
        }

        public StringValidation notNull(){
            if(value ==null )
                Validator.this.violations.add(new Violation(path, fieldName, ViolationType.REQUIRED, "must specify value for "+fieldName));
            return this;
        }

        public StringValidation matchPattern(Pattern pattern){
            if(pattern != null && !pattern.matcher(value).matches())
                Validator.this.violations.add(new Violation(path, fieldName, ViolationType.PATTERN, "must meet desired pattern: "+pattern));
            return this;
        }

    }

    public final class ObjectValidation {

        private final String fieldName;
        private final Object value;

        private ObjectValidation(String fieldName, Object value){
            this.fieldName = fieldName;
            this.value = value;
        }

        public ObjectValidation notNull() {
            if (value == null)
                Validator.this.violations.add(new Violation(path, fieldName, ViolationType.REQUIRED, "must specify value for " + fieldName));
            return this;
        }

        public ObjectValidation cascade() {
            if(value != null && value instanceof Validable){
                Validator.this.violations.addAll(((Validable) value).validate(path.append(fieldName)));
            }
            return this;
        }

    }

}
