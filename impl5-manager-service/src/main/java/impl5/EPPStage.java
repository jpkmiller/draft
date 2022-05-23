package impl5;

import java.util.*;

public class EPPStage {
    final String name;
    final String kind;
    final String location;
    final List<EPPStage> stages;
    boolean available = true;

    private EPPStage(String name, String kind) {
        this(name, kind, "");
    }

    private EPPStage(String name, String kind, List<EPPStage> stages) {
        this(name, kind, "", stages);
    }

    public EPPStage(String name, String kind, String location) {
        this(name, kind, location, Collections.emptyList());
    }

    private EPPStage(String name, String kind, String location, List<EPPStage> stages) {
        this.name = name;
        this.kind = kind;
        this.location = location;
        this.stages = stages;
    }

    @Override
    public String toString() {
        return this.name + " (" + this.kind + "): " + this.location;
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
