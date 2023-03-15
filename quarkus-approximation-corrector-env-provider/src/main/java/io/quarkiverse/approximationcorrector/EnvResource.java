package io.quarkiverse.approximationcorrector;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.Map;
import java.util.stream.Collectors;

@Path("/environments")

public class EnvResource {

    static Map<String, String> env;

    static {
        env = Map.of(
                "devoxxFR.CONFERENCE", "DevoxxFR 2023",
                "devoxxFR.TITLE", "Quarkus : One step beyond !",
                "bdxIO.CONFERENCE", "BDX.io 2023",
                "bdxIO.TITLE", "alilce au pays d'OpenTelemetry",
                "acme.CONFERENCE", "Acme Looniversity",
                "acme.TITLE", "Why does Elmyra Duff love animals so much ?");
    }

    @GET
    @Path("{prefix}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Map<String, String>> hello(String prefix) {
        return Uni.createFrom().item(EnvResource.env.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(prefix))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }
}
