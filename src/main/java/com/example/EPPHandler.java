package com.example;

import io.quarkus.arc.All;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class EPPHandler {

    List<EPPStage> stages;

    public EPPHandler () {
        stages = new ArrayList<>();
    }

    public void execute() {
        Event e = null;
        for (EPPStage stage : stages) {
            e = stage.execute(e);
        }
    }

    public EPPHandler addStage(EPPStage stage) {
        stages.add(stage);
        return this;
    }

    public void removeAll() {
        stages.clear();
    }
}
