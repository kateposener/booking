package dsl;

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
            hotelBookingApi.deleteBooking(entry.getKey());
        }
    }

    protected void tearDownBase() throws IOException {
        tearDownBookingIds();
        hotelBookingUi.closeBrowser();
    }
}
