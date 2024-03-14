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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public abstract class BaseResource {

    final String ID_REGEX = "^[a-z0-9]+(-[a-z0-9]+){0,20}$";
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

    public Response checkIssuerExistsThen(String issuerId, IssuerOperation operation){
        Optional<Response> optionalResponse = this.checkIssuerId(issuerId);
        if(optionalResponse.isPresent())
            return optionalResponse.get();
        Optional<CertIssuer> issuer = registry.getById(issuerId);
        if(issuer.isEmpty()){
            LOGGER.warn("Issuer not found: " +  issuerId);
            return Response.status(404).entity(
                    new Problem("/problems/issuer/not-found",
                                    "Issuer not found", 404,
                                    "There no issuer with a matching ID: " + issuerId))
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .build();
        }
        return operation.getResponse(issuer.get());
    }


    protected Optional<Response> checkIssuerId(String issuerId){
        Set<Violation> violationSet = new HashSet<>();
        if(issuerId==null || issuerId.isEmpty() || issuerId.isBlank())
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

}
