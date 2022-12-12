package io.quarkiverse.approximationcorrector.it;

import org.jboss.resteasy.reactive.ResponseHeader;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/acme")
public class AcmeResource {

    @GET
    public String hello() {
        return "Hello";
    }
}
