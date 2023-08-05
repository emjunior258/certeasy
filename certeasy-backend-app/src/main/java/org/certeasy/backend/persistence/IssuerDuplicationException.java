package org.certeasy.backend.persistence;

import org.certeasy.DistinguishedName;
import org.certeasy.backend.common.problem.Problem;

public class IssuerDuplicationException extends IssuerRegistryException {

    private final DistinguishedName distinguishedName;

    public IssuerDuplicationException(DistinguishedName distinguishedName) {
        super("There is already an issuer with the provided Distinguished Name: "+ distinguishedName);
        this.distinguishedName = distinguishedName;
    }

    public DistinguishedName getDistinguishedName() {
        return distinguishedName;
    }

    public Problem asProblem(){
        return new Problem("/problems/issuer/duplication", "Issuer Duplication", 409,
                "There is already an issuer with the provided Distinguished Name");
    }

}
