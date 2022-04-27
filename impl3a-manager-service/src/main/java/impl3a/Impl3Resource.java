package impl3a;

import javax.ws.rs.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;


@Path("/v3")
public class Impl3Resource {

    Map<String, String> epps = new HashMap<>();

    @POST
    @Path("/register")
    @Consumes(APPLICATION_JSON)
    @Produces(TEXT_PLAIN)
    public String registerEPP(EPP epp) {
        this.epps.put(epp.name, epp.location);
        return "registered " + epp.name;
    }

    @GET
    @Produces(APPLICATION_JSON)
    public List<String> getEPPs() {
        return epps.entrySet().stream().map(Objects::toString).collect(Collectors.toList());
    }
}
