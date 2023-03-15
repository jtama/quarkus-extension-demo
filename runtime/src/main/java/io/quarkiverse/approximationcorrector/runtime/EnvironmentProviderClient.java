package io.quarkiverse.approximationcorrector.runtime;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import java.util.Map;

@Path("/environments")
@Consumes(MediaType.APPLICATION_JSON)
public interface EnvironmentProviderClient {

    @GET
    @Path("{prefix}")
    Map<String, String> getEnvironment(String prefix);
}
