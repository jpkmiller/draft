package impl5;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.runtime.StartupEvent;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestResponse;

import javax.enterprise.event.Observes;
import javax.ws.rs.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;


@Path("/")
public class Manager {

    EPPStage config;

    HashMap<Integer, List<EPPStage>> nextStage = new HashMap<>();

    private static final Logger LOGGER = Logger.getLogger("Manager");

    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("The application is starting...");

    }

    @POST
    @Path("/readConfig")
    @Consumes("*/*")
    @Produces(TEXT_PLAIN)
    public String readConfigFromFile() {
        try {
            // create object mapper instance
            ObjectMapper mapper = new ObjectMapper();

            // convert JSON file to EPPStage
            EPPStage config = mapper.readValue(Paths.get("config.json").toFile(), EPPStage.class);
            System.out.println(config);

            this.config = config;

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
        return "Success";
    }

    /**
     * Save for each stage its subStages in a Map.
     */
    public void buildNextStageMap() {
        buildNextStageMapR(this.config);
    }

    private void buildNextStageMapR(EPPStage stage) {
        if (stage == null) {
            return;
        }

        stage.subStages.forEach(subStage -> {
            /* use hashCode here because it's the simplest way to ensure a unique identifier */
            Integer stageHashCode = stage.hashCode();
            if (!this.nextStage.containsKey(stageHashCode)) {
                this.nextStage.put(stageHashCode, List.of(subStage));
            } else {
                List<EPPStage> subStages = this.nextStage.get(stageHashCode);
                subStages.add(subStage);
                this.nextStage.put(stageHashCode, subStages);
            }
            buildNextStageMapR(subStage);
        });
    }

    @GET
    @Path("/nextStage")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public RestResponse<List<EPPStage>> getNextStage(EPPStage stage) {
        Integer stageHashCode = stage.hashCode();
        List<EPPStage> nextStages = this.nextStage.get(stageHashCode);
        return RestResponse.ResponseBuilder.ok(
                        nextStages,
                        APPLICATION_JSON)
                .build();
    }
}
