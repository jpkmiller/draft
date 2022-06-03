package impl5;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.buffer.Buffer;
import io.vertx.mutiny.ext.web.client.HttpResponse;
import io.vertx.mutiny.ext.web.client.WebClient;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@ApplicationScoped
public class EPPStage {

    private static final Logger LOGGER = Logger.getLogger("Impl5-Source-AbstractEPPStage");
    @Inject
    Endpoint endpoint;
    private List<String> stagesLocations;
    private List<Event> events;

    public EPPStage() {
        this(new ArrayList<>());
    }

    public EPPStage(List<String> stagesLocations) {
        this.stagesLocations = stagesLocations;
        this.events = new LinkedList<>();
    }

    public void exec() {
        if (this.events.size() == 0) {
            LOGGER.info("No Events in Buffer");
            return;
        }
        /* get next event from list (FIFO) and execute it */
        Event e = this.events.remove(0);

        e = new Event(e.data + " cool.");
        LOGGER.info("Executed Event " + e + " with data: " + e.data);
        execOthers(e);
        this.exec();
    }

    /**
     * Should be called asynchronously.
     * Executes the event and sends it to the next stage.
     */
    public void execOthers(Event e) {
        /* send event to next stages */
        WebClient client = endpoint.getClient();
        for (String stagesLocation : this.stagesLocations) {
            LOGGER.info("Sending event to locations " + stagesLocation);
            Uni<HttpResponse<Buffer>> response = client
                    .postAbs("http://" + stagesLocation + "/exec")
                    .sendJson(e);

            response.subscribe().with(item -> LOGGER.info("Received " + item.statusCode()));
        }
    }

    public void setStages(List<String> stagesLocations) {
        this.stagesLocations = stagesLocations;
    }

    public void addEvent(Event e) {
        this.events.add(e);
        LOGGER.info("Successfully added Event");
    }

    public List<String> getStagesLocations() {
        return this.stagesLocations;
    }

    public void setStagesLocations(List<String> stagesLocations) {
        this.stagesLocations = stagesLocations;
    }
}
