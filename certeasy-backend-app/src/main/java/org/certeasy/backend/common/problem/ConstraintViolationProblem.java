package org.certeasy.backend.common.problem;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.certeasy.backend.common.validation.Violation;

import java.util.Collections;
import java.util.Set;


@JsonPropertyOrder({ "type", "title", "status", "detail", "violations" })
public class ConstraintViolationProblem extends Problem {

    private Set<Violation> violations;

    public ConstraintViolationProblem(Violation violation){
        this(Set.of(violation));
    }

    public ConstraintViolationProblem(Set<Violation> violations){
        super("/problems/constraint-violation","Constraint Violation", 422,
                "The request violates one or more constraints");
        if(violations==null || violations.isEmpty())
            throw new IllegalArgumentException("violations must not be null or empty");
        this.violations = Collections.unmodifiableSet(violations);

    }

    public Set<Violation> getViolations() {
        return violations;
    }
}
