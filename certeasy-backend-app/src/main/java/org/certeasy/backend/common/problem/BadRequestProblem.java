package org.certeasy.backend.common.problem;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.certeasy.backend.common.validation.Violation;

@JsonPropertyOrder({ "type", "title", "status", "detail", "violation" })
@RegisterForReflection
public class BadRequestProblem extends Problem {


    public static final String INVALID_PATH_PARAMETER = "Invalid path parameter";
    public static final String INVALID_QUERY_PARAMETER = "Invalid query parameter";

    @JsonProperty("violation")
    private Violation violation;

    public BadRequestProblem(String detail, Violation violation){
        super("/problems/bad-request","Bad Request", 400,detail);
        this.violation = violation;
    }

    public BadRequestProblem(String detail){
        this(detail, null);
    }

    public Violation getViolation() {
        return violation;
    }

    public void setViolation(Violation violation) {
        this.violation = violation;
    }
}
