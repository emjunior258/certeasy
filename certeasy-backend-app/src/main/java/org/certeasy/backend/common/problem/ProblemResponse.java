package org.certeasy.backend.common.problem;

import org.certeasy.backend.common.validation.Violation;

import javax.ws.rs.core.Response;
import java.util.Set;

public class ProblemResponse {

    public static Response constraintViolation(Violation violation){
        return Response.status(422).entity(new ConstraintViolationProblem(violation))
                .build();
    }

    public static Response constraintViolations(Violation... violations){
        return Response.status(422).entity(new ConstraintViolationProblem(Set.of(violations)))
                .build();
    }

    public static Response constraintViolations(Set<Violation> violations){
        return Response.status(422).entity(new ConstraintViolationProblem(violations))
                .build();
    }

}
