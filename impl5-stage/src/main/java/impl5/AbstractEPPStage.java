package impl5;

import java.util.List;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.buffer.Buffer;
import io.vertx.mutiny.ext.web.client.HttpResponse;
import io.vertx.mutiny.ext.web.client.WebClient;

public abstract class AbstractEPPStage {

    public List<String> stagesLocations;

    private static final Logger LOGGER = Logger.getLogger("Impl5-Source-EPPStage");

    @Inject
    Endpoint endpoint;

    public AbstractEPPStage(List<String> stagesLocations) {
        this.stagesLocations = stagesLocations;
    }

    protected abstract Event execSelf(Event e);

    public Event exec(Event e) {
        e = this.execSelf(e);
        WebClient client = endpoint.getClient();
        for (String stagesLocation : this.stagesLocations) {
            LOGGER.info("Sending event to locations " + stagesLocation);
            Uni<HttpResponse<Buffer>> response = client
                    .postAbs("http://" + stagesLocation + "/exec")
                    .sendJson(e);

            response.subscribe().with(item -> LOGGER.info("Received " + item.statusCode()));
        }
        return e;
    }

    public void exec() {
        exec(null);
    }

    public void setStages(List<String> stagesLocations) {
        this.stagesLocations = stagesLocations;
    }
}
