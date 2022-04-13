package com.example;


import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class ExampleResource {

    @Inject
    EPPHandler handler;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String execute() {
        handler.removeAll();
        handler.addStage(new ExampleEventSource1())
                .addStage(new ExampleEventStage1())
                .addStage(new ExampleEventStage2())
                .addStage(new ExampleEventSink());

        handler.execute();
        return "Hello";
    }
}
