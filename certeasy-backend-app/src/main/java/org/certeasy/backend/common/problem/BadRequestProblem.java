package org.certeasy.backend.common.problem;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.quarkus.runtime.annotations.RegisterForReflection;

@JsonPropertyOrder({ "type", "title", "status", "detail", "violations" })
@RegisterForReflection
public class BadRequestProblem extends Problem {


    public BadRequestProblem(String detail){
        super("/problems/bad-request","Bad Request", 400,detail);

    }

}
