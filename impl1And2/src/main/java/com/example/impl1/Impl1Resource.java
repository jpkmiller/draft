package com.example.impl1;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("/v1")
public class Impl1Resource {

    @Inject
    EPPHandler handler;

    @GET
    @Path("/exec")
    @Produces(MediaType.TEXT_PLAIN)
    public String execute() {
        this.handler.execute();
        return "Success";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getConfig() {
        System.err.println(this.handler.getStages());
        return this.handler.getStages();
    }

    @PUT
    public List<String> addStage(List<String> config) {
        for (String stage : config) {
            handler.addStage(stringToStage(stage));
        }
        return this.handler.getStages();
    }

    @POST
    public List<String> setStage(List<String> config) {
        this.handler.setStages(config.stream().map(this::stringToStage).collect(Collectors.toList()));
        return this.handler.getStages();
    }

    private EPPStage stringToStage(String stage) {
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
