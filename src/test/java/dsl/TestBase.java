package dsl;

import java.io.IOException;
import java.util.Map;

public class TestBase {

    public TestContext testContext;
    public HotelBookingApi hotelBookingApi;
    public HotelBookingUi hotelBookingUi;

    public TestBase() {
        this.testContext = new TestContext();
        this.hotelBookingApi = new HotelBookingApi(testContext);
//        this.hotelBookingUi = new HotelBookingUi(testContext);
    }

    public void tearDownBookingIds() throws IOException {
        for (Map.Entry<String, Integer> entry : testContext.bookingIds.entrySet())
        {
            hotelBookingApi.removeBooking(entry.getKey());
        }
    }
}
