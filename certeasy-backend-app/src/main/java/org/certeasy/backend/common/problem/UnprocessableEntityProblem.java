package org.certeasy.backend.common.problem;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.certeasy.backend.common.validation.Violation;

import java.util.Collections;
import java.util.Set;


@JsonPropertyOrder({ "type", "title", "status", "detail", "violations" })
@RegisterForReflection
public class UnprocessableEntityProblem extends Problem {

    private Set<Violation> violations;

    public UnprocessableEntityProblem(){

    }

    public UnprocessableEntityProblem(Violation violation){
        this(Set.of(violation));
    }

    public UnprocessableEntityProblem(Set<Violation> violations){
        super("/problems/unprocessable-entity","Unprocessable Entity", 422,
                "The request body violates one or more constraints");
        if(violations==null || violations.isEmpty())
            throw new IllegalArgumentException("violations must not be null or empty");
        this.violations = Collections.unmodifiableSet(violations);

    }

    public Set<Violation> getViolations() {
        return violations;
    }
}
