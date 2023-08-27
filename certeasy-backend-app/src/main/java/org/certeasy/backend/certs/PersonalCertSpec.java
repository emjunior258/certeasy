package org.certeasy.backend.certs;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.certeasy.backend.common.BaseCertSpec;
import org.certeasy.backend.common.validation.ValidationPath;
import org.certeasy.backend.common.validation.Validator;
import org.certeasy.backend.common.validation.Violation;

import java.util.Set;

public class PersonalCertSpec extends BaseCertSpec {

    private String name;
    private String surname;
    private String telephone;
    @JsonProperty("email_addresses")
    private Set<String> emailAddresses;

    private Set<String> usernames;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Set<String> getEmailAddresses() {
        return emailAddresses;
    }

    public void setEmailAddresses(Set<String> emailAddresses) {
        this.emailAddresses = emailAddresses;
    }

    public Set<String> getUsernames() {
        return usernames;
    }

    public void setUsernames(Set<String> usernames) {
        this.usernames = usernames;
    }

    @Override
    public Set<Violation> validate(ValidationPath path) {
        Set<Violation> violationSet = super.validate(path);

        Validator validator = Validator.with(path, violationSet);
        validator.string("name", name)
                .notNull()
                .lengthGreaterThan(0)
                .lengthLessThan(255);
        validator.string("surname", surname)
                .notNull()
                .lengthGreaterThan(0)
                .lengthLessThan(255);
        validator.string("telephone", telephone)
                .lengthGreaterThan(0)
                .lengthLessThan(255);
        return violationSet;
    }
}
