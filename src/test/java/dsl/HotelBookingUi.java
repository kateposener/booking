package dsl;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.HotelBookingPage;

import java.util.List;

public class HotelBookingUi {

    WebDriver driver;
    private TestContext testContext;
    HotelBookingPage hotelBookingPage;

    String baseUrl = "http://hotel-test.equalexperts.io/";

    public HotelBookingUi(TestContext testContext) {
        this.testContext = testContext;
        ChromeDriverManager.getInstance().setup();
        this.driver = new ChromeDriver();
        this.hotelBookingPage = new HotelBookingPage(driver);
    }

    public void navigateToHotelBookingPage() {
        hotelBookingPage.navigateToHotelBookingPage(baseUrl);
    }

    public void createBooking(String firstName, String lastName, String totalPrice, String depositPaid,
                              String checkIn, String checkOut) {

        hotelBookingPage.createBooking(firstName, lastName, totalPrice, depositPaid, checkIn, checkOut);

        testContext.bookingFirstNames.add(firstName);

        hotelBookingPage.verifyBookingByFirstName(firstName);
    }

    public void closeBrowser() {
        driver.close();
    }

    public void removeAllBookings() {
        List<String> bookingFirstNames = testContext.bookingFirstNames;
        for (String firstName : bookingFirstNames) {
            hotelBookingPage.removeBooking(firstName);
        }
    }
}
