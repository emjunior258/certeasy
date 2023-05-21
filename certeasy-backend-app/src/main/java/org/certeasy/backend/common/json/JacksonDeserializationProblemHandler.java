package org.certeasy.backend.common.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import org.certeasy.backend.common.problem.ConstraintViolationProblem;
import org.certeasy.backend.common.problem.ProblemException;
import org.certeasy.backend.common.validation.Violation;

import java.io.IOException;

public class JacksonDeserializationProblemHandler extends DeserializationProblemHandler {

    public boolean handleUnknownProperty(DeserializationContext ctxt, JsonParser p, JsonDeserializer<?> deserializer, Object beanOrClass, String propertyName) throws IOException {
        throw new ProblemException(
                new ConstraintViolationProblem(
                        new Violation( "body", "schema", "The request body contains properties that are not defined in the schema")
                )
        );
    }


}
