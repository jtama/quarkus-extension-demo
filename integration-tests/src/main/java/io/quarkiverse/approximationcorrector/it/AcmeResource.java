package io.quarkiverse.approximationcorrector.it;

import org.jboss.resteasy.reactive.ResponseHeader;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/acme")
public class AcmeResource {

    @GET
    public String hello() {
        return "Hello";
    }
}
