package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckboxesPage extends BasePage {
    private final By CHECKBOX_1 = By.cssSelector("input[type='checkbox']:nth-of-type(1)");
    private final By CHECKBOX_2 = By.cssSelector("input[type='checkbox']:nth-of-type(2)");

    public CheckboxesPage(WebDriver driver) {
        super(driver);
        openPage("https://the-internet.herokuapp.com/checkboxes");
    }

    public boolean isCheckboxChecked(int checkboxNumber) {
        By checkboxLocator = (checkboxNumber == 1) ? CHECKBOX_1 : CHECKBOX_2;
        waitForVisibility(checkboxLocator);
        return driver.findElement(checkboxLocator).isSelected();
    }

    public void clickCheckbox(int checkboxNumber) {
        By checkboxLocator = (checkboxNumber == 1) ? CHECKBOX_1 : CHECKBOX_2;
        click(checkboxLocator);
    }
}