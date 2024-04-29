package org.certeasy.backend;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.certeasy.backend.common.problem.CommonProblems;
import org.certeasy.backend.common.problem.ServerErrorProblem;
import org.jboss.logging.Logger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@RegisterForReflection
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {

    private static final Logger LOGGER = Logger.getLogger(GlobalExceptionMapper.class);

    @Override
    public Response toResponse(Exception exception) {
        // Use response from WebApplicationException as they are
        if (exception instanceof WebApplicationException wex) {
            if(wex.getCause() instanceof JsonProcessingException){
                LOGGER.debug("Bad Request Entity");
                return Response.status(400).entity(CommonProblems.badRequest(
                                "Request Entity NOT a well-formed JSON"))
                        .build();
            } else {
                LOGGER.debug("Not a JSON formatting issue");
                return wex.getResponse();
            }
        } else {
            LOGGER.error(exception);
            return Response.status(500).entity(new ServerErrorProblem("Something unexpected happened"))
                    .build();
        }
    }

}
