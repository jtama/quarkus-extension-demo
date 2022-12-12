package io.quarkiverse.approximationcorrector.it;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class CustomResourceTest {

    @Test
    public void testHelloEndpoint() {
        System.out.println(given()
                .when().get("/acme")
                .then()
                .statusCode(200)
                .extract()
                .headers().asList().toString());
    }
}
