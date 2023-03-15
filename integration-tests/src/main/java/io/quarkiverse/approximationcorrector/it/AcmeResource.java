package io.quarkiverse.approximationcorrector.it;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import io.quarkiverse.approximationcorrector.runtime.Environment;
import io.quarkiverse.approximationcorrector.runtime.EnvironmentValues;
import io.smallrye.mutiny.Uni;

@Path("/acme")
public class AcmeResource {

    @Environment("devoxxFR")
    EnvironmentValues devoxx;

    @Environment("bdxIO")
    EnvironmentValues bdxIO;

    @Environment("acme")
    EnvironmentValues acme;

    @Path("/devoxxFR")
    @GET
    public String hellodevoxxFR() {
        return "Hello %s, welcome to %s".formatted(devoxx.conference(), devoxx.title());
    }

    @Path("/bdxio")
    @GET
    public String helloBdxio() {
        return "Hello %s, welcome to %s".formatted(bdxIO.conference(), bdxIO.title());
    }

    @Path("/")
    @GET
    public String hello() {
        return "Hello %s, welcome to %s".formatted(acme.conference(), acme.title());
    }
}
