package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class AddRemoveElementsPage extends BasePage {
    private final By ADD_ELEMENT_BUTTON = By.xpath("//button[text()='Add Element']");
    private final By DELETE_BUTTON = By.xpath("//button[text()='Delete']");

    public AddRemoveElementsPage(WebDriver driver) {
        super(driver);
        openPage("https://the-internet.herokuapp.com/add_remove_elements/");
    }

    public void addElement() {
        click(ADD_ELEMENT_BUTTON);
        wait.until(ExpectedConditions.visibilityOfElementLocated(DELETE_BUTTON));
    }

    public void deleteElement() {
        click(DELETE_BUTTON);
    }

    public int getDeleteButtonsCount() {
        return driver.findElements(DELETE_BUTTON).size();
    }
}