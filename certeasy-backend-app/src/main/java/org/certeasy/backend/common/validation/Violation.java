package org.certeasy.backend.common.validation;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.quarkus.runtime.annotations.RegisterForReflection;

@JsonPropertyOrder({"field", "type", "subType", "message"})
@RegisterForReflection
public record  Violation (String field, String type, @JsonProperty("sub_type") String subType, String message){

    public Violation(String field, String type, String subType,  String message){
        this.field = field;
        this.type = type;
        this.subType = subType;
        this.message = message;
    }

    public Violation(String field, String type, String message){
        this(field,type, null, message);
    }

    public Violation(ValidationPath path, String field, String type, String message){
        this(String.format("%s.%s", path, field), type, message);
    }

    public Violation(ValidationPath path, String field, String type, String subType, String message){
        this(String.format("%s.%s", path, field), type, subType, message);
    }

}
