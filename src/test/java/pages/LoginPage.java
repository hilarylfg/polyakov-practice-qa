package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.SauceDemoConfig;

/**
 * Страница авторизации Swag Labs (/).
 * Локаторы полей и кнопки — стабильные id-атрибуты (#user-name, #password, #login-button).
 */
public class LoginPage extends BasePage {

    private final By USERNAME_FIELD = By.id("user-name");
    private final By PASSWORD_FIELD = By.id("password");
    private final By LOGIN_BUTTON = By.id("login-button");
    private final By ERROR_MESSAGE = By.cssSelector("[data-test=error]");

    public LoginPage(WebDriver driver) {
        super(driver, SauceDemoConfig.EXPLICIT_TIMEOUT);
        openPage(SauceDemoConfig.BASE_URL);
        waitForVisibility(USERNAME_FIELD);
    }

    /** Выполняет вход с переданными учётными данными. При успехе для дальнейшей
     *  работы тест создаёт InventoryPage (его конструктор дожидается загрузки каталога). */
    @Step("Вход с логином {username}")
    public void login(String username, String password) {
        type(USERNAME_FIELD, username);
        type(PASSWORD_FIELD, password);
        click(LOGIN_BUTTON);
    }

    @Step("Чтение сообщения об ошибке авторизации")
    public String getErrorMessage() {
        return getText(ERROR_MESSAGE);
    }
}
