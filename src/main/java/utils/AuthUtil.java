
package utils;

import io.restassured.http.ContentType;
import org.json.JSONObject;

import static io.restassured.RestAssured.given;

public class AuthUtil {
    /**
     * This method generates an authentication token by sending a POST request
     * with admin credentials to the /auth endpoint.
     *
     * @return the authentication token as a String
     */
    public static String getAuthToken() {
        JSONObject credentials = new JSONObject();
        credentials.put("username", "admin");
        credentials.put("password", "password123");

        return given()
                .contentType(ContentType.JSON)
                .body(credentials.toString())
                .post("/auth")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }

}
