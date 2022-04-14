package com.example.impl1;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class EPPConfig {
    List<String> stages;

    public EPPConfig () {
        this.stages = new ArrayList<>();
    }

    public EPPConfig(String[] stages) {
        this.stages = List.of(stages);
    }

    public List<String> getStages() {
        return stages;
    }
}
