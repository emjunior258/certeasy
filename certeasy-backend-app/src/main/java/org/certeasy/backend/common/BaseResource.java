package org.certeasy.backend.common;

import org.certeasy.CertEasyContext;
import org.certeasy.backend.common.problem.ConstraintViolationProblem;
import org.certeasy.backend.common.problem.Problem;
import org.certeasy.backend.common.problem.ProblemResponse;
import org.certeasy.backend.common.validation.Violation;
import org.certeasy.backend.common.validation.ViolationType;
import org.certeasy.backend.issuer.CertIssuer;
import org.certeasy.backend.persistence.IssuerRegistry;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public abstract class BaseResource {

    final String ID_REGEX = "^[a-z]+(-[a-z]+)*$";
    final Pattern ID_PATTERN = Pattern.compile(ID_REGEX);

    @Inject
    IssuerRegistry registry;

    @Inject
    CertEasyContext context;

    private static final Logger LOGGER = Logger.getLogger(BaseResource.class);


    protected IssuerRegistry registry() {
        return this.registry;
    }

    protected CertEasyContext context() {
        return this.context;
    }

    public Response checkIssuerNotExistsThen(String issuerId, Supplier<Response> operation){
        Optional<Response> optionalResponse = this.checkIssuerId(issuerId);
        if(optionalResponse.isPresent())
            return optionalResponse.get();
        if(registry.exists(issuerId)){
            LOGGER.warn("Issuer id taken: " +  issuerId);
            return Response.status(409).entity(
                            new Problem("/problems/issuerId/id-taken",
                                    "Issuer ID Taken", 409,
                                    "There is already an issuerId with the provided ID: " + issuerId))
                    .build();
        }
        return operation.get();
    }

    public Response checkIssuerExistsThen(String issuerId, IssuerOperation operation){
        Optional<Response> optionalResponse = this.checkIssuerId(issuerId);
        if(optionalResponse.isPresent())
            return optionalResponse.get();
        Optional<CertIssuer> issuer = registry.getById(issuerId);
        if(issuer.isEmpty()){
            LOGGER.warn("Issuer not found: " +  issuerId);
            return Response.status(409).entity(
                            new Problem("/problems/issuer/not-found",
                                    "Issuer not found", 404,
                                    "There no issuer with a matching ID: " + issuerId))
                    .build();
        }
        return operation.getResponse(issuer.get());
    }


    protected Optional<Response> checkIssuerId(String issuerId){
        Set<Violation> violationSet = new HashSet<>();
        if(issuerId==null || issuerId.isEmpty())
            violationSet.add(new Violation("path.issuerId", ViolationType.REQUIRED,
                    "issuerId must not be null nor empty"));
        if(!ID_PATTERN.matcher(issuerId).matches())
            violationSet.add(new Violation("path.issuerId", ViolationType.PATTERN,
                    "issuerId does not match regular expression: "+ ID_REGEX));
        if(violationSet.isEmpty())
            return Optional.empty();
        return Optional.of(ProblemResponse.constraintViolations(
                violationSet));
    }


    /*
    public Response checkIssuerThen(String issuerId, boolean mustExist, IssuerOperation operation){
        Set<Violation> violationSet = new HashSet<>();
        if(!ID_PATTERN.matcher(issuerId).matches())
            violationSet.add(new Violation("path.issuerId", ViolationType.PATTERN,
                    "issuerId does not match regular expression: "+ ID_REGEX));
        if(mustExist && !registry.exists(issuerId)){
            LOGGER.warn("Issuer not found: " +  issuerId);
            return Response.status(409).entity(
                            new Problem("/problems/issuer/not-found",
                                    "Issuer not found", 404,
                                    "There no issuer with a matching ID: " + issuerId))
                    .build();
        }
        if(!mustExist && registry.exists(issuerId)){
            LOGGER.warn("Issuer id taken: " +  issuerId);
            return Response.status(409).entity(
                            new Problem("/problems/issuerId/id-taken",
                                    "Issuer ID Taken", 409,
                                    "There is already an issuerId with the provided ID: " + issuerId))
                    .build();
        }
        if(!violationSet.isEmpty()) {
            return Response.status(422).entity(
                            new ConstraintViolationProblem(violationSet))
                    .build();
        }
        return operation.getResponse(registry.getById(issuerId)
                .orElse(null));
    }*/

}
