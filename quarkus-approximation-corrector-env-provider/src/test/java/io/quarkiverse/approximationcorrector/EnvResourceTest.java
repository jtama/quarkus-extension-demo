package io.quarkiverse.approximationcorrector;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.responseSpecification;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class EnvResourceTest {


    @Test
    public void testHelloEndpoint() {
        Map<String, String> result = given()
                .when().get("/hello")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(Map.class);
        Assertions.assertEquals("Quarkus : One step beyond !", result.get("TITLE"));
        Assertions.assertEquals("DevoxxFR 2023",result.get("CONFERENCE"));
    }

}
