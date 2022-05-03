package impl5;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class EPPStage {
    final String name;
    final String kind;
    final String location;
    final List<EPPStage> stages;
    boolean available;


    public EPPStage(String name, String kind, String location, EPPStage[] stages) {
        this.name = name;
        this.kind = kind;
        this.location = location;
        this.stages = Arrays.asList(stages);
        this.available = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EPPStage eppStage = (EPPStage) o;
        return name.equals(eppStage.name) && kind.equals(eppStage.kind) && location.equals(eppStage.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, kind, location, stages);
    }
}
