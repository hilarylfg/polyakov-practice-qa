import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShareLaneTest {

    private static final String MAIN_URL = "https://sharelane.com/cgi-bin/main.py";
    private static final String REGISTER_URL = "https://sharelane.com/cgi-bin/register.py";
    private static final String ADD_TO_CART_URL = "https://sharelane.com/cgi-bin/add_to_cart.py?book_id=4";
    private static final String SHOPPING_CART_URL = "https://sharelane.com/cgi-bin/shopping_cart.py";
    private static final String CREDIT_CARD_URL = "https://sharelane.com/cgi-bin/get_credit_card.py";

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void checkZipCode4digits() {
        driver.get(REGISTER_URL);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("zip_code")));
        driver.findElement(By.name("zip_code")).sendKeys("1234");
        driver.findElement(By.cssSelector("[value='Continue']")).click();

        String error = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".error_message"))).getText();
        Assert.assertEquals(error, "Oops, error on page. ZIP code should have 5 digits");

        System.out.println("ZIP из 4 цифр отклонён — тест пройден");
    }

    @Test
    public void validZipOpensRegistrationForm() {
        driver.get(REGISTER_URL);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("zip_code")));
        driver.findElement(By.name("zip_code")).sendKeys("10001");
        driver.findElement(By.cssSelector("[value='Continue']")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("first_name")));

        System.out.println("Форма регистрации открылась — тест пройден");
    }

    @Test
    public void loginWithWrongPasswordShowsError() {
        String email = registerNewUser();

        driver.get(MAIN_URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email")));
        driver.findElement(By.name("email")).sendKeys(email);
        driver.findElement(By.name("password")).sendKeys("0000");
        driver.findElement(By.cssSelector("[value='Login']")).click();

        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), "match our records"));
        Assert.assertTrue(Objects.requireNonNull(driver.getPageSource()).contains("match our records"), "Должна быть ошибка про неверный пароль");

        System.out.println("Неверный пароль отклонён — тест пройден");
    }

    @Test
    public void buyBookTest() {
        String email = registerNewUser();
        System.out.println("Зарегистрировались, сайт выдал email: " + email);

        login(email);
        System.out.println("Авторизация пройдена");

        driver.get(CREDIT_CARD_URL);
        driver.findElement(By.cssSelector("[value='Generate Credit Card']")).click();

        String cardPage = driver.findElement(By.tagName("body")).getText();
        Matcher matcher = Pattern.compile("\\d{16}").matcher(cardPage);
        Assert.assertTrue(matcher.find(), "Номер тестовой карты не найден на странице");
        String cardNumber = matcher.group();
        System.out.println("Сгенерировали тестовую карту: " + cardNumber);

        driver.get(MAIN_URL);
        if (!driver.findElements(By.name("email")).isEmpty()) {
            driver.findElement(By.name("email")).sendKeys(email);
            driver.findElement(By.name("password")).sendKeys("1111");
            driver.findElement(By.cssSelector("[value='Login']")).click();
        }

        driver.get(ADD_TO_CART_URL);
        driver.get(SHOPPING_CART_URL);

        Assert.assertTrue(driver.getPageSource().contains("War and Peace"), "Книга должна быть в корзине");
        System.out.println("Книга в корзине");

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[value='Proceed to Checkout']")));
        driver.findElement(By.cssSelector("[value='Proceed to Checkout']")).click();
        System.out.println("Перешли к оформлению заказа");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("card_number")));
        driver.findElement(By.name("card_number")).sendKeys(cardNumber);
        driver.findElement(By.cssSelector("[value='Make Payment']")).click();

        wait.until(ExpectedConditions.urlContains("card_number"));
        Assert.assertTrue(driver.getPageSource().contains("Thank you for your order"), "Заказ должен оформиться");

        System.out.println("Оплата прошла, заказ оформлен — тест пройден");
    }

    private String registerNewUser() {
        driver.get(REGISTER_URL);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("zip_code")));
        driver.findElement(By.name("zip_code")).sendKeys("10001");
        driver.findElement(By.cssSelector("[value='Continue']")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("first_name")));
        driver.findElement(By.name("first_name")).sendKeys("Alex");
        driver.findElement(By.name("last_name")).sendKeys("Tester");
        driver.findElement(By.name("email")).sendKeys("test" + System.currentTimeMillis() + "@mail.com");
        driver.findElement(By.name("password1")).sendKeys("1111");
        driver.findElement(By.name("password2")).sendKeys("1111");
        driver.findElement(By.cssSelector("[value='Register']")).click();

        String pageText = driver.findElement(By.tagName("body")).getText();
        Matcher matcher = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.sharelane\\.com").matcher(pageText);
        Assert.assertTrue(matcher.find(), "Email с доменом .sharelane.com не найден на странице");
        return matcher.group();
    }

    private void login(String email) {
        driver.get(MAIN_URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email")));
        driver.findElement(By.name("email")).sendKeys(email);
        driver.findElement(By.name("password")).sendKeys("1111");
        driver.findElement(By.cssSelector("[value='Login']")).click();
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
