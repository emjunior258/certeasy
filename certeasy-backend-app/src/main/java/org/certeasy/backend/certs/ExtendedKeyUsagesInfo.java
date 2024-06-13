package org.certeasy.backend.certs;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.certeasy.ExtendedKeyUsage;
import org.certeasy.ExtendedKeyUsageEffect;
import org.certeasy.ExtendedKeyUsages;
import org.certeasy.backend.common.validation.Validable;
import org.certeasy.backend.common.validation.ValidationPath;
import org.certeasy.backend.common.validation.Violation;
import org.certeasy.backend.common.validation.ViolationType;
import org.jboss.logging.Logger;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@RegisterForReflection
public class ExtendedKeyUsagesInfo implements Validable {

    private static final String ALLOWED_USAGES_ATTR = "allowed_usages";

    private static final Logger LOGGER = Logger.getLogger(ExtendedKeyUsagesInfo.class);

    @JsonProperty(ALLOWED_USAGES_ATTR)
    private String[] allowedUsages;

    private boolean critical;

    public String[] getAllowedUsages() {
        return allowedUsages;
    }

    public void setAllowedUsages(String[] allowedUsages) {
        this.allowedUsages = allowedUsages;
    }

    public boolean isCritical() {
        return critical;
    }

    public void setCritical(boolean critical) {
        this.critical = critical;
    }

    @Override
    public Set<Violation> validate(ValidationPath path) {
        Set<Violation> violations = new HashSet<>();
        if (allowedUsages == null) {
            violations.add(new Violation(path, ALLOWED_USAGES_ATTR,
                    ViolationType.REQUIRED,
                    "allowed usages must not be null"));
        } else if (allowedUsages != null && allowedUsages.length == 0) {
            violations.add(new Violation(path, ALLOWED_USAGES_ATTR,
                    ViolationType.LENGTH,
                    "allowed usages must have at least one item"));
        } else {
            for(byte i=0; i < allowedUsages.length;  i++){
                try{
                    ExtendedKeyUsage.valueOf(allowedUsages[i]);
                }catch (IllegalArgumentException ex){
                    violations.add(new Violation(path.append(ALLOWED_USAGES_ATTR), String.format("[%s]", i),
                            ViolationType.ENUM,
                            "value MUST be one of "+ Arrays.toString(
                                    ExtendedKeyUsage.values())));
                }
            }
        }
        return violations;
    }

    public ExtendedKeyUsages toExtendedKeyUsages() {
        Set<ExtendedKeyUsage> keyUsages = new HashSet<>();
        for(String keyUsage : allowedUsages){
            try {
                keyUsages.add(ExtendedKeyUsage.valueOf(keyUsage));
            }catch (IllegalArgumentException ex){
                LOGGER.debug("Invalid Extended Key usage: "+keyUsage);
            }
        }
        if(keyUsages.isEmpty())
            throw new IllegalStateException("not a single allowed usage found");
        return new ExtendedKeyUsages(keyUsages,
                critical ? ExtendedKeyUsageEffect.ENFORCE : ExtendedKeyUsageEffect.INFO);
    }

}
