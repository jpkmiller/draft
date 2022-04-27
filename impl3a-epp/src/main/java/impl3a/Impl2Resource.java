package impl3a;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/v2")
public class Impl2Resource {

    List<AbstractEPPStage> stages = new ArrayList<>();

    @GET
    @Path("/exec")
    @Produces(MediaType.TEXT_PLAIN)
    public String exec() {
        for (AbstractEPPStage stage : stages) {
            stage.exec();
        }
        return "Success";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getStages() {
        return this.stages.stream().map(AbstractEPPStage::toString).collect(Collectors.toList());
    }

    @PUT
    @Consumes(APPLICATION_JSON)
    public List<String> addStage(EPPStage stage) {
        this.stages.add(stageBuilder(stage));
        System.out.println(this.stages);
        return this.getStages();
    }

    @POST
    @Consumes(APPLICATION_JSON)
    public List<String> setStage(EPPStage stage) {
        this.stages.clear();
        return this.addStage(stage);
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
