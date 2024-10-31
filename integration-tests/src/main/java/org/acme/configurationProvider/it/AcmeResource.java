package org.acme.configurationProvider.it;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Path("/acme")
public class AcmeResource {

    private static final Map<String, Map<String, String>> confByEvent = new HashMap<>();

    AcmeResource(@ConfigProperty(name = "env.jtama.title")
                         String jtamaConfTitle,
                         @ConfigProperty(name = "env.jtama.author")
                         String jtamaConfAuthor,
                         @ConfigProperty(name = "env.smetayer.title")
                         String smetayerConfTitle,
                         @ConfigProperty(name = "env.smetayer.author")
                         String smetayerConfAuthor,
                         @ConfigProperty(name = "env.dlib.title")
                         String dlgibConfTitle,
                         @ConfigProperty(name = "env.dlib.author")
                         String dlgibConfAuthor,
                         @ConfigProperty(name = "env.dummy.title")
                         String dummyConfTitle,
                         @ConfigProperty(name = "env.dummy.author")
                         String dummyConfAuthor) {
        confByEvent.putAll(Map.of(
                "smetayer", Map.of("title", smetayerConfTitle, "author", smetayerConfAuthor),
                "jtama", Map.of("title", jtamaConfTitle, "author", jtamaConfAuthor),
                "dlgib", Map.of("title", dlgibConfTitle, "author", dlgibConfAuthor),
                "dummy", Map.of("title", dummyConfTitle, "author", dummyConfAuthor)));
    }

    @Path("/{event}")
    @GET
    public Uni<Event> helloEvent(String event) {
        return Uni.createFrom().item(Event.fromMap(confByEvent.getOrDefault(event, confByEvent.get("dummy"))));
    }

    private record Event(String title, String author, String message) {

        public Event {
            Objects.requireNonNull(title);
            Objects.requireNonNull(author);
            message = "Welcome %s, that will present: \"%s\"".formatted(author, title);
        }

        public static Event fromMap(Map<String, String> conf) {
            return new Event(conf.get("title"), conf.get("author"), null);
        }

    }
}
