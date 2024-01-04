
package saucedemo.website.testing;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class SaucedemoWebsiteTesting {

  private static final String BASE_URL = "https://www.saucedemo.com/v1";
    private static final String CHROME_DRIVER_PATH = "C:\\Users\\Jihad\\Downloads\\Compressed\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe";
    private static final String CSV_FILE = "test_results.csv";
    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        testLoginWithValidUsername(driver);
        testAddMultipleItemsAndCheckout(driver);
        testAboutPageVisibility(driver);
        testNavigateBack(driver);
       
        testLogout(driver);
        testLoginWithLockedOutUser(driver);
        testLoginWithInvalidUsername(driver);
        driver.quit();
    }
    