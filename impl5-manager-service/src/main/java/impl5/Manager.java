package impl5;

import impl5.EPPStage;
import org.jboss.resteasy.reactive.RestResponse;

import javax.ws.rs.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;


@Path("/v5")
public class Manager {

    Map<String, List<EPPStage>> config = new HashMap<>();


    @POST
    public void loadConfig() {

    }

    public void loadConfigFromFile() {

    }

    private void setConfig () {

    }

    @POST
    @Path("/register")
    @Consumes(APPLICATION_JSON)
    @Produces(TEXT_PLAIN)
    public RestResponse<String> register(EPPStage epp) {

    }

    @POST
    @Path("/register")
    @Consumes(APPLICATION_JSON)
    @Produces(TEXT_PLAIN)
    public RestResponse<String> balanceLoad() {

    }

    @POST
    @Path("/register")
    @Consumes(APPLICATION_JSON)
    @Produces(TEXT_PLAIN)
    public RestResponse<String> killServices() {

    }

    @GET
    @Path("/nextStage")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public RestResponse<Map<String, EPPStage>> getNextStageAndSource() {

    }
}
