package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class DropdownPage extends BasePage {
    private final By DROPDOWN = By.id("dropdown");

    public DropdownPage(WebDriver driver) {
        super(driver);
        openPage("https://the-internet.herokuapp.com/dropdown");
    }

    public void selectOptionByVisibleText(String optionText) {
        waitForVisibility(DROPDOWN);
        Select dropdown = new Select(driver.findElement(DROPDOWN));
        dropdown.selectByVisibleText(optionText);
    }

    public String getSelectedOptionText() {
        waitForVisibility(DROPDOWN);
        Select dropdown = new Select(driver.findElement(DROPDOWN));
        return dropdown.getFirstSelectedOption().getText();
    }

    public int getDropdownOptionsCount() {
        waitForVisibility(DROPDOWN);
        Select dropdown = new Select(driver.findElement(DROPDOWN));
        return dropdown.getOptions().size();
    }
}