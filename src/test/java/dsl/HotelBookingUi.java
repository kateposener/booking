package dsl;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.HotelBookingPage;

public class HotelBookingUi {

    private WebDriver driver;
    private TestContext testContext;
    private HotelBookingPage hotelBookingPage;

    private String baseUrl = "http://hotel-test.equalexperts.io/";

    HotelBookingUi(TestContext testContext) {
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

        testContext.bookingIds.put(firstName, hotelBookingPage.getBookingId(firstName));

        hotelBookingPage.verifyBookingByFirstName(firstName);
    }

    void closeBrowser() {
        driver.quit();
    }
}
