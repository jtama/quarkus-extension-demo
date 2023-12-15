package org.acme;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.HashMap;
import java.util.Map;

@Path("/conferences")
@Produces(MediaType.APPLICATION_JSON)
public class ConferencesResource {

    static Map<String, Conference> conferences;

    static {
        conferences = new HashMap<>(Map.of(
                "maryCoreTech", new Conference("A mandonné, ça fatigue !", "Myra Cool"),
                "daminouTech", new Conference("Tchou la bise !", "Super Daminou"),
                "snowcamp", new Conference("Quarkus: Greener, Better, Faster, Stronger", "j.tama"),
                "devoxxFR", new Conference("#RetourAuxSources : Le cache HTTP", "Hubert Sablonnière ❤"),
                "dummy", new Conference("Why does Elmyra Duff love animals so much ?", "Malvin le Martien")
        ));
    }

    @GET
    @Path("{event}")
    public Uni<Conference> conferenceByName(String event) {
        return Uni.createFrom().item(conferences.computeIfAbsent(event, ignored -> new Conference("Always Look on the Bright Side of Life", "Monty Python")));
    }

    public record Conference(String title, String author) {

    }
}
