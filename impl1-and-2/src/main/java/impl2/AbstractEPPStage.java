package impl2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class AbstractEPPStage {

    List<AbstractEPPStage> stages;
    String name;
    String kind;

    public AbstractEPPStage(String name, String kind) {
        this.name = name;
        this.kind = kind;
        this.stages = new ArrayList<>();
    }

    public AbstractEPPStage(String name, String kind, List<AbstractEPPStage> stages) {
        this.name = name;
        this.kind = kind;
        this.stages = stages;
    }

    protected abstract Event execSelf(Event e);

    public void exec(Event e) {
        e = this.execSelf(e);
        for (AbstractEPPStage stage : this.stages) {
            stage.exec(e);
        }
    }

    public void exec() {
        exec(null);
    }

    public void addStage(AbstractEPPStage stage) {
        this.stages.add(stage);
    }

    public void setStages(List<AbstractEPPStage> stages) {
        this.stages = stages;
    }

    @Override
    public String toString() {
        return this.name + this.stages.stream().filter(Objects::nonNull).map(AbstractEPPStage::toString).collect(Collectors.toList());
    }
}
