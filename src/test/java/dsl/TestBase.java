package dsl;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;
import java.util.Map;

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
        for (Map.Entry<String, Integer> entry : testContext.bookingIds.entrySet())
        {
            hotelBookingApi.tearDownBooking(entry.getKey());
        }
    }

    protected static String randomiseSuffix(final String input) {
        String randomString = RandomStringUtils.randomAlphanumeric(5);
        return input + randomString;
    }

    protected void tearDown() throws IOException {
        tearDownBookingIds();
        hotelBookingUi.closeBrowser();
    }
}
