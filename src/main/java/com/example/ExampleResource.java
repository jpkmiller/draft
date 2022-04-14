package com.example;

import com.example.impl1.*;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/")
public class ExampleResource {

    @Inject
    EPPHandler handler;

    @Inject
    EPPConfig config;

    @GET
    @Path("/exec")
    @Produces(MediaType.TEXT_PLAIN)
    public String exec() {
        this.handler.exec();
        return "Success";
    }

    @GET
    @Path("/config")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getConfig () {
        return this.config.getStages();
    }

    @POST
    public void setConfig(EPPConfig config) {
        System.out.println(config);
        for (String stage : config.getStages()) {
            handler.addStage(stringToStage(stage));
        }
    }

    private EPPStage stringToStage (String stage) {
        switch (stage) {
            case "source":
                return new ExampleEventSource1();
            case "stage1":
                return new ExampleEventStage1();
            case "stage2":
                return new ExampleEventStage2();
            case "sink":
                return new ExampleEventSink();
            default:
                return null;
        }
    }
}
