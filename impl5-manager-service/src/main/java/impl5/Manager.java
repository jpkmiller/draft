package impl5;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.buffer.Buffer;
import io.vertx.mutiny.ext.web.client.HttpResponse;
import io.vertx.mutiny.ext.web.client.WebClient;
import org.jboss.logging.Logger;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

@Path("/")
public class Manager {

    private static final Logger LOGGER = Logger.getLogger("Manager");
    EPPStage config;
    @Inject
    Endpoint endpoint;
    /**
     * String is the hashcode of the EPPStage
     * List<String> is a list of hostnames and ports
     */
    HashMap<String, List<String>> subStageMap = new HashMap<>();
    List<EPPStage> flatConfig = new ArrayList<>();

    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("Starting the Application ...");
        readConfigFromFile();
        flattenConfig();
        buildSubStageMap();
        dispatchSubstage();
    }

    /**
     * Read the config file and return the config object
     *
     * @return the config object
     */
    @GET
    @Path("/readConfig")
    @Produces(TEXT_PLAIN)
    public EPPStage readConfigFromFile() {
        LOGGER.info("Reading config from /config.json ...");
        try {
            InputStream inputStream = getClass().getResourceAsStream("/config.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            /* read the json file and convert to java object */
            ObjectMapper mapper = new ObjectMapper();
            config = mapper.readValue(reader, EPPStage.class);

            LOGGER.info("Successfully read config from /config.json ...");
        } catch (Exception e) {
            LOGGER.error("Failed reading config from /config.json ...");
            LOGGER.error(e.getMessage());
        }
        return getConfig();
    }

    /**
     * Save for the subStages of each stage in a Map.
     */
    public void buildSubStageMap() {
        LOGGER.info("Creating subStage map from config ...");
        buildSubStageMapR(this.config);
    }

    /**
     * Recursive builder.
     * Called by buildSubStageMap.
     *
     * @param stage
     */
    private void buildSubStageMapR(EPPStage stage) {
        if (stage == null) {
            return;
        }

        /*
         * use hashCode here because it's the simplest way to ensure a unique identifier
         */
        String stageHashCode = stage.getUniqueId();
        if (!this.subStageMap.containsKey(stageHashCode)) {
            this.subStageMap.put(stageHashCode, new ArrayList<>());
        }

        stage.subStages.forEach(subStage -> {
            String location = subStage.location;
            List<String> subStages = this.subStageMap.get(stageHashCode);
            subStages.add(location);
            this.subStageMap.put(stageHashCode, subStages);
            buildSubStageMapR(subStage);
        });
    }

    /**
     * Inform the stages about their subStages.
     */
    @GET
    @Path("/dispatchHeartbeat")
    public void dispatchHeartbeat() {
        dispatchR(this.config, "/", (stage) -> "{}");
    }

    @GET
    @Path("/dispatchSubstage")
    public void dispatchSubstage() {
        dispatchR(this.config, "/nextStage", (stage) -> getSubStage(stage));
    }

    /**
     * Recursive dispatcher.
     * Called by dispatchSubStage.
     *
     * @param stage
     * @return
     */
    private <T> void dispatchR(EPPStage stage, String query, Function<EPPStage, T> fn) {
        if (stage == null) {
            return;
        }

        LOGGER.info("Dispatching " + query + " to " + stage.name + " at " + stage.location + " ...");
        WebClient client = endpoint.getClient();

        Uni<HttpResponse<Buffer>> response = client.postAbs("http://" + stage.location + query)
                .sendJson(fn.apply(stage));

        response.subscribe().with(item -> LOGGER.info("Received " + item.statusCode() + " ..."));

        stage.subStages.forEach(subStage -> {
            dispatchR(subStage, query, fn);
        });
    }

    @GET
    @Path("/flatConfig")
    @Produces(APPLICATION_JSON)
    public List<EPPStage> getFlatConfig() {
        return this.flatConfig;
    }

    /**
     * Get flattened config.
     */
    public List<EPPStage> flattenConfig() {
        this.flatConfig = flattenConfigR(this.config);
        return this.flatConfig;
    }

    private ArrayList<EPPStage> flattenConfigR(EPPStage stage) {
        if (stage == null) {
            return new ArrayList<>();
        }

        ArrayList<EPPStage> stages = new ArrayList<>();
        stages.add(stage);
        stage.subStages.forEach(subStage -> {
            stages.addAll(flattenConfigR(subStage));
        });

        return stages;
    }

    @GET
    @Path("/config")
    @Produces(APPLICATION_JSON)
    public EPPStage getConfig() {
        return this.config;
    }

    @GET
    @Path("/subStageMap")
    @Produces(APPLICATION_JSON)
    public HashMap<String, List<String>> getSubStageMap() {
        return this.subStageMap;
    }

    @GET
    @Path("/subStage")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public List<String> getSubStage(EPPStage stage) {
        String stageHashCode = stage.getUniqueId();
        List<String> subStages = this.subStageMap.get(stageHashCode);
        return subStages == null ? Collections.emptyList() : subStages;
    }

}
