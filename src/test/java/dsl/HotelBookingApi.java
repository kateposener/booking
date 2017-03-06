package dsl;

import com.google.gson.Gson;
import drivers.HttpDrivers;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
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
        postWithExpectedStatus(firstName, lastName, totalPrice, depositPaid, checkIn, checkOut, 200, "");
    }

    public void createBookingWithError(String firstName, String lastName, String totalPrice,
                                       String depositPaid, String checkIn, String checkOut, int expectedStatusCode,
                                       String expectedErrorMessage) throws IOException {
        postWithExpectedStatus(firstName, lastName, totalPrice, depositPaid, checkIn, checkOut, expectedStatusCode, expectedErrorMessage);
    }

    public void getBooking(final String bookingFirstName) throws IOException {
        getWithExpectedStatus(bookingFirstName, 200, "");
    }

    public void getBookingWithError(String bookingFirstName, int expectedStatusCode, String expectedErrorMessage) throws IOException {
        getWithExpectedStatus(bookingFirstName, expectedStatusCode, expectedErrorMessage);
    }

    public void deleteBooking(String bookingFirstName) throws IOException {
        // FIXME: this is an incorrect response code
        deleteWithExpectedStatus(bookingFirstName, 201, "");
    }

    public void deleteBookingWithError(String bookingFirstName, int expectedStatusCode, String expectedErrorMessage) throws IOException {
        deleteWithExpectedStatus(bookingFirstName, expectedStatusCode, expectedErrorMessage);
    }

    void tearDownBooking(String bookingFirstName) throws IOException {
        try {
            final Integer bookingId = testContext.bookingIds.get(bookingFirstName);
            httpDrivers.delete("http://hotel-test.equalexperts.io/booking/" + bookingId);
        } catch (Exception e) {
        }
    }

    public void verifyNoBookingExists(String bookingFirstName) throws IOException {
        getWithExpectedStatus(bookingFirstName, 404, "Not Found");
    }

    public void verifyBookingExists(String bookingFirstName) throws IOException {
        getWithExpectedStatus(bookingFirstName, 200, "");
    }

    private boolean isLiteral(final String input) {
        return input.charAt(0) == '<';
    }

    private String extractLiteral(String input) {
        return input.substring(1, input.length() - 1);
    }

    private void postWithExpectedStatus(String firstName, String lastName, String totalPrice,
                                        String depositPaid, String checkIn, String checkOut, int expectedStatusCode,
                                        String expectedErrorMessage) throws IOException {

        final String randomisedFirstName = randomiseSuffix(firstName);
        GetHotelBookingResponse body = new GetHotelBookingResponse(randomisedFirstName, lastName, totalPrice, depositPaid, checkIn, checkOut);
        Gson gson = new Gson();
        String postBody = gson.toJson(body);

        HttpResponse response = httpDrivers.post("http://hotel-test.equalexperts.io/booking", postBody);

        HttpEntity responseBody = response.getEntity();

        Assert.assertEquals(response.getStatusLine().getStatusCode(), expectedStatusCode, "Unexpected status code\n");

        if (expectedStatusCode == 200) {
            PostHotelBookingResponse postHotelBookingResponse = gson.fromJson(EntityUtils.toString(responseBody), PostHotelBookingResponse.class);

            testContext.bookingIds.put(firstName, Integer.parseInt(postHotelBookingResponse.bookingid));
            testContext.bookingFirstNames.put(firstName, randomisedFirstName);

            Assert.assertEquals(postHotelBookingResponse.booking.firstname, randomisedFirstName);
            Assert.assertEquals(postHotelBookingResponse.booking.lastname, lastName);
            Assert.assertEquals(postHotelBookingResponse.booking.totalprice, totalPrice);
            Assert.assertEquals(postHotelBookingResponse.booking.depositpaid, depositPaid);
            Assert.assertEquals(postHotelBookingResponse.booking.bookingdates.checkin, checkIn);
            Assert.assertEquals(postHotelBookingResponse.booking.bookingdates.checkout, checkOut);
        }
        else {
            Assert.assertEquals(EntityUtils.toString(responseBody), expectedErrorMessage, "Did not get expected error message\n");
        }
    }

    private void getWithExpectedStatus(final String bookingFirstName, final int expectedStatusCode, final String expectedErrorMessage) throws IOException {

        final String bookingId;
        Gson gson = new Gson();

        if (isLiteral(bookingFirstName)) {
            bookingId = extractLiteral(bookingFirstName);
        }
        else {
            bookingId = testContext.bookingIds.get(bookingFirstName).toString();
        }

        HttpResponse response = httpDrivers.get("http://hotel-test.equalexperts.io/booking/" + bookingId);
        HttpEntity responseBody = response.getEntity();

        Assert.assertEquals(response.getStatusLine().getStatusCode(), expectedStatusCode, "Unexpected status code\n");

        if (expectedStatusCode == 200) {
            GetHotelBookingResponse getHotelBookingResponse = gson.fromJson(EntityUtils.toString(responseBody), GetHotelBookingResponse.class);

            Assert.assertEquals(getHotelBookingResponse.firstname, testContext.bookingFirstNames.get(bookingFirstName), "Did not get expected first name\n");
        }
        else {
            Assert.assertEquals(EntityUtils.toString(responseBody), expectedErrorMessage, "Did not get expected error message\n");
        }
    }

    private void deleteWithExpectedStatus(String bookingFirstName, int expectedStatusCode, String expectedErrorMessage) throws IOException {

        final Integer bookingId = testContext.bookingIds.get(bookingFirstName);
        HttpResponse response = httpDrivers.delete("http://hotel-test.equalexperts.io/booking/" + bookingId);

        Assert.assertEquals(response.getStatusLine().getStatusCode(), expectedStatusCode, "Booking not deleted\n");

        if (expectedStatusCode != 201) {
            HttpEntity responseBody = response.getEntity();
            Assert.assertEquals(EntityUtils.toString(responseBody), expectedErrorMessage, "Did not get expected error message\n");
        }
    }
}
