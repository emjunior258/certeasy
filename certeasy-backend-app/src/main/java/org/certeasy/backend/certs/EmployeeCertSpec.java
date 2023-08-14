package org.certeasy.backend.certs;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.certeasy.backend.common.BaseCertSpec;
import org.certeasy.backend.common.validation.ValidationPath;
import org.certeasy.backend.common.validation.Validator;
import org.certeasy.backend.common.validation.Violation;
import org.certeasy.backend.common.validation.ViolationType;

import java.util.Set;

public class EmployeeCertSpec extends BaseCertSpec {

    @JsonProperty("first_name")
    private String firstName;
    private String surname;
    private String telephone;

    private EmploymentInfo employment;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public EmploymentInfo getEmployment() {
        return employment;
    }

    public void setEmployment(EmploymentInfo employment) {
        this.employment = employment;
    }

    @Override
    public Set<Violation> validate(ValidationPath path) {
        Set<Violation> violationSet = super.validate(path);
        Validator validator = Validator.with(path, violationSet);
        validator.object("employment", employment)
                .notNull()
                .cascade();
        validator.string("first_name", firstName)
                .notNull()
                .lengthGreaterThan(0)
                .lengthLessThan(255);
        validator.string("surname", firstName)
                .notNull()
                .lengthGreaterThan(0)
                .lengthLessThan(255);
        validator.string("telephone", telephone)
                .lengthGreaterThan(0)
                .lengthLessThan(255);
        return violationSet;
    }

}
