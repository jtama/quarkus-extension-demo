package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class ConferencesResourceTest {


    @Test
    public void testHelloEndpoint() {
        Map<String, Map<String, String>> result = given()
                .when().get("/conferences")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(Map.class);
        Assertions.assertEquals("Quarkus: Greener, Better, Faster, Stronger", result.get("snowcamp").get("title"));
        Assertions.assertEquals("#RetourAuxSources : Le cache HTTP", result.get("devoxxFR").get("title"));
    }

}
