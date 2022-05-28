package impl5;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.config.ConfigProvider;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
@ApplicationScoped
public class Resource {

    @Inject
    Endpoint endpoint;

    String locationNextStage;

    @PostConstruct
    public Uni<String> postConstruct() {
        return register();
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.WILDCARD)
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> register() {
        System.out.println("Sink registering");
        String port = ConfigProvider.getConfig().getValue("quarkus.http.port", String.class);
        return this.endpoint.register(new EPPStage("sink1", "sink", "http://impl5-source:" + port));
    }

    @POST
    @Path("/nextStage")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String nextStage(String location) {
        this.locationNextStage = location;
        return this.locationNextStage;
    }
}
