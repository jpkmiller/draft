package impl5;

import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.WebClient;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Endpoint {
    public final WebClient client;

    public WebClient getClient() {
        return client;
    }

    public Endpoint() {
        Vertx vertx = Vertx.vertx();
        this.client = WebClient.create(vertx);
    }
}
