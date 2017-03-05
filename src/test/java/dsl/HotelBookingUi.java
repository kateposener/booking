package dsl;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import pages.HotelBookingPage;

import static dsl.TestBase.randomiseSuffix;

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

        final String randomisedFirstName = randomiseSuffix(firstName);
        hotelBookingPage.createBooking(randomisedFirstName, lastName, totalPrice, depositPaid, checkIn, checkOut);

        testContext.bookingIds.put(firstName, hotelBookingPage.getBookingId(randomisedFirstName));
        testContext.bookingFirstNames.put(firstName, randomisedFirstName);
    }

    public void verifyBookingByFirstName(final String bookingFirstName) {
        final String firstName = testContext.bookingFirstNames.get(bookingFirstName);
        Assert.assertTrue(hotelBookingPage.verifyBookingByFirstName(firstName), "Booking was not found\n");
    }

    public void deleteBooking(String bookingFirstName) {
        final String firstName = testContext.bookingFirstNames.get(bookingFirstName);
        hotelBookingPage.deleteBooking(firstName);

        testContext.bookingIds.remove(bookingFirstName);
        testContext.bookingFirstNames.remove(bookingFirstName);
    }

    void closeBrowser() {
        driver.quit();
    }
}
