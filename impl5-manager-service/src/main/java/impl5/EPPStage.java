package impl5;

import java.util.*;

public class EPPStage {
    public String name;
    public String type;
    public String location;
    public List<EPPStage> subStages;
    public boolean available = false;

    public EPPStage () {}

    public EPPStage(String name, String type, String location) {
        this(name, type, location, Collections.emptyList());
    }

    public EPPStage(String name, String type, String location, EPPStage[] subStages) {
        this(name, type, location, Arrays.asList(subStages));
    }

    public EPPStage(String name, String type, String location, List<EPPStage> subStages) {
        this.name = name;
        this.type = type;
        this.location = location;
        this.subStages = subStages;
    }

    @Override
    public String toString() {
        return this.name + " (" + this.type + "): " + this.location;
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
        return name.equals(eppStage.name) && type.equals(eppStage.type) && location.equals(eppStage.location);
    }

    public String getUniqueId() {
        return this.name + this.location;
    }
}
