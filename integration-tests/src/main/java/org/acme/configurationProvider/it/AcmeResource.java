package org.acme.configurationProvider.it;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Map;

@Path("/acme")
public class AcmeResource {

    private final Map<String, Map<String, String>> confByEvent;

    AcmeResource(@ConfigProperty(name = "env.devoxxFR.title")
                         String devoxxFRConfTitle,
                         @ConfigProperty(name = "env.devoxxFR.author")
                         String devoxxFRConfAuthor,
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
        confByEvent = Map.of("devoxxFR", Map.of("title", devoxxFRConfTitle, "author", devoxxFRConfAuthor),
                "maryCoreTech", Map.of("title", maryCoreTechConfTitle, "author", maryCoreTechConfAuthor),
                "snowcamp", Map.of("title", snowcampConfTitle, "author", snowcampConfAuthor),
                "daminouTech", Map.of("title", daminouTechConfTitle, "author", daminouTechConfAuthor),
                "dummy", Map.of("title", dummyConfTitle, "author", dummyConfAuthor));
    }

    @Path("/{event}")
    @GET
    public String hellodevoxxFR(String event) {
        return hello(confByEvent.get(event));
    }

    @GET
    public String hello() {
        return hello(confByEvent.get("dummy"));
    }

    private String hello(Map<String, String> conf) {
        return "Welcome %s, that will present: \"%s\"".formatted(conf.get("author"), conf.get("title"));
    }
}
