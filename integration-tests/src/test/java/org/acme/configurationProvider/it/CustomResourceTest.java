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
                .when().get("/acme/rivieraDev")
                .then()
                .statusCode(200)
                .extract();
        Assertions.assertNotNull(extract.headers().get("X-ApproximationCorrector"));
    }

    @Test
    public void helloEndpoint_should_have_body_from_env() {
        ExtractableResponse<Response> extract = given()
                .when().get("/acme/dummy")
                .then()
                .statusCode(200)
                .extract();
        Assertions.assertEquals("Welcome Monty Python, that will present: Always Look on the Bright Side of Life",
                extract.body().jsonPath().getString("message"));
    }
}
