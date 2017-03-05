package dsl;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;

public class TestBase {

    private TestContext testContext;
    protected HotelBookingApi hotelBookingApi;
    protected HotelBookingUi hotelBookingUi;

    protected TestBase() {
        this.testContext = new TestContext();
        this.hotelBookingApi = new HotelBookingApi(testContext);
        this.hotelBookingUi = new HotelBookingUi(testContext);
    }

    private void tearDownBookingIds() throws IOException {
        for (String key : testContext.bookingIds.keySet()) {
            hotelBookingApi.tearDownBooking(key);
        }
    }

    static String randomiseSuffix(final String input) {
        String randomString = RandomStringUtils.randomAlphanumeric(5);
        return input + randomString;
    }

    protected void tearDown() throws IOException {
        tearDownBookingIds();
        hotelBookingUi.closeBrowser();
    }
}
