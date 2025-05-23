package utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import com.aventstack.extentreports.ExtentTest;

import java.util.Arrays;

/**
 * Utility class for logging REST API responses to Extent Reports.
 */
public class LogUtil {

    /**
     * Logs a formatted API response (JSON or plain text) to the Extent Report with request URL and a title.
     *
     * @param response     the REST-assured Response object received from the API call
     * @param test         the ExtentTest object used for logging the response to the report
     * @param title        a descriptive title to be shown in the report
     * @param endpointPath the relative path of the endpoint (appended to baseURI)
     */
    public static void logJsonResponse(Response response, ExtentTest test, String title, String endpointPath) {
        String fullUrl = RestAssured.baseURI + endpointPath;
        int statusCode = response.getStatusCode();
        String contentType = response.getContentType();
        boolean isJson = contentType != null && contentType.contains("application/json");

        StringBuilder logMessage = new StringBuilder();
        logMessage.append("<b>Request URL:</b> ").append(fullUrl).append("<br>");
        logMessage.append("<b>Response Received with Status Code: </b>").append(statusCode).append("<br>");

        if (isJson) {
            logMessage.append("<pre>").append(response.asPrettyString()).append("</pre>");
        } else {
            logMessage.append(response.asString());
        }

        test.info(title + "<br>" + logMessage);
    }

    public static boolean isStatusCodeExpected(Response response, ExtentTest test, int... expectedStatusCodes) {
        int actual = response.getStatusCode();
        boolean isExpected = Arrays.stream(expectedStatusCodes).anyMatch(code -> code == actual);

        if (!isExpected) {
            String expected = Arrays.toString(expectedStatusCodes);
            String message = String.format(
                    "Unexpected Status Code<br>Expected: %s<br>Actual: %d<br><br><b>Response:</b><br><pre>%s</pre>",
                    expected, actual, response.asPrettyString()
            );
            test.fail(message); // Extent Report logging only
        } else {
            test.pass("Status code " + actual + " is as expected.");
        }

        return isExpected;
    }

}

