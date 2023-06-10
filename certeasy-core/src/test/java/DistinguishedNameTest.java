import org.certeasy.DistinguishedName;
import org.certeasy.RelativeDistinguishedName;
import org.certeasy.SubjectAttributeType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DistinguishedNameTest {

    @Test
    @DisplayName("toString() must return desired output: repeated attributes")
    public void toStringMustReturnDesiredOutputWithRepeatedAttributes(){

        DistinguishedName dn = DistinguishedName
                .builder()
                .append(new RelativeDistinguishedName(SubjectAttributeType.CommonName, "John Doe"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.Initials, "JD"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.DomainComponent, "vm", 0))
                .append(new RelativeDistinguishedName(SubjectAttributeType.DomainComponent, "co", 1))
                .append(new RelativeDistinguishedName(SubjectAttributeType.DomainComponent, "mz", 2))
                .build();

        assertEquals("CN=John Doe, initials=JD, DC=vm, DC=co, DC=mz", dn.toString());

    }

    @Test
    @DisplayName("toString() must return desired output: single instance attributes")
    public void toStringMustReturnDesiredOutputWithSingleInstanceAttributes(){

        DistinguishedName dn = DistinguishedName
                .builder()
                .append(new RelativeDistinguishedName(SubjectAttributeType.CommonName, "John Doe"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.Initials, "JD"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.GivenName, "John"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.Surname, "Doe"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.CountryName, "MZ"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.Province, "Maputo"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.Street, "Av 24 de Julho. Nr300"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.UserID, "johndoe"))
                .append(new RelativeDistinguishedName(SubjectAttributeType.TelephoneNumber, "849901010"))
                .build();

        assertEquals("CN=John Doe, initials=JD, G=John, SN=Doe, telephoneNumber=849901010, UID=johndoe, C=MZ, ST=Maputo, STREET=Av 24 de Julho. Nr300", dn.toString());

    }


    @Test
    @DisplayName("findAll() must return all RDNs of specified type")
    public void findAllMustFindAllRDNsOfSpecifiedType(){

    }

    @Test
    @DisplayName("findAll() must return empty set of RDNs of specified type")
    public void findAllMustReturnEmptySetOfSpecifiedType(){

    }


    @Test
    @DisplayName("findAll() must return first matching RDN")
    public void findFirstMustReturnFirstMatchingRDN(){


    }

    @Test
    @DisplayName("findAll() must return empty optional")
    public void findFirstMustReturnEmptyOptional(){


    }

}
