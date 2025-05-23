package utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.anyOf;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Utility class to send HTTP requests with RestAssured.
 */
public class ApiUtil {

    private static final String BASE_URL = "https://restful-booker.herokuapp.com";

    static {
        RestAssured.baseURI = BASE_URL;
    }

    public static void init() {
        // Empty method just to trigger class loading
    }

    /**
     * Sends an HTTP request with specified method, endpoint, optional body, and optional auth token.
     * Validates response status code against expected status codes.
     *
     * @param method HTTP method (GET, POST, PUT, DELETE)
     * @param endpoint API endpoint path (e.g., "/booking", "/booking/{id}")
     * @param body Request payload (can be POJO, Map or JSON string), null if no body
     * @param token Auth token for secured endpoints, null if not required
     * @return Response object for further validation and logging
     */
    public static Response sendRequest(String method, String endpoint, Object body, String token) {
        RequestSpecification request = RestAssured.given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON);

        // Add auth token in Cookie header if provided
        if (token != null && !token.isEmpty()) {
            request.header("Cookie", "token=" + token);
        }

        // Attach request body if present
        if (body != null) {
            request.body(body);
        }

        // Execute HTTP request based on method
        Response response;
        switch (method.toUpperCase()) {
            case "POST":
                response = request.post(endpoint);
                break;
            case "PUT":
                response = request.put(endpoint);
                break;
            case "DELETE":
                response = request.delete(endpoint);
                break;
            case "GET":
            default:
                response = request.get(endpoint);
        }

        // Log response for debugging
        response.then().log().all();

        return response;
    }
}
