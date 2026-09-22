package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CheckboxesPage;

public class CheckboxesTests extends BaseTest {

    @Test(description = "Проверка начального состояния чекбоксов")
    public void testInitialCheckboxState() {
        CheckboxesPage page = new CheckboxesPage(driver);

        Assert.assertFalse(page.isCheckboxChecked(1), "Первый чекбокс должен быть unchecked по умолчанию");

        Assert.assertTrue(page.isCheckboxChecked(2), "Второй чекбокс должен быть checked по умолчанию");
    }

    @Test(description = "Проверка переключения чекбоксов")
    public void testToggleCheckbox() {
        CheckboxesPage page = new CheckboxesPage(driver);

        page.clickCheckbox(1);
        Assert.assertTrue(page.isCheckboxChecked(1), "Первый чекбокс должен быть checked после клика");

        page.clickCheckbox(2);
        Assert.assertFalse(page.isCheckboxChecked(2), "Второй чекбокс должен быть unchecked после клика");
    }
}