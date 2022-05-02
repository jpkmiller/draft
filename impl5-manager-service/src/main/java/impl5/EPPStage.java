package impl5;

import java.util.Arrays;
import java.util.List;

public class EPPStage {
    final String name;
    final String location;

    final List<EPPStage> stages;

    boolean available;


    public EPPStage(String name, String location, EPPStage[] stages) {
        this.name = name;
        this.location = location;
        this.stages = Arrays.asList(stages);
        this.available = true;
    }
}
