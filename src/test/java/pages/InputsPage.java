package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

public class InputsPage extends BasePage {
    private final By INPUT_FIELD = By.tagName("input");

    public InputsPage(WebDriver driver) {
        super(driver);
        openPage("https://the-internet.herokuapp.com/inputs");
    }

    public void inputValue(String value) {
        type(INPUT_FIELD, value);
    }

    public void pressArrowUp() {
        driver.findElement(INPUT_FIELD).sendKeys(Keys.ARROW_UP);
    }

    public void pressArrowDown() {
        driver.findElement(INPUT_FIELD).sendKeys(Keys.ARROW_DOWN);
    }

    public String getInputValue() {
        waitForVisibility(INPUT_FIELD);
        return driver.findElement(INPUT_FIELD).getAttribute("value");
    }
}