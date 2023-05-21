package org.certeasy.backend.issuer.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonPropertyOrder({"id", "type", "serial", "dn", "path_length"})
public record IssuerInfo(String id,
                         String serial,
                         IssuerType type,

                         @JsonProperty("dn")
                         String distinguishedName,

                         @JsonProperty("path_length")
                         int pathLength) {

}
