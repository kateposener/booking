package dsl;

import com.google.gson.Gson;
import drivers.HttpDrivers;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import responses.GetHotelBookingResponse;
import responses.PostHotelBookingResponse;

import java.io.IOException;

import static dsl.TestBase.randomiseSuffix;

public class HotelBookingApi {

    private HttpDrivers httpDrivers;
    private TestContext testContext;

    HotelBookingApi(TestContext testContext) {
        this.testContext = testContext;
        this.httpDrivers = new HttpDrivers();
    }

    public void createBooking(String firstName, String lastName, String totalPrice,
                              String depositPaid, String checkIn, String checkOut) throws IOException {

        final String randomisedFirstName = randomiseSuffix(firstName);
        GetHotelBookingResponse body = new GetHotelBookingResponse(randomisedFirstName, lastName, totalPrice, depositPaid, checkIn, checkOut);
        Gson gson = new Gson();
        String postBody = gson.toJson(body);

        PostHotelBookingResponse response = httpDrivers.post("http://hotel-test.equalexperts.io/booking", postBody);

        if (response.bookingid != null) {
            testContext.bookingIds.put(firstName, Integer.parseInt(response.bookingid));
            testContext.bookingFirstNames.put(firstName, randomisedFirstName);
        }
        else {
            Assert.fail("Booking creation failed\n");
        }
    }

    public void getBooking(final String bookingFirstName, final boolean expectResponse) throws IOException {
        final String bookingId;

        if (isLiteral(bookingFirstName)) {
            bookingId = extractLiteral(bookingFirstName);
        }
        else {
            bookingId = testContext.bookingIds.get(bookingFirstName).toString();
        }

        GetHotelBookingResponse response = httpDrivers.get("http://hotel-test.equalexperts.io/booking/" + bookingId, 200);

        if (expectResponse)
        {
            Assert.assertNotNull(response, "Did not get response and was expecting one\n");
        }

        else {
            Assert.assertNull(response, "Got response when not expecting one\n");
        }
    }

    public void deleteBooking(String bookingFirstName) throws IOException {
        final Integer bookingId = testContext.bookingIds.get(bookingFirstName);
        HttpResponse response = httpDrivers.delete("http://hotel-test.equalexperts.io/booking/" + bookingId);

        Assert.assertEquals(response.getStatusLine().getStatusCode(), 201, "Booking not deleted\n"); // this is an incorrect response code!
    }

    void tearDownBooking(String bookingFirstName) throws IOException {
        try {
            final Integer bookingId = testContext.bookingIds.get(bookingFirstName);
            httpDrivers.delete("http://hotel-test.equalexperts.io/booking/" + bookingId);
        }
        catch (Exception e) {
        }
    }

    public void verifyNoBookingExists(String bookingFirstName) throws IOException {
        getBooking(bookingFirstName, false);
    }

    public void verifyBookingExists(String bookingFirstName) throws IOException {
        getBooking(bookingFirstName, true);
    }

    private boolean isLiteral(final String input) {
        return input.charAt(0) == '<';
    }

    private String extractLiteral(String input) {
        return input.substring(1, input.length()-1);
    }
}
