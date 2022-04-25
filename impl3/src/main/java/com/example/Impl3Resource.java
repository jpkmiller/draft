package com.example;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Path("/v3")
public class Impl3Resource {
    List<EPPStage> stages = new ArrayList<>();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/load")
    public List<String> loadStages () throws IOException {
        return this.stages.stream().map(Objects::toString).collect(Collectors.toList());
    }
}
