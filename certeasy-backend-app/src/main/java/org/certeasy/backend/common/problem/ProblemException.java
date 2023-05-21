package org.certeasy.backend.common.problem;

public class ProblemException extends RuntimeException {

    private Problem problem;

    public ProblemException(Problem problem){
        super(problem.getDetail());
        this.problem = problem;
    }

    public ProblemException(Problem problem, Throwable cause){
        super(problem.getDetail(), cause);
        this.problem = problem;
    }

    public Problem getProblem() {
        return problem;
    }
}
