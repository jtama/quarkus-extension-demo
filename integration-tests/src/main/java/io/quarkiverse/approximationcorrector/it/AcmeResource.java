package io.quarkiverse.approximationcorrector.it;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import io.quarkiverse.approximationcorrector.runtime.Environment;

@Path("/acme")
public class AcmeResource {

    @Environment
    String env;

    @GET
    public String hello() {
        return "Hello from %s".formatted(env);
    }
}
