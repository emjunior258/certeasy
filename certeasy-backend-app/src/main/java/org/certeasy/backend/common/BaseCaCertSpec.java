package org.certeasy.backend.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.certeasy.backend.common.validation.ValidationPath;
import org.certeasy.backend.common.validation.Violation;
import org.certeasy.backend.common.validation.ViolationType;

import java.util.Set;

public abstract class BaseCaCertSpec extends BaseCertSpec {

    @JsonProperty("path_length")
    private int pathLength;

    public int getPathLength() {
        return pathLength;
    }

    public void setPathLength(int pathLength) {
        this.pathLength = pathLength;
    }

    @Override
    public Set<Violation> validate(ValidationPath path) {
        Set<Violation> violationSet = super.validate(path);
        if(pathLength<-1)
            violationSet.add(new Violation(path, "path_length", ViolationType.RANGE, "path_length should be >= -1"));
        return violationSet;
    }

}
