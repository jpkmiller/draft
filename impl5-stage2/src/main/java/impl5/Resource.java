package impl5;

import io.quarkus.runtime.StartupEvent;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.Arrays;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

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
    public List<String> getNextStages() {
        return this.stage.getStagesLocations();
    }

    @POST
    @Path("/nextStage")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public List<String> nextStage(String[] locations) {
        LOGGER.info("Receiving locations " + Arrays.toString(locations));
        this.stage.setStagesLocations(List.of(locations));
        return this.getNextStages();
    }

    @POST
    @Path("/exec")
    public void exec(Event e) {
        LOGGER.info("Receiving Event " + e.data);
        stage.addEvent(e);
        /* should not be called like this */
        stage.exec();
    }
}
