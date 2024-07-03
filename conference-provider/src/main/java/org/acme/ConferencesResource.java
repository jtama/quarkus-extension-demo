package org.acme;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithParentName;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/conferences")
@Produces(MediaType.APPLICATION_JSON)
public class ConferencesResource {


    @Inject
    Conferences conferences;

    @GET
    @Path("{event}")
    public Uni<Conference> conferenceByName(String event) {
        return Uni.createFrom().item(
                Conference.fromTalk(conferences.values().computeIfAbsent(event,
                        _ -> conferences.values().get("default"))));
    }

    @GET
    public Uni<List<Conference>> conferenceByName() {
        return Uni.createFrom().item(
                conferences.values().values().stream().map(Conference::fromTalk).toList());
    }


    @ConfigMapping(prefix = "conference")
    public interface Conferences {

        @WithParentName
        Map<String, Talk> values();
    }

    public interface Talk {
        String title();
        String author();
    }

    record Conference(String title, String author) {
        static Conference fromTalk(Talk talk){
            return new Conference(talk.title(), talk.author());
        }
    }
}
