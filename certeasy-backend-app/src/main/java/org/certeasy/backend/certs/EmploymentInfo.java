package org.certeasy.backend.certs;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.certeasy.backend.common.validation.*;

import java.util.HashSet;
import java.util.Set;

public class EmploymentInfo implements Validable {

    @JsonProperty("organization_name")
    private String organizationName;

    @JsonProperty("job_title")
    private String jobTitle;

    @JsonProperty("email_address")
    private String emailAddress;

    private String username;

    private String department;

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Set<Violation> validate(ValidationPath path) {
        Set<Violation> violationSet = new HashSet<>();
        Validator validator = Validator.with(path, violationSet);
        validator.string("organization_name", organizationName)
                .notNull()
                .lengthGreaterThan(0)
                .lengthLessThan(255);
        validator.string("job_title", jobTitle)
                .notNull()
                .lengthGreaterThan(0)
                .lengthLessThan(255);
        validator.string("email_address", emailAddress)
                .lengthGreaterThan(0)
                .lengthLessThan(255);
        validator.string("username", username)
                .lengthGreaterThan(0)
                .lengthLessThan(255);
        return violationSet;
    }
}
