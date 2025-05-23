package tests;

import base.BaseTest;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.*;

import static org.hamcrest.Matchers.*;

/**
 * End-to-end API tests for Booking functionality.
 * Covers Create, Read, Update, and Delete (CRUD) operations.
 */
public class BookingTests extends BaseTest {

    // API endpoint for booking operations
    private static final String BOOKING_ENDPOINT = "/booking";

    // Thread-safe storage for booking ID to support parallel test execution
    private static ThreadLocal<Integer> bookingId = new ThreadLocal<>();

    /**
     * Test to verify that all bookings can be retrieved.
     * Sends a GET request and asserts the response is not empty.
     */
    @Test(description = "Verify all bookings can be retrieved")
    public void testGetAllBookings() {
        test = extent.createTest("Get All Bookings");

        String endpointPath = BOOKING_ENDPOINT;
        Response response = ApiUtil.sendRequest("GET", endpointPath, null, null);

        // Status code validation with logging
        if (!LogUtil.isStatusCodeExpected(response, test, 200)) {
            throw new RuntimeException("Unexpected status code for " + endpointPath);
        }

        ValidatableResponse validatableResponse = response.then();
        validatableResponse.body("$", not(empty())); // Asserts response JSON array is not empty

        LogUtil.logJsonResponse(response, test, test.getModel().getName(), endpointPath);
    }

    /**
     * Test to create a new booking using data from DataProvider.
     * Sends a POST request and stores the booking ID for future tests.
     *
     * @param booking Sample booking data provided by DataProvider
     */
    @Test(dataProvider = "bookingData", description = "Create a new booking")
    public void testCreateBooking(Booking booking) {
        test = extent.createTest("Create Booking");

        String endpointPath = BOOKING_ENDPOINT;
        Response response = ApiUtil.sendRequest("POST", endpointPath, booking, null);

        // Status code validation with logging
        if (!LogUtil.isStatusCodeExpected(response, test, 200,201)) {
            throw new RuntimeException("Unexpected status code for " + endpointPath);
        }

        // Store booking ID for use in dependent tests
        bookingId.set(response.jsonPath().getInt("bookingid"));

        LogUtil.logJsonResponse(response, test, test.getModel().getName(), endpointPath);
    }

    /**
     * Test to retrieve the booking by ID and assert all required fields are present.
     * Depends on testCreateBooking.
     */
    @Test(dependsOnMethods = "testCreateBooking", description = "Get booking details by ID with detailed assertions")
    public void testGetBookingById() {
        test = extent.createTest("Get Booking By ID");

        String endpointPath = getBookingPath();
        Response response = ApiUtil.sendRequest("GET", endpointPath, null, null);

        // Status code validation with logging
        if (!LogUtil.isStatusCodeExpected(response, test, 200)) {
            throw new RuntimeException("Unexpected status code for " + endpointPath);
        }

        ValidatableResponse validatableResponse = response.then();
        validatableResponse
                .body("firstname", notNullValue())
                .body("lastname", notNullValue())
                .body("totalprice", greaterThan(0))
                .body("depositpaid", anyOf(is(true), is(false)))
                .body("bookingdates.checkin", notNullValue())
                .body("bookingdates.checkout", notNullValue())
                .body("additionalneeds", notNullValue());

        LogUtil.logJsonResponse(response, test, test.getModel().getName(), endpointPath);
    }

    /**
     * Test to update an existing booking with new data and validate all fields are updated.
     * Depends on testCreateBooking.
     */
    @Test(dependsOnMethods = "testCreateBooking", description = "Update existing booking with detailed assertions")
    public void testUpdateBooking() {
        test = extent.createTest("Update Booking");

        String token = AuthUtil.getAuthToken(); // Obtain auth token
        String endpointPath = getBookingPath();

        // Prepare updated booking details
        Booking booking = new Booking();
        booking.firstname = "Updated";
        booking.lastname = "Name";
        booking.totalprice = 222;
        booking.depositpaid = false;
        booking.additionalneeds = "Lunch";

        BookingDates dates = new BookingDates();
        dates.checkin = "2023-01-01";
        dates.checkout = "2023-01-03";
        booking.bookingdates = dates;

        // Send PUT request to update booking
        Response response = ApiUtil.sendRequest("PUT", endpointPath, booking, token);

        // Status code validation with logging
        if (!LogUtil.isStatusCodeExpected(response, test, 200)) {
            throw new RuntimeException("Unexpected status code for " + endpointPath);
        }

        // Validate updated fields
        ValidatableResponse validatableResponse = response.then();
        validatableResponse
                .body("firstname", equalTo("Updated"))
                .body("lastname", equalTo("Name"))
                .body("totalprice", equalTo(222))
                .body("depositpaid", equalTo(false))
                .body("additionalneeds", equalTo("Lunch"))
                .body("bookingdates.checkin", equalTo("2023-01-01"))
                .body("bookingdates.checkout", equalTo("2023-01-03"));

        LogUtil.logJsonResponse(response, test, test.getModel().getName(), endpointPath);
    }

    /**
     * Test to delete the booking by ID and confirm deletion with a 404 GET response.
     * Depends on testCreateBooking.
     */
    @Test(dependsOnMethods = "testCreateBooking", description = "Delete booking by ID and confirm deletion")
    public void testDeleteBooking() {
        test = extent.createTest("Delete Booking");

        String token = AuthUtil.getAuthToken(); // Obtain auth token
        String endpointPath = getBookingPath();

        // Send DELETE request
        Response response = ApiUtil.sendRequest("DELETE", endpointPath, null, token);

        // Status code validation with logging
        if (!LogUtil.isStatusCodeExpected(response, test, 201, 200)) {
            throw new RuntimeException("Unexpected status code for " + endpointPath);
        }

        // Assert the status code is either 200 or 201 (successful deletion)
        int statusCode = response.getStatusCode();
        Assert.assertTrue(statusCode == 200 || statusCode == 201,
                "Unexpected status code for delete booking: " + statusCode);

        LogUtil.logJsonResponse(response, test, test.getModel().getName(), endpointPath);

        // Confirm deletion by trying to GET the deleted booking and expecting 404
        Response getResponse = ApiUtil.sendRequest("GET", endpointPath, null, null);

        // Status code validation with logging
        if (!LogUtil.isStatusCodeExpected(getResponse, test, 404)) {
            throw new RuntimeException("Unexpected status code for " + endpointPath);
        }

        LogUtil.logJsonResponse(getResponse, test, "Get Booking after deletion : ", endpointPath);
    }

    /**
     * DataProvider for creating sample booking test data.
     *
     * @return 2D Object array with Booking objects
     */
    @DataProvider(name = "bookingData")
    public Object[][] bookingData() {
        return new Object[][]{{TestDataUtil.getSampleBooking()}};
    }

    /**
     * Builds the complete booking path using the stored booking ID.
     *
     * @return String representing endpoint with booking ID
     */
    private String getBookingPath() {
        return BOOKING_ENDPOINT + "/" + bookingId.get();
    }
}
