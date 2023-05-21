package org.certeasy.backend.common.problem;

public final class CommonProblems {

    public static Problem notFound(String title, String detail){
        return new Problem("/problems/server-error", title, 404,
                detail);
    }

    public static Problem badRequest(String detail){
        return new Problem("/problems/bad-request", "Bad Request", 400,
                detail);
    }

    public static Problem serverError(String detail){
        return new Problem("/problems/server-error", "Server error", 500,
                detail);
    }

}
