package org.certeasy.backend.common.problem;

import org.certeasy.backend.common.validation.Violation;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

public final class ProblemResponse {


    public static Response badRequest(String message, Violation violation){
        BadRequestProblem problem = new BadRequestProblem(message, violation);
        return Response.status(400).entity(problem)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }

    public static Response badQueryParameter(Violation violation){
        BadRequestProblem problem = new BadRequestProblem(BadRequestProblem.INVALID_QUERY_PARAMETER, violation);
        return Response.status(400).entity(problem)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }

    public static Response badPathParameter(Violation violation){
        BadRequestProblem problem = new BadRequestProblem(BadRequestProblem.INVALID_PATH_PARAMETER, violation);
        return Response.status(400).entity(problem)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }

    public static Response badRequest(String message){
        BadRequestProblem problem = new BadRequestProblem(message, null);
        return Response.status(400).entity(problem)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }

    public static Response unprocessableEntity(Set<Violation> violations){
        return Response.status(422).entity(new UnprocessableEntityProblem(violations))
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }

    public static Response fromProblem(Problem problem){
        return Response.status(problem.getStatus()).entity(problem)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }

}
