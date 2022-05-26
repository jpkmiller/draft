package impl5;

import org.jboss.resteasy.reactive.RestResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.HashMap;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;


@Path("/")
public class Manager {

    EPPStage config;

    HashMap<Integer, List<EPPStage>> nextStage = new HashMap<>();


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
