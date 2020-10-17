package de.dealog.msg.rest;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class ApiResourceTest {

    private static final String UNSUPPORTED_VERSION = "v1.666+json";
    private static final String SUPPORTED_VERSION = "v1.0+json";

    @Test
    void supported() {
        given()
                .param(ApiResource.QUERY_PARAM_VERSION, SUPPORTED_VERSION)
                .when().get(ApiResource.RESOURCE_PATH)
                .then()
                .statusCode(404);
    }

    @Test
    void unsupported() {
        given()
                .param(ApiResource.QUERY_PARAM_VERSION, UNSUPPORTED_VERSION)
                .when().get(ApiResource.RESOURCE_PATH)
                .then()
                .statusCode(204);
    }
}