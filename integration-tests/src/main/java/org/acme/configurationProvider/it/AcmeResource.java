package org.acme.configurationProvider.it;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Map;

@Path("/acme")
public class AcmeResource {

    private final Map<String, Map<String, String>> confByEvent;

    AcmeResource(@ConfigProperty(name = "env.touraineTech.title")
                         String touraineTechConfTitle,
                         @ConfigProperty(name = "env.touraineTech.author")
                         String touraineTechConfAuthor,
                         @ConfigProperty(name = "env.snowcamp.title")
                         String snowcampConfTitle,
                         @ConfigProperty(name = "env.snowcamp.author")
                         String snowcampConfAuthor,
                         @ConfigProperty(name = "env.maryCoreTech.title")
                         String maryCoreTechConfTitle,
                         @ConfigProperty(name = "env.maryCoreTech.author")
                         String maryCoreTechConfAuthor,
                         @ConfigProperty(name = "env.daminouTech.title")
                         String daminouTechConfTitle,
                         @ConfigProperty(name = "env.daminouTech.author")
                         String daminouTechConfAuthor,
                         @ConfigProperty(name = "env.dummy.title")
                         String dummyConfTitle,
                         @ConfigProperty(name = "env.dummy.author")
                         String dummyConfAuthor) {
        confByEvent = Map.of("touraineTech", Map.of("title", touraineTechConfTitle, "author", touraineTechConfAuthor),
                "maryCoreTech", Map.of("title", maryCoreTechConfTitle, "author", maryCoreTechConfAuthor),
                "snowcamp", Map.of("title", snowcampConfTitle, "author", snowcampConfAuthor),
                "daminouTech", Map.of("title", daminouTechConfTitle, "author", daminouTechConfAuthor),
                "dummy", Map.of("title", dummyConfTitle, "author", dummyConfAuthor));
    }

    @Path("/{event}")
    @GET
    public String helloEvent(String event) {
        return hello(confByEvent.getOrDefault(event, confByEvent.get("dummy")));
    }

    private String hello(Map<String, String> conf) {
        return "Welcome %s, that will present: \"%s\"".formatted(conf.get("author"), conf.get("title"));
    }
}
