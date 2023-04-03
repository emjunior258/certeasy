package org.certeasy;



import io.quarkus.runtime.StartupEvent;

import javax.enterprise.event.Observes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.*;

@Path("/dn-attributes")
public class AttributesResource {

    private static final Set<DistinguishedNameAttribute> ATTRIBUTE_SET = new HashSet<>();


    public void initialize(@Observes StartupEvent ev){
        ATTRIBUTE_SET.add(new DistinguishedNameAttribute("common-name", "Common Name", "The name commonly used to refer to the subject", false, 1, true));
        ATTRIBUTE_SET.add(new DistinguishedNameAttribute("title", "Title", "The title used to refer to the subject", false, 1, false));
        ATTRIBUTE_SET.add(new DistinguishedNameAttribute("initials", "Initials", "The initials used to refer to the subject", false, 1, false));
        ATTRIBUTE_SET.add(new DistinguishedNameAttribute("given-name", "Given Name", "The given name of the subject", false, 1, false));
        ATTRIBUTE_SET.add(new DistinguishedNameAttribute("surname", "Surname", "The surname of the subject", false, 1, false));
        ATTRIBUTE_SET.add(new DistinguishedNameAttribute("telephone", "Telephone", "The telephone of the subject", false, 1, false));
        ATTRIBUTE_SET.add(new DistinguishedNameAttribute("user-id", "User ID", "The ID of a user", false, 1, false));
        ATTRIBUTE_SET.add(new DistinguishedNameAttribute("country", "Country", "The country to which the subject belongs", false, 1, false));
        ATTRIBUTE_SET.add(new DistinguishedNameAttribute("locality", "Locality", "The state/province locality to which the subject belongs", false, 1, false));
        ATTRIBUTE_SET.add(new DistinguishedNameAttribute("province-state", "Province/State", "The provide/state to which the subject belongs", false, 1, false));
        ATTRIBUTE_SET.add(new DistinguishedNameAttribute("street", "Street", "The address of the street where the subject is located", false, 1, false));
        ATTRIBUTE_SET.add(new DistinguishedNameAttribute("organization-name", "Organization Name", "The name of the organization to which the subject belongs", false, 1, false));
        ATTRIBUTE_SET.add(new DistinguishedNameAttribute("organization-unit", "Organization Unit", "The name of the organization unit to which the subject belongs", true, 10, false));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Set<DistinguishedNameAttribute> list(){

        return ATTRIBUTE_SET;

    }

}
