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
        hotelBookingApi.verifyBookingExists("firstCreate");
    }

    @Test
    public void shouldErrorWhenEnteringInvalidPrice() throws IOException {
        // FIXME: this is a bug, you should not get a 500 error
        hotelBookingApi.createBookingWithError("firstCreate", "last", "text", "true", "2018-02-28", "2018-03-01", 500, "Internal Server Error");
    }

    @Test
    public void shouldGetBooking() throws IOException {
        hotelBookingApi.createBooking("firstGet", "last", "50", "true", "2018-02-28", "2018-03-01");
        hotelBookingApi.getBooking("firstGet");
    }

    @Test
    public void shouldErrorWhenGettingInvalidBookingId() throws IOException {
        hotelBookingApi.getBookingWithError("<1234>", 404, "Not Found");
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
