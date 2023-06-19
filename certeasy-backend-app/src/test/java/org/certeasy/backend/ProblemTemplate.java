package org.certeasy.backend;

public enum ProblemTemplate {
    CONSTRAINT_VIOLATION("/problems/constraint-violation", "Constraint Violation",422, "The request violates one or more constraints"),
    ISSUER_ID_TAKEN("/problems/issuerId/id-taken", "Issuer ID Taken", 409);

    private String title;
    private int status;
    private String type;

    private String detail;

    ProblemTemplate(String type, String title, int status){
        this(type, title, status, null);
    }

    ProblemTemplate(String type, String title, int status, String detail){
        this.type = type;
        this.title = title;
        this.status = status;
        this.detail = detail;
    }


    public String getTitle() {
        return title;
    }

    public int getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public String getDetail() {
        return detail;
    }

}
