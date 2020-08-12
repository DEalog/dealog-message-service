package de.dealog.msg.rest;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;

@QuarkusTest
class ApiResourceTest {

    @Test
    void supported() {
        given()
                .param("version", "v1.0+json")
                .when().get(ApiResource.RESOURCE_PATH)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    void unsupported() {
        given()
                .param("version", "v1.666+json")
                .when().get(ApiResource.RESOURCE_PATH)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }
}