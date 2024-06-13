package org.certeasy.backend.common.cert;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.certeasy.BasicConstraints;
import org.certeasy.backend.common.validation.Validable;
import org.certeasy.backend.common.validation.ValidationPath;
import org.certeasy.backend.common.validation.Violation;
import org.certeasy.backend.common.validation.ViolationType;

import java.util.HashSet;
import java.util.Set;

@RegisterForReflection
public class BasicConstraintsInfo  implements Validable {
        @JsonProperty(required = true)
        private boolean ca;

        @JsonProperty("path_length")
        private int pathLength;


        public BasicConstraintsInfo(){

        }

        public BasicConstraintsInfo(BasicConstraints basicConstraints){
                this.ca = basicConstraints.ca();
                this.pathLength = basicConstraints.pathLength();
        }

        public boolean isCa() {
                return ca;
        }

        public void setCa(boolean ca) {
                this.ca = ca;
        }

        public int getPathLength() {
                return pathLength;
        }

        public void setPathLength(int pathLength) {
                this.pathLength = pathLength;
        }

        @Override
        public Set<Violation> validate(ValidationPath path) {
                Set<Violation> violationSet = new HashSet<>();
                if(pathLength < -1)
                        violationSet.add(new Violation(path, "path_length", ViolationType.RANGE,
                                "path length cannot be less than -1"));
                if(!ca && pathLength != 0)
                        violationSet.add(new Violation(path, "path_length", ViolationType.PRECEDENCE,
                                "path length cannot be different of zero if not a CA"));
                return violationSet;
        }

        public BasicConstraints toBasicConstraints() {
                return new BasicConstraints(ca, pathLength);
        }

}
