package org.certeasy.backend.common.cert;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.certeasy.BasicConstraints;

public record BasicConstraintsInfo(
        boolean ca,
        @JsonProperty("path_length")
        int pathLength) {

        public BasicConstraintsInfo(boolean ca, int pathLength){
                this.ca = ca;
                this.pathLength = pathLength;
        }

        public BasicConstraintsInfo(BasicConstraints basicConstraints){
                this(basicConstraints.ca(),basicConstraints.pathLength());
        }

}
