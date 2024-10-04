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

    AcmeResource(@ConfigProperty(name = "env.rivieradev.title")
                         String rivieraDevConfTitle,
                         @ConfigProperty(name = "env.rivieradev.author")
                         String rivieraDevConfAuthor,
                         @ConfigProperty(name = "env.bdxjug.title")
                         String snowcampConfTitle,
                         @ConfigProperty(name = "env.bdxjug.author")
                         String snowcampConfAuthor,
                         @ConfigProperty(name = "env.marycoretech.title")
                         String maryCoreTechConfTitle,
                         @ConfigProperty(name = "env.marycoretech.author")
                         String maryCoreTechConfAuthor,
                         @ConfigProperty(name = "env.daminoutech.title")
                         String daminouTechConfTitle,
                         @ConfigProperty(name = "env.daminoutech.author")
                         String daminouTechConfAuthor,
                         @ConfigProperty(name = "env.dummy.title")
                         String dummyConfTitle,
                         @ConfigProperty(name = "env.dummy.author")
                         String dummyConfAuthor) {
        confByEvent.putAll(Map.of("rivieraDev", Map.of("title", rivieraDevConfTitle, "author", rivieraDevConfAuthor),
                "maryCoreTech", Map.of("title", maryCoreTechConfTitle, "author", maryCoreTechConfAuthor),
                "devoxxFR", Map.of("title", snowcampConfTitle, "author", snowcampConfAuthor),
                "daminouTech", Map.of("title", daminouTechConfTitle, "author", daminouTechConfAuthor),
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
