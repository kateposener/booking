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
        hotelBookingUi.createBooking("firstX", "lastX", "50", "true", "2018-02-28", "2018-03-01");
        hotelBookingUi.verifyBookingByFirstName("firstX");
    }

    @Test
    public void shouldRemoveBooking() throws IOException {
        hotelBookingApi.createBooking("firstY", "lastY", "100", "true", "2018-02-28", "2018-03-01");
        hotelBookingUi.navigateToHotelBookingPage();
        hotelBookingUi.deleteBooking("firstY");
        hotelBookingApi.verifyNoBookingExists("firstY");
    }

    @AfterTest
    public void tearDown() throws IOException {
        tearDownBase();
    }
}
