package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.SauceDemoConfig;

/** Шаг 1 оформления заказа Swag Labs (/checkout-step-one.html) — данные покупателя. */
public class CheckoutPage extends BasePage {

    private final By FIRST_NAME_FIELD = By.id("first-name");
    private final By LAST_NAME_FIELD = By.id("last-name");
    private final By POSTAL_CODE_FIELD = By.id("postal-code");
    private final By CONTINUE_BUTTON = By.id("continue");
    private final By ERROR_MESSAGE = By.cssSelector("[data-test=error]");

    public CheckoutPage(WebDriver driver) {
        super(driver, SauceDemoConfig.EXPLICIT_TIMEOUT);
        waitForVisibility(FIRST_NAME_FIELD);
    }

    @Step("Заполнить данные покупателя ({firstName} {lastName}, индекс {postalCode})")
    public void fillCustomerInfo(String firstName, String lastName, String postalCode) {
        type(FIRST_NAME_FIELD, firstName);
        type(LAST_NAME_FIELD, lastName);
        type(POSTAL_CODE_FIELD, postalCode);
    }

    /** Нажимает Continue без ожидания перехода — используется для проверки валидации. */
    @Step("Нажать Continue (без ожидания навигации)")
    public void clickContinue() {
        click(CONTINUE_BUTTON);
    }

    /** Заполнять поля заранее: {@link #fillCustomerInfo}. Переходит на шаг Overview. */
    @Step("Подтвердить данные и перейти к обзору заказа")
    public CheckoutOverviewPage continueToOverview() {
        clickUntilEffect(CONTINUE_BUTTON, d -> d.getCurrentUrl().contains("checkout-step-two.html"));
        return new CheckoutOverviewPage(driver);
    }

    @Step("Чтение сообщения об ошибке валидации")
    public String getErrorMessage() {
        return getText(ERROR_MESSAGE);
    }
}
