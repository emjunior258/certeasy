package org.certeasy.backend.common.problem;

import org.certeasy.backend.common.validation.Violation;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

public final class ProblemResponse {

    public static Response constraintViolation(Violation violation){
        return Response.status(422).entity(new ConstraintViolationProblem(violation))
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }

    public static Response constraintViolations(Set<Violation> violations){
        return Response.status(422).entity(new ConstraintViolationProblem(violations))
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }

    public static Response fromProblem(Problem problem){
        return Response.status(problem.getStatus()).entity(problem)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }

}
