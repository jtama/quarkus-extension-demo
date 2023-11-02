package org.acme.configurationProvider.it;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path("/acme")
public class AcmeResource {

    @ConfigProperty(name = "acme.devoxxFR.title")
    String devoxxfrConfTitle;

    @ConfigProperty(name = "acme.devoxxFR.author")
    String devoxxConfAuthor;

    @ConfigProperty(name = "acme.bdxIO.title")
    String bdxIOConfTitle;

    @ConfigProperty(name = "acme.bdxIO.author")
    String bdxIOConfAuthor;

    @ConfigProperty(name = "acme.dummy.title")
    String dummyConfTitle;

    @ConfigProperty(name = "acme.dummy.author")
    String dummyConfAuthor;

    @Path("/devoxxFR")
    @GET
    public String hellodevoxxFR() {
        return hello(devoxxConfAuthor, devoxxfrConfTitle);
    }

    @Path("/bdxio")
    @GET
    public String helloBdxio() {
        return hello(bdxIOConfAuthor, bdxIOConfTitle);
    }

    @Path("/")
    @GET
    public String hello() {
        return hello(dummyConfAuthor, dummyConfTitle);
    }

    private String hello(String conferenceAuthor, String talkTitle) {
        return "Welcome %s, that will present: \"%s\"".formatted(conferenceAuthor, talkTitle);
    }
}
