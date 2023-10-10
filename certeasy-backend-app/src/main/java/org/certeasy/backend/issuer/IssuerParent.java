package org.certeasy.backend.issuer;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.quarkus.runtime.annotations.RegisterForReflection;

@JsonPropertyOrder({"id", "name"})
@RegisterForReflection
public record IssuerParent(String id, String name) {

}
