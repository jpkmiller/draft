package impl5;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestResponse;

import io.quarkus.runtime.StartupEvent;

@Path("/")
public class Manager {

    EPPStage config;

    HashMap<Integer, List<EPPStage>> nextStage = new HashMap<>();

    private static final Logger LOGGER = Logger.getLogger("Manager");

    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("The application is starting ...");
        readConfigFromFile();
        buildNextStageMap();
    }

    @GET
    @Path("/readConfig")
    @Produces(TEXT_PLAIN)
    public EPPStage readConfigFromFile() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/config.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            /* read the json file and convert to java object */
            ObjectMapper mapper = new ObjectMapper();
            config = mapper.readValue(reader, EPPStage.class);

            LOGGER.info("Config read from /config.json ...");
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return getConfig();
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
            /*
             * use hashCode here because it's the simplest way to ensure a unique identifier
             */
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
    @Path("/getConfig")
    @Produces(APPLICATION_JSON)
    public EPPStage getConfig() {
        return this.config;
    }

    @GET
    @Path("/nextStageMap")
    @Produces(APPLICATION_JSON)
    public HashMap<Integer, List<EPPStage>> getNextStageMap() {
        return this.nextStage;
    }

    @GET
    @Path("/nextStage")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public RestResponse<List<EPPStage>> getNextStage(EPPStage stage) {
        Integer stageHashCode = stage.hashCode();
        List<EPPStage> nextStages = this.nextStage.get(stageHashCode);

        if (nextStages == null) {
            return RestResponse.notFound();
        }

        return RestResponse.ResponseBuilder.ok(
                nextStages,
                APPLICATION_JSON)
                .build();
    }
}
