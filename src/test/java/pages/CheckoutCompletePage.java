package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.SauceDemoConfig;

/** Финальная страница оформления заказа Swag Labs (/checkout-complete.html). */
public class CheckoutCompletePage extends BasePage {

    private final By COMPLETE_HEADER = By.cssSelector("[data-test=complete-header]");
    private final By COMPLETE_TEXT = By.cssSelector("[data-test=complete-text]");

    public CheckoutCompletePage(WebDriver driver) {
        super(driver, SauceDemoConfig.EXPLICIT_TIMEOUT);
        waitForVisibility(COMPLETE_HEADER);
    }

    public String getCompleteHeaderText() {
        return getText(COMPLETE_HEADER);
    }

    public String getCompleteText() {
        return getText(COMPLETE_TEXT);
    }
}
