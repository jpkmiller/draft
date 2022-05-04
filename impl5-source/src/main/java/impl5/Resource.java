package impl5;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.config.ConfigProvider;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class Resource {

    @Inject
    Endpoint endpoint;

    @POST
    @Path("/register")
    @Consumes(MediaType.WILDCARD)
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> register() {
        String port = ConfigProvider.getConfig().getValue("quarkus.http.port", String.class);
        return endpoint.register(new EPPStage("stage1", "source", "http://impl5-source:" + port));
    }
}
