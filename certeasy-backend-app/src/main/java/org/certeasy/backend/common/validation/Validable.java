package org.certeasy.backend.common.validation;

import java.util.Set;

public interface Validable {

    Set<Violation> validate(ValidationPath path);

}
