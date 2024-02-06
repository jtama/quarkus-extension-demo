package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class ConferencesResourceTest {


    @Test
    public void testUnknownConference() {
        ConferencesResource.Conference result = given()
                .when().get("/conferences/unknown")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(ConferencesResource.Conference.class);
        Assertions.assertEquals("Always Look on the Bright Side of Life", result.title());
        Assertions.assertEquals("Monty Python", result.author());
    }

    @Test
    public void testDummyConference() {
        ConferencesResource.Conference result = given()
                .when().get("/conferences/dummy")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(ConferencesResource.Conference.class);
        Assertions.assertEquals("Why does Elmyra Duff loves animals so much ?", result.title());
        Assertions.assertEquals("Malvin le Martien", result.author());
    }

}
