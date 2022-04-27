package impl1;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class EPPHandler {

    List<EPPStage> stages;

    public EPPHandler() {
        stages = new ArrayList<>();
    }

    public void execute() {
        Event e = null;
        for (EPPStage stage : stages) {
            e = stage.execute(e);
        }
    }

    public void addStage(EPPStage stage) {
        stages.add(stage);
    }

    public void setStages(List<EPPStage> stages) {
        this.stages = stages;
    }

    public List<String> getStages() {
        return this.stages.stream().map(Object::toString).collect(Collectors.toList());
    }
}
