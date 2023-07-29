package org.certeasy.backend.issuer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonPropertyOrder({"id", "name", "type", "serial", "dn", "path_length", "parent", "children_count"})
public record IssuerInfo(
                         @JsonProperty("id")
                         String id,

                         @JsonProperty("name")
                         String name,

                         @JsonProperty("serial")
                         String serial,

                         @JsonProperty("type")
                         IssuerType type,

                         @JsonProperty("dn")
                         String distinguishedName,

                         @JsonProperty("path_length")
                         int pathLength,

                         @JsonProperty("parent")
                         IssuerParent parent,

                         @JsonProperty("children_count")
                         int totalChildren) {
}
