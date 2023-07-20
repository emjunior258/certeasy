package org.certeasy.backend.common.cert;


import io.quarkus.runtime.annotations.RegisterForReflection;
import org.certeasy.backend.common.problem.Problem;

@RegisterForReflection
public class NotFoundProblem extends Problem {

    public NotFoundProblem(String type, String title, String detail) {
        super(type, title, 404, detail);
    }

}
