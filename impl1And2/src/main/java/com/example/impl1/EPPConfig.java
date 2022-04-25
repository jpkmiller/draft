package com.example.impl1;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EPPConfig {
    String[] stages;

    public EPPConfig () {
        this.stages = new String[0];
    }

    public EPPConfig(String[] stages) {
        this.stages = stages;
    }

    public String[] getStages() {
        return stages;
    }
}
