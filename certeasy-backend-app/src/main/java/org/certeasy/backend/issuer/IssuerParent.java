package org.certeasy.backend.issuer;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id", "name"})
public record IssuerParent(String id, String name) {

}
