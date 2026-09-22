package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DropdownPage;

public class DropdownTests extends BaseTest {

    @Test(description = "Проверка наличия опций в dropdown")
    public void testDropdownOptions() {
        DropdownPage page = new DropdownPage(driver);

        int optionsCount = page.getDropdownOptionsCount();
        Assert.assertEquals(optionsCount, 3, "В dropdown должно быть 3 опции");
    }

    @Test(description = "Проверка выбора опций в dropdown")
    public void testSelectOption() {
        DropdownPage page = new DropdownPage(driver);

        page.selectOptionByVisibleText("Option 1");
        String selectedText = page.getSelectedOptionText();
        Assert.assertEquals(selectedText, "Option 1", "Должна быть выбрана Option 1");

        page.selectOptionByVisibleText("Option 2");
        selectedText = page.getSelectedOptionText();
        Assert.assertEquals(selectedText, "Option 2", "Должна быть выбрана Option 2");
    }
}