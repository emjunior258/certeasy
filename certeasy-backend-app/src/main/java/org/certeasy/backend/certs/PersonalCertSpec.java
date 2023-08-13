package org.certeasy.backend.certs;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.certeasy.backend.common.BaseCertSpec;
import org.certeasy.backend.common.validation.ValidationPath;
import org.certeasy.backend.common.validation.Violation;
import org.certeasy.backend.common.validation.ViolationType;

import java.util.Set;

public class PersonalCertSpec extends BaseCertSpec {


    @JsonProperty("first_name")
    private String firstName;
    private String surname;
    private String telephone;
    @JsonProperty("email_addresses")
    private Set<String> emailAddresses;

    private Set<String> usernames;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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
        if(firstName==null || firstName.isEmpty())
            violationSet.add(new Violation(path, "first_name", ViolationType.REQUIRED, "must specify the person's first_name"));
        if(surname==null || surname.isEmpty())
            violationSet.add(new Violation(path, "surname", ViolationType.REQUIRED, "must specify the person's surname"));
        return violationSet;
    }
}
