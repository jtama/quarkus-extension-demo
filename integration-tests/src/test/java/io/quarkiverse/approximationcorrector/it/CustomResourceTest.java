package io.quarkiverse.approximationcorrector.it;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@QuarkusTest
public class CustomResourceTest {

    @Test
    public void helloEndpoint_should_have_correct_header() {
        ExtractableResponse<Response> extract = given()
                .when().get("/acme")
                .then()
                .statusCode(200)
                .extract();
        Assertions.assertTrue(extract.headers().get("X-ApproximationCorrector") != null);
    }

    @Test
    public void helloEndpoint_should_have_body_from_env() {
        ExtractableResponse<Response> extract = given()
                .when().get("/acme")
                .then()
                .statusCode(200)
                .extract();
        Assertions.assertEquals("Hello Acme Looniversity, welcome to Why does Elmyra Duff love animals so much ?",
                extract.body().asString());
    }
}
