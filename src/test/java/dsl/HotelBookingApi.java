package dsl;

import com.google.gson.Gson;
import drivers.HttpDrivers;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import responses.GetHotelBookingResponse;
import responses.PostHotelBookingResponse;

import java.io.IOException;

public class HotelBookingApi {

    private HttpDrivers httpDrivers;
    private TestContext testContext;

    HotelBookingApi(TestContext testContext) {
        this.testContext = testContext;
        this.httpDrivers = new HttpDrivers();
    }

    public void createBooking(String firstName, String lastName, String totalPrice,
                              String depositPaid, String checkIn, String checkOut) throws IOException {

        GetHotelBookingResponse body = new GetHotelBookingResponse(firstName, lastName, totalPrice, depositPaid, checkIn, checkOut);
        Gson gson = new Gson();
        String postBody = gson.toJson(body);

        PostHotelBookingResponse response = httpDrivers.post("http://hotel-test.equalexperts.io/booking", postBody);

        if (response.bookingid != null) {
            testContext.bookingIds.put(firstName, Integer.parseInt(response.bookingid));
        }
        else {
            Assert.fail("Booking creation failed");
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
            Assert.assertNotNull(response, "Did not get response and was expecting one");
        }

        else {
            Assert.assertNull(response, "Got response when not expecting one");
        }
    }

    public void removeBooking(String bookingFirstName) throws IOException {
        final Integer bookingId = testContext.bookingIds.get(bookingFirstName);
        HttpResponse response = httpDrivers.delete("http://hotel-test.equalexperts.io/booking/" + bookingId);
        testContext.bookingIds.remove(bookingFirstName);

        Assert.assertEquals(response.getStatusLine().getStatusCode(), 201); // this is an incorrect response code!
    }

    public void verifyNoBookingExists(String bookingFirstName) throws IOException {
        final Integer bookingId = testContext.bookingIds.get(bookingFirstName);
        GetHotelBookingResponse response = httpDrivers.get("http://hotel-test.equalexperts.io/booking/" + bookingId, 404);
        Assert.assertNull(response);
    }

    private boolean isLiteral(final String input) {
        return input.charAt(0) == '<';
    }

    private String extractLiteral(String input) {
        return input.substring(1, input.length()-1);
    }
}
