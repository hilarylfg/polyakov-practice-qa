package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public BasePage(WebDriver driver, Duration explicitWaitTimeout) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, explicitWaitTimeout);
    }

    protected void waitForVisibility(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void waitForClickability(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void waitForUrlContains(String urlFragment) {
        wait.until(ExpectedConditions.urlContains(urlFragment));
    }

    /**
     * Кликает по элементу и дожидается наблюдаемого эффекта. SPA-фреймворки (React)
     * могут «проглотить» клик, выполненный сразу после обновления состояния страницы
     * (обработчик события ещё не привязан к пересозданному узлу DOM), поэтому клик
     * повторяется до появления эффекта в пределах таймаута явных ожиданий.
     *
     * @param effect условие, подтверждающее, что клик сработал (изменение URL, счётчика и т.п.)
     */
    protected void clickUntilEffect(By locator, Predicate<WebDriver> effect) {
        for (int attempt = 1; attempt <= 3; attempt++) {
            click(locator);
            try {
                wait.until(d -> effect.test(d));
                return;
            } catch (TimeoutException e) {
                if (attempt == 3) {
                    throw new TimeoutException(
                            "Клик по " + locator + " не привёл к ожидаемому эффекту после " + attempt + " попыток", e);
                }
            }
        }
    }

    protected void waitForTextPresent(By locator, String text) {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    protected void click(By locator) {
        waitForClickability(locator);
        driver.findElement(locator).click();
    }

    protected void type(By locator, String text) {
        waitForVisibility(locator);
        driver.findElement(locator).sendKeys(text);
    }

    protected String getText(By locator) {
        waitForVisibility(locator);
        return driver.findElement(locator).getText();
    }

    protected List<String> getAllTexts(By locator) {
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        return driver.findElements(locator).stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    protected void openPage(String url) {
        driver.get(url);
    }
}
