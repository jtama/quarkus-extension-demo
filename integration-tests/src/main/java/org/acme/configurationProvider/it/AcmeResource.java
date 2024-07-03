package org.acme.configurationProvider.it;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.HashMap;
import java.util.Map;

@Path("/acme")
public class AcmeResource {

    private final Map<String, Map<String, String>> confByEvent = new HashMap<>();

    AcmeResource(@ConfigProperty(name = "env.rivieradev.title")
                         String rivieraDevConfTitle,
                         @ConfigProperty(name = "env.rivieradev.author")
                         String rivieraDevConfAuthor,
                         @ConfigProperty(name = "env.devoxxfr.title")
                         String snowcampConfTitle,
                         @ConfigProperty(name = "env.devoxxfr.author")
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
    public String helloEvent(String event) {
        return hello(confByEvent.getOrDefault(event, confByEvent.get("dummy")));
    }

    private String hello(Map<String, String> conf) {
        return "Welcome %s, that will present: \"%s\"".formatted(conf.get("author"), conf.get("title"));
    }
}
