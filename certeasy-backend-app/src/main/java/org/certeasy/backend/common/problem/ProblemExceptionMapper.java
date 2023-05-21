package org.certeasy.backend.common.problem;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ProblemExceptionMapper implements ExceptionMapper<ProblemException> {

    @Override
    public Response toResponse(ProblemException e) {
        Problem problem = e.getProblem();
        return  Response.status(problem.getStatus()).entity(problem)
                .build();
    }

}
