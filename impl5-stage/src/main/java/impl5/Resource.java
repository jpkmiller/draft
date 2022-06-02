package impl5;

import java.util.Arrays;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestResponse;

import io.quarkus.runtime.StartupEvent;

@Path("/")
@ApplicationScoped
public class Resource {

    private static final Logger LOGGER = Logger.getLogger("Impl5-Source-Resource");

    @Inject
    EPPStage stage;

    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("Starting the Application ...");
    }

    @GET
    @Path("/stages")
    @Produces(APPLICATION_JSON)
    public List<String> getNextStages () {
        return this.stage.stagesLocations;
    }

    @POST
    @Path("/nextStage")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public List<String> nextStage(String[] locations) {
        LOGGER.info("Receiving locations " + Arrays.toString(locations));
        this.stage.stagesLocations = List.of(locations);
        return this.stage.stagesLocations;
    }

    @POST
    @Path("/exec")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public RestResponse<Event> exec(Event e) {
        LOGGER.info("Receiving Event " + e.data);
        Event resultEvent = stage.exec(e);
        if (resultEvent == null) {
            return RestResponse.serverError();
        }
        return RestResponse.ok(resultEvent);
    }
}
