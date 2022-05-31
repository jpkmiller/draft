package impl5;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.jboss.logging.Logger;
import io.vertx.mutiny.core.buffer.Buffer;
import io.vertx.mutiny.ext.web.client.HttpResponse;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.ext.web.client.WebClient;

@Path("/")
public class Manager {

    EPPStage config;

    @Inject
    Endpoint endpoint;

    /**
     * Integer is the hash of the EPPStage
     * List<String> is a list of hostnames and ports
     */
    HashMap<Integer, List<String>> subStageMap = new HashMap<>();

    private static final Logger LOGGER = Logger.getLogger("Manager");

    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("Starting the Application ...");
        readConfigFromFile();
        buildSubStageMap();
        dispatchSubStage();
    }

    @GET
    @Path("/readConfig")
    @Produces(TEXT_PLAIN)
    /**
     * Read the config file and return the config object
     * @return the config object
     */
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

    private void buildSubStageMapR(EPPStage stage) {
        if (stage == null) {
            return;
        }

        /* use hashCode here because it's the simplest way to ensure a unique identifier */
        Integer stageHashCode = stage.hashCode();
        if (!this.subStageMap.containsKey(stageHashCode)) {
            this.subStageMap.put(stageHashCode, Collections.emptyList());
        }

        stage.subStages.forEach(subStage -> {
            String location = subStage.location;
            List<String> subStages = this.subStageMap.get(stageHashCode);
            subStages.add(location);
            this.subStageMap.put(stageHashCode, subStages);
            buildSubStageMapR(subStage);
        });
    }

    @GET
    @Path("/dispatch")
    public void dispatchSubStage() {
        dispatchSubStageR(this.config);
    }

    private void dispatchSubStageR(EPPStage stage) {
        if (stage == null) {
            return;
        }

        List<String> subStages = getSubStage(stage);
        LOGGER.info("Dispatching subStages (" + subStages + ") to" + stage.name + " ...");
        WebClient client = endpoint.getClient();
        
        Uni<HttpResponse<Buffer>> response = client.postAbs("http://" + stage.location + "/subStage")
            .sendJson(getSubStage(stage));

        response.subscribe().with(item -> LOGGER.info("Received " + item.statusCode()));
        

        stage.subStages.forEach(subStage -> {
            dispatchSubStageR(subStage);
        });
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
    public HashMap<Integer, List<String>> getSubStageMap() {
        return this.subStageMap;
    }

    @GET
    @Path("/subStage")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public List<String> getSubStage(EPPStage stage) {
        Integer stageHashCode = stage.hashCode();
        List<String> subStages = this.subStageMap.get(stageHashCode);
        return subStages == null ? Collections.emptyList() : subStages;
    }
}
