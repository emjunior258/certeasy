package org.certeasy.backend.issuer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonPropertyOrder({"id", "type", "serial", "dn", "path_length"})
public record IssuerInfo(
                         @JsonProperty("id")
                         String id,

                         @JsonProperty("serial")
                         String serial,

                         @JsonProperty("type")
                         IssuerType type,

                         @JsonProperty("dn")
                         String distinguishedName,

                         @JsonProperty("path_length")
                         int pathLength) {
}
