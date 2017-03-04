import dsl.TestBase;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import java.io.IOException;

public class HotelBookingApiTest extends TestBase {

    public HotelBookingApiTest() {
    }

    @Test
    public void shouldCreateBooking() throws IOException {
        hotelBookingApi.createBooking("first1", "last1", "50", "true", "2018-02-28", "2018-03-01");
        hotelBookingApi.getBooking("first1", true);
    }

    @Test
    public void shouldErrorWhenGettingInvalidBookingId() throws IOException {
        hotelBookingApi.getBooking("<1234>", false);
    }

    @Test
    public void shouldRemoveBooking() throws IOException {
        hotelBookingApi.createBooking("first2", "last2", "50", "true", "2018-02-28", "2018-03-01");
        hotelBookingApi.removeBooking("first2");
    }

    @AfterTest
    public void tearDown() throws IOException {
        tearDownBase();
    }
}
