import dsl.TestBase;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import java.io.IOException;

public class HotelBookingUiTest extends TestBase {

    public HotelBookingUiTest() {
    }

    @Test
    public void shouldCreateBooking() throws IOException {
        hotelBookingUi.navigateToHotelBookingPage();
        hotelBookingUi.createBooking("firstCreate", "last", "50", "true", "2018-02-28", "2018-03-01");
        hotelBookingUi.verifyBookingByFirstName("firstCreate");
    }

    @Test
    public void shouldDeleteBooking() throws IOException {
        hotelBookingApi.createBooking("firstDelete", "last", "100", "true", "2018-02-28", "2018-03-01");
        hotelBookingUi.navigateToHotelBookingPage();
        hotelBookingUi.deleteBooking("firstDelete");
        hotelBookingApi.verifyNoBookingExists("firstDelete");
    }

    @AfterTest
    public void afterTest() throws IOException {
        tearDown();
    }
}
