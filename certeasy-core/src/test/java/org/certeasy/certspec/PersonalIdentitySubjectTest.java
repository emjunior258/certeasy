package org.certeasy.certspec;

import org.certeasy.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

class PersonalIdentitySubjectTest {

    @Test
    @DisplayName("Create PersonalIdentitySubject with Required Fields")
    void testCreatePersonalIdentitySubject_WithRequiredFields() {
        // Arrange
        PersonName personName = new PersonName("John", "Doe");
        GeographicAddress address = new GeographicAddress("US", "New York", "Brooklyn", "123 Main St");
        Set<String> emails = new HashSet<>();
        emails.add("john.doe@example.com");
        Set<String> usernames = new HashSet<>();
        usernames.add("johndoe");

        // Act
        PersonalIdentitySubject personalIdentitySubject = new PersonalIdentitySubject(personName, address, "123-456-7890", emails, usernames);

        // Assert
        this.assertPersonalIdentitySubject(personalIdentitySubject, personName, address, emails, usernames);
    }

    private void assertPersonalIdentitySubject(PersonalIdentitySubject personalIdentitySubject, PersonName personName, GeographicAddress address, Set<String> emails, Set<String> usernames){
        DistinguishedName distinguishedName = personalIdentitySubject.getDistinguishedName();
        assertTrue(distinguishedName.hasAttribute(SubjectAttributeType.GIVEN_NAME, personName.givenName()));
        assertTrue(distinguishedName.hasAttribute(SubjectAttributeType.SURNAME, personName.surname()));
        assertTrue(distinguishedName.hasAttribute(SubjectAttributeType.INITIALS, personName.initials()));
        assertTrue(distinguishedName.hasAttribute(SubjectAttributeType.COUNTRY_NAME, address.countryIsoCode()));
        assertTrue(distinguishedName.hasAttribute(SubjectAttributeType.COUNTRY_NAME, address.countryIsoCode()));
        assertTrue(distinguishedName.hasAttribute(SubjectAttributeType.COUNTRY_NAME, address.countryIsoCode()));
        assertTrue(distinguishedName.hasAttribute(SubjectAttributeType.PROVINCE, address.state()));
        assertTrue(distinguishedName.hasAttribute(SubjectAttributeType.LOCALITY, address.locality()));
        assertTrue(distinguishedName.hasAttribute(SubjectAttributeType.STREET, address.streetAddress()));
        assertNotNull(personalIdentitySubject);
        assertEquals(personName.fullName(), personalIdentitySubject.getCommonName());
        assertEquals(emails.size() + usernames.size(), personalIdentitySubject.getAlternativeNames().size());
        Set<SubjectAlternativeName> alternativeNameSet = personalIdentitySubject.getAlternativeNames();
        if(emails != null && emails.size() > 0){
            Set<String> emailSans = alternativeNameSet.stream().filter(it ->
                            it.type() == SubjectAlternativeNameType.EMAIL).map(SubjectAlternativeName::value)
                    .collect(Collectors.toSet());
            assertEquals(emails.size(), emailSans.size());
            assertTrue(emailSans.containsAll(emails));
        }
        if(usernames != null && usernames.size() > 0){
            Set<String> usernameSans = alternativeNameSet.stream().filter(it ->
                            it.type() == SubjectAlternativeNameType.OTHER_NAME).map(SubjectAlternativeName::value)
                    .collect(Collectors.toSet());
            assertEquals(usernames.size(), usernameSans.size());
            assertTrue(usernameSans.containsAll(usernames));
        }
    }

    @Test
    @DisplayName("Create PersonalIdentitySubject with All Fields")
    void testCreatePersonalIdentitySubject_WithAllFields() {
        // Arrange
        PersonName personName = new PersonName("John", "Doe");
        GeographicAddress address = new GeographicAddress("US", "New York", "Brooklyn", "123 Main St");
        Set<String> emails = new HashSet<>();
        emails.add("john.doe@example.com");
        Set<String> usernames = new HashSet<>();
        usernames.add("johndoe");
        OrganizationBinding organizationBinding = new OrganizationBinding("ACME Corp", "IT Department", "Manager");

        // Act
        PersonalIdentitySubject personalIdentitySubject = new PersonalIdentitySubject(personName, address, "123-456-7890", emails, usernames, organizationBinding);

        // Assert
        this.assertPersonalIdentitySubject(personalIdentitySubject, personName, address, emails, usernames);
    }

    @Test
    @DisplayName("Create PersonalIdentitySubject with Null Person Name")
    void testCreatePersonalIdentitySubject_WithNullPersonName() {
        // Arrange
        GeographicAddress address = new GeographicAddress("US", "New York", "Brooklyn", "123 Main St");
        Set<String> emails = new HashSet<>();
        emails.add("john.doe@example.com");
        Set<String> usernames = new HashSet<>();
        usernames.add("johndoe");

        // Assert
        assertThrows(IllegalArgumentException.class, () -> {
            // Act
            new PersonalIdentitySubject(null, address, "123-456-7890", emails, usernames);
        });
    }

    @Test
    @DisplayName("Create PersonalIdentitySubject with Null Address")
    void testCreatePersonalIdentitySubject_WithNullAddress() {
        // Arrange
        PersonName personName = new PersonName("John", "Doe");
        Set<String> emails = new HashSet<>();
        emails.add("john.doe@example.com");
        Set<String> usernames = new HashSet<>();
        usernames.add("johndoe");

        // Assert
        assertThrows(IllegalArgumentException.class, () -> {
            // Act
            new PersonalIdentitySubject(personName, null, "123-456-7890", emails, usernames);
        });
    }

    @Test
    @DisplayName("Create PersonalIdentitySubject with Empty Usernames")
    void testCreatePersonalIdentitySubject_WithEmptyUsernames() {
        // Arrange
        PersonName personName = new PersonName("John", "Cina");
        GeographicAddress address = new GeographicAddress("US", "New York", "Brooklyn", "123 Main St");
        Set<String> emails = new HashSet<>();
        Set<String> usernames = new HashSet<>();

        new PersonalIdentitySubject(personName, address, "123-456-7898", emails, usernames);

    }

    @Test
    @DisplayName("Create PersonalIdentitySubject with Empty Emails")
    void testCreatePersonalIdentitySubject_WithEmptyEmails() {
        // Arrange
        PersonName personName = new PersonName("John", "Doe");
        GeographicAddress address = new GeographicAddress("US", "New York", "Brooklyn", "123 Main St");
        Set<String> emails = new HashSet<>();
        Set<String> usernames = new HashSet<>();
        usernames.add("johndoe");

        new PersonalIdentitySubject(personName, address, "123-456-7890", emails, usernames);

    }
}
