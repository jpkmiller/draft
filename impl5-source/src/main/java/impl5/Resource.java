package impl5;

import java.util.Arrays;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;

import io.quarkus.runtime.StartupEvent;

@Path("/")
@ApplicationScoped
public class Resource {

    @Inject
    Endpoint endpoint;

    private static final Logger LOGGER = Logger.getLogger("Impl5-Source-Resource");

    String[] locationNextStages;

    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("Starting the Application ...");
    }

    @POST
    @Path("/nextStage")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String[] nextStage(String[] locations) {
        LOGGER.info("Receiving locations " + Arrays.toString(locations));
        this.locationNextStages = locations;
        return this.locationNextStages;
    }
}
