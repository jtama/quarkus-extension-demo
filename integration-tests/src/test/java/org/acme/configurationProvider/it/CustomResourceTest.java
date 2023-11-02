package org.acme.configurationProvider.it;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class CustomResourceTest {

    @Test
    public void helloEndpoint_should_have_correct_header() {
        ExtractableResponse<Response> extract = given()
                .when().get("/acme")
                .then()
                .statusCode(200)
                .extract();
        Assertions.assertNotNull(extract.headers().get("X-ApproximationCorrector"));
    }

    @Test
    public void helloEndpoint_should_have_body_from_env() {
        ExtractableResponse<Response> extract = given()
                .when().get("/acme")
                .then()
                .statusCode(200)
                .extract();
        Assertions.assertEquals("Welcome Malvin le Martien, that will present: \"Why does Elmyra Duff love animals so much ?\"",
                extract.body().asString());
    }
}
