package impl3a;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

@Path("/v3")
public class Impl3EPPManager {

    @Inject
    Endpoint endpoint;

    List<AbstractEPPStage> stages = new ArrayList<>();

    @GET
    @Path("/execute")
    @Produces(TEXT_PLAIN)
    public String execute() {
        for (AbstractEPPStage stage : stages) {
            stage.exec();
        }
        return "Success";
    }

    @GET
    @Produces(APPLICATION_JSON)
    public List<String> getStages() {
        return this.stages.stream().map(AbstractEPPStage::toString).collect(Collectors.toList());
    }

    @PUT
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public List<String> addStage(EPPStage stage) {
        this.stages.add(stageBuilder(stage));
        System.out.println(this.stages);
        return this.getStages();
    }

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public List<String> setStage(EPPStage stage) {
        this.stages.clear();
        return this.addStage(stage);
    }

    @POST
    @Path("/register")
    @Consumes(TEXT_PLAIN)
    @Produces(TEXT_PLAIN)
    public String register() {
        Client client = endpoint.getClient();

    }

    @POST
    @Produces(APPLICATION_JSON)
    @Consumes()
    public List<String> loadStagesFromConfig() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            EPPStage[] stages = mapper.readValue(Paths.get("example3.json").toFile(), EPPStage[].class);

            // print book
            System.out.println(Arrays.toString(stages));
            Arrays.asList(stages).forEach(this::setStage);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return this.getStages();
    }

    private AbstractEPPStage stageBuilder(EPPStage stage) {
        AbstractEPPStage builtStage;
        switch (stage.name) {
            case "source":
                builtStage = new ExampleEventSource1(stage.name, stage.kind);
                break;
            case "stage1":
                builtStage = new ExampleEventStage1(stage.name, stage.kind);
                break;
            case "stage2":
                builtStage = new ExampleEventStage2(stage.name, stage.kind);
                break;
            case "sink1":
                builtStage = new ExampleEventSink(stage.name, stage.kind);
                break;
            case "sink2":
                builtStage = new ExampleEventSink2(stage.name, stage.kind);
                break;
            default:
                return null;
        }

        for (EPPStage stg : stage.stages) {
            builtStage.stages.add(stageBuilder(stg));
        }

        return builtStage;
    }
}
