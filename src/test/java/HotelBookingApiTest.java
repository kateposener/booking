import dsl.TestBase;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import java.io.IOException;

public class HotelBookingApiTest extends TestBase {

    public HotelBookingApiTest() {
    }

    @Test
    public void shouldCreateBooking() throws IOException {
        hotelBookingApi.createBooking("firstCreate", "last", "50", "true", "2018-02-28", "2018-03-01");
        hotelBookingApi.getBooking("firstCreate", true);
    }

    @Test
    public void shouldErrorWhenGettingInvalidBookingId() throws IOException {
        hotelBookingApi.getBooking("<1234>", false);
    }

    @Test
    public void shouldDeleteBooking() throws IOException {
        hotelBookingApi.createBooking("firstDelete", "last", "50", "true", "2018-02-28", "2018-03-01");
        hotelBookingApi.deleteBooking("firstDelete");
        hotelBookingApi.verifyNoBookingExists("firstDelete");
    }

    @AfterTest
    public void afterTest() throws IOException {
        tearDown();
    }
}
