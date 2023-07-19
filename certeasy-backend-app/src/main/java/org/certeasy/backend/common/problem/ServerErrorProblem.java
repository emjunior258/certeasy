package org.certeasy.backend.common.problem;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class ServerErrorProblem extends Problem {

    public ServerErrorProblem(String detail){
        super("/problems/server-error", "Server error", 500, detail);
    }

}
