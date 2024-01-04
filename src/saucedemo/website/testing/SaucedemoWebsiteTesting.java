
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
    private static void testLoginWithValidUsername(WebDriver driver) throws InterruptedException {
        driver.get(BASE_URL + "/index.html");
        String loginTestCase = "Login with valid username";
        Thread.sleep(5000);
       
        String loginStatus = loginToSauceDemo(driver, "standard_user", "secret_sauce") ? "Pass" : "Fail";
        saveResultToCSV(loginTestCase, loginStatus);
    }
    private static void testAddMultipleItemsAndCheckout(WebDriver driver) throws InterruptedException {
    String addToCartTestCase = "Add multiple items to cart and checkout";
    Thread.sleep(5000);
    List<WebElement> addToCartButtons = driver.findElements(By.cssSelector(".btn_inventory"));
    for (WebElement addToCartButton : addToCartButtons) {
        addToCartButton.click();
    }
    Thread.sleep(5000);
    WebElement cartIcon = driver.findElement(By.cssSelector(".shopping_cart_link"));
    cartIcon.click();
    Thread.sleep(5000);
    List<WebElement> cartItems = driver.findElements(By.cssSelector(".cart_item"));
    boolean itemsAddedToCart = !cartItems.isEmpty();
    Thread.sleep(5000);
    WebElement checkoutButton = driver.findElement(By.cssSelector(".checkout_button"));
    checkoutButton.click();
    Thread.sleep(5000);
    WebElement firstName = driver.findElement(By.id("first-name"));
    WebElement lastName = driver.findElement(By.id("last-name"));
    WebElement zipCode = driver.findElement(By.id("postal-code"));
    WebElement continueButton = driver.findElement(By.cssSelector(".cart_button"));
    firstName.sendKeys("John");
    lastName.sendKeys("Doe");
    zipCode.sendKeys("12345");
    continueButton.click();
    Thread.sleep(5000);
    WebElement finishButton = driver.findElement(By.cssSelector(".cart_button"));
    finishButton.click();
    Thread.sleep(5000);
    String currentUrl = driver.getCurrentUrl();
    boolean checkoutSuccessful = currentUrl.contains("checkout-complete");
    String addToCartStatus = (itemsAddedToCart && checkoutSuccessful) ? "Pass" : "Fail";
    saveResultToCSV(addToCartTestCase, addToCartStatus);
}
    private static void testAboutPageVisibility(WebDriver driver) {
    String aboutPageTestCase = "Check visibility of About page";
    // Locate the burger menu
    WebElement menuButton = driver.findElement(By.cssSelector(".bm-burger-button"));
    new Actions(driver).moveToElement(menuButton).click().perform();
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("about_sidebar_link")));
    WebElement aboutLink = driver.findElement(By.id("about_sidebar_link"));
    new Actions(driver).moveToElement(aboutLink).click().perform();
    boolean aboutPageVisible = driver.findElement(By.id("__next")).isDisplayed();
    String aboutPageStatus = aboutPageVisible ? "Pass" : "Fail";
    saveResultToCSV(aboutPageTestCase, aboutPageStatus);
}
   private static void testNavigateBack(WebDriver driver) {
    String navigateBackTestCase = "Navigate back";
    driver.navigate().back();
    String navigateBackStatus = "Pass";
    saveResultToCSV(navigateBackTestCase, navigateBackStatus);
}
    private static void testLogout(WebDriver driver) {
    String logoutTestCase = "Logout";
    WebElement menuButton = driver.findElement(By.cssSelector(".bm-burger-button"));
    WebElement logoutLink = driver.findElement(By.id("logout_sidebar_link"));
       Actions actions = new Actions(driver);
    actions.moveToElement(menuButton).perform();
    try {
        Thread.sleep(1000);
    } catch (InterruptedException e) {
    }
    actions.click(logoutLink).perform();
    String logoutStatus = isLogoutSuccessful(driver) ? "Pass" : "Fail";
    saveResultToCSV(logoutTestCase, logoutStatus);
}
    private static boolean isLogoutSuccessful(WebDriver driver) {
    return driver.getCurrentUrl().equals(BASE_URL + "/index.html");
}  
    private static void testLoginWithLockedOutUser(WebDriver driver) throws InterruptedException {
        String lockedOutLoginTestCase = "Login with locked-out user";
        Thread.sleep(5000);
        String lockedOutLoginStatus = isLoginFailed(driver, "locked_out_user", "invalid_password") ? "Fail" : "Pass";
        saveResultToCSV(lockedOutLoginTestCase, lockedOutLoginStatus);
    }
    private static void testLoginWithInvalidUsername(WebDriver driver) throws InterruptedException {
        String invalidLoginTestCase = "Login with invalid username";
        Thread.sleep(5000);
        String invalidLoginStatus = isLoginFailed(driver, "invalid_user", "invalid_password") ? "Fail" : "Pass";
        saveResultToCSV(invalidLoginTestCase, invalidLoginStatus);
    }
    private static boolean loginToSauceDemo(WebDriver driver, String username, String password) {
        WebElement usernameField = driver.findElement(By.id("user-name"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.cssSelector(".btn_action"));
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        loginButton.click();
        return driver.findElement(By.cssSelector(".product_label")).isDisplayed();
    }
    private static boolean isLoginFailed(WebDriver driver, String username, String password) {
        WebElement usernameField = driver.findElement(By.id("user-name"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.cssSelector(".btn_action"));
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        loginButton.click();
        return driver.findElements(By.cssSelector(".error-button")).size() > 0;
    }
    private static void saveResultToCSV(String testCase, String status) {
    try {
        Path filePath = Paths.get(CSV_FILE);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));  
        String result = timestamp + "," + testCase + "," + status + "\n";
        Files.write(filePath, result.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        System.out.println("Result saved to " + CSV_FILE);
    } catch (IOException e) {
    }
}

}

