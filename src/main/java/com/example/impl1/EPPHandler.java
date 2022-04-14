package com.example.impl1;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class EPPHandler {

    List<EPPStage> stages;

    public EPPHandler () {
        stages = new ArrayList<>();
    }

    public void exec() {
        Event e = null;
        for (EPPStage stage : stages) {
            e = stage.execute(e);
        }
    }

    public void addStage(EPPStage stage) {
        stages.add(stage);
    }
}
