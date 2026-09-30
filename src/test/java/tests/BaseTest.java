package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import utils.AllureAttachments;
import utils.AllureEnvironmentWriter;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Инициализация/закрытие драйвера для всех тестов.
 *
 * Кроссбраузерность: браузер и headless-режим задаются либо параметрами TestNG
 * (testng-crossbrowser.xml, @Parameters), либо системными свойствами Maven,
 * которые имеют более высокий приоритет: mvn clean test -Dbrowser=firefox -Dheadless=true.
 * По умолчанию — Chrome в обычном режиме.
 */
public abstract class BaseTest {
    protected WebDriver driver;

    @Parameters({"browser", "headless"})
    @BeforeMethod
    public void setUp(@Optional("chrome") String browserParam,
                      @Optional("false") String headlessParam) {
        // Системные свойства Maven приоритетнее параметров testng.xml
        String browser = System.getProperty("browser", browserParam).toLowerCase();
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", headlessParam));

        driver = createDriver(browser, headless);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        AllureEnvironmentWriter.registerBrowser(browser);
    }

    private WebDriver createDriver(String browser, boolean headless) {
        return switch (browser) {
            case "firefox" -> {
                FirefoxOptions fo = new FirefoxOptions();
                if (headless) fo.addArguments("-headless");
                WebDriverManager.firefoxdriver().setup();
                yield new FirefoxDriver(fo);
            }
            case "edge" -> {
                EdgeOptions eo = new EdgeOptions();
                if (headless) eo.addArguments("--headless=new");
                // Edge построен на Chromium: те же настройки профиля, что и для Chrome
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("credentials_enable_service", false);
                prefs.put("profile.password_manager_enabled", false);
                prefs.put("profile.password_manager_leak_detection", false);
                eo.setExperimentalOption("prefs", prefs);
                eo.addArguments("--disable-features=PasswordLeakDetection");
                WebDriverManager.edgedriver().setup();
                yield new EdgeDriver(eo);
            }
            default -> {
                ChromeOptions co = new ChromeOptions();
                if (headless) co.addArguments("--headless=new");
                // Chrome показывает модальную для страницы всплывашку «пароль скомпрометирован»,
                // если введённый пароль найден в базах утечек (demo-пароль Swag Labs — public).
                // Пока всплывашка открыта, все клики по странице проглатываются, поэтому
                // проверку утечек и менеджер паролей отключаем в настройках профиля.
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("credentials_enable_service", false);
                prefs.put("profile.password_manager_enabled", false);
                prefs.put("profile.password_manager_leak_detection", false);
                co.setExperimentalOption("prefs", prefs);
                co.addArguments("--disable-features=PasswordLeakDetection");
                WebDriverManager.chromedriver().setup();
                yield new ChromeDriver(co);
            }
        };
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (driver != null && !result.isSuccess()) {
            // Диагностические вложения Allure: состояние страницы в момент падения
            AllureAttachments.screenshot(driver, "Скриншот падения");
            AllureAttachments.pageSource(driver, "DOM-дамп на момент падения");
        }
        if (driver != null) {
            driver.quit();
        }
    }
}
