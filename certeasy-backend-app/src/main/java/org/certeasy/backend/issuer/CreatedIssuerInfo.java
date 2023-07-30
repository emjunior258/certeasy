package org.certeasy.backend.issuer;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreatedIssuerInfo(@JsonProperty("issuer_id")  String issuerId) {

}
