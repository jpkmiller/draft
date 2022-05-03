package impl5;

import impl5.EPPStage;
import org.jboss.resteasy.reactive.RestResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;


@Path("/v5")
public class Manager {

    Set<EPPStage> registeredStages;
    Map<String, List<EPPStage>> config = new HashMap<>();


    @POST
    public void loadConfig() {
    }

    public void loadConfigFromFile() {
    }

    private void setConfig() {
    }

    @POST
    @Path("/register")
    @Consumes(APPLICATION_JSON)
    @Produces(TEXT_PLAIN)
    public RestResponse<String> register(EPPStage stage) {
        this.registeredStages.add(stage);
        return RestResponse.ResponseBuilder.ok(
                        this.registeredStages
                                .stream()
                                .map(Objects::toString)
                                .collect(Collectors.toList())
                                .toString(),
                        MediaType.TEXT_PLAIN_TYPE)
                .build();
    }

    @GET
    @Path("/nextStage")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public RestResponse<Map<String, Object>> getNextStageAndSource(EPPStage stage) {
        Map<String, Object> nextStageAndSource = new HashMap<>();
        for (Map.Entry<String, List<EPPStage>> entry : this.config.entrySet()) {
            List<EPPStage> stages = entry.getValue();
            for (EPPStage stage1 : stages) {
                if (stage1.equals(stage)) {
                    nextStageAndSource.put("source", stages.get(0));
                    nextStageAndSource.put("nextStage", stage1.stages);
                    break;
                }
            }
        }
        return RestResponse.ResponseBuilder.ok(
                        nextStageAndSource,
                        MediaType.TEXT_PLAIN_TYPE)
                .build();
    }
}
