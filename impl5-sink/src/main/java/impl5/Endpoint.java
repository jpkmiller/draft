package impl5;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.HttpResponse;
import io.vertx.mutiny.ext.web.client.WebClient;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Endpoint {

    private final WebClient client;

    public Endpoint() {
        Vertx vertx = Vertx.vertx();
        this.client = WebClient.create(vertx);
    }

    public WebClient getClient() {
        return this.client;
    }

    public Uni<String> register(EPPStage stage) {
        return this.client.postAbs("http://impl5-manager-service:8081/register")
                .sendJson(stage)
                .map(HttpResponse::bodyAsString);
    }

}