package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class HotelBookingPage {
    private WebDriver driver;

    @FindBy(id="firstname")
    WebElement firstname;
    @FindBy(id="lastname")
    WebElement lastname;
    @FindBy(id="totalprice")
    WebElement totalprice;
    @FindBy(id="depositpaid")
    WebElement depositpaid;
    @FindBy(id="checkin")
    WebElement checkin;
    @FindBy(id="checkout")
    WebElement checkout;
    @FindBy(xpath="//*[@id=\"form\"]/div/div[7]/input")
    WebElement saveButton;

    public HotelBookingPage(WebDriver driver) {

        this.driver = driver;

        PageFactory.initElements(driver, this);
    }

    public void navigateToHotelBookingPage(String baseUrl) {
        driver.get(baseUrl);
    }

    public void createBooking(String firstName, String lastName, String totalPrice, String depositPaid, String checkIn, String checkOut) {
        firstname.sendKeys(firstName);
        lastname.sendKeys(lastName);
        totalprice.sendKeys(totalPrice);
        depositpaid.sendKeys(depositPaid);
        checkin.sendKeys(checkIn);
        checkout.sendKeys(checkOut);
        saveButton.click();
    }

    public boolean verifyBookingByFirstName(final String firstname) {

        waitForNewRowDisplayed(firstname);

        List<WebElement> rows = driver.findElements(By.className("row"));

        for (WebElement row : rows)
        {
            if (row.getText().contains(firstname)) {
                return true;
            }
        }

        return false;
    }

    public void removeBooking(final String firstname) {

        waitForNewRowDisplayed(firstname);

        final Integer rowId = getBookingId(firstname);

        String deleteButtonXPathLocator = "//*[@id=\"" + rowId + "\"]/div[7]/input";
        driver.findElement(By.xpath(deleteButtonXPathLocator)).click();
    }

    private void waitForNewRowDisplayed(final String firstname) {

        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                try {
                    List<WebElement> rows = d.findElements(By.className("row"));
                    for (WebElement row : rows) {
                        if (row.getText().contains(firstname)) {
                            return true;
                        }
                    }
                }
                catch (Exception e) {
                    d.navigate().refresh();
                }
                return false;
            }
        });
    }

    public Integer getBookingId(final String firstname) {

        waitForNewRowDisplayed(firstname);

        List<WebElement> rows = driver.findElements(By.className("row"));

        for (WebElement row : rows)
        {
            if (row.getText().contains(firstname)) {
                String rowId = row.getAttribute("id");
                return Integer.valueOf(rowId);
            }
        }

        return null;
    }
}
