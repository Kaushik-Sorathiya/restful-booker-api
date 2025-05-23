package utils;

/**
 * Utility class for generating test data for API testing.
 */
public class TestDataUtil {
    /**
     * Creates and returns a sample {@link Booking} object with predefined values.
     *
     * @return a sample Booking object populated with test data
     */
    public static Booking getSampleBooking() {
        Booking booking = new Booking();
        booking.firstname = "Jim";
        booking.lastname = "Brown";
        booking.totalprice = 111;
        booking.depositpaid = true;

        BookingDates dates = new BookingDates();
        dates.checkin = "2023-01-01";
        dates.checkout = "2023-01-02";
        booking.bookingdates = dates;
        booking.additionalneeds = "Breakfast";

        return booking;
    }
}
