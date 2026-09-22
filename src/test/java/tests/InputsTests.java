package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.InputsPage;

public class InputsTests extends BaseTest {

    @Test(description = "Проверка ввода числовых значений")
    public void testNumericInput() {
        InputsPage page = new InputsPage(driver);

        page.inputValue("12345");
        String value = page.getInputValue();
        Assert.assertEquals(value, "12345", "В поле должно быть значение 12345");
    }

    @Test(description = "Проверка ввода нечисловых значений (негативный тест)")
    public void testNonNumericInput() {
        InputsPage page = new InputsPage(driver);

        page.inputValue("abc");

        String value = page.getInputValue();
        Assert.assertNotEquals(value, "abc", "Буквы не должны вводиться в поле");
    }

    @Test(description = "Проверка работы стрелок вверх и вниз")
    public void testArrowKeys() {
        InputsPage page = new InputsPage(driver);

        page.inputValue("10");

        page.pressArrowUp();
        String valueAfterUp = page.getInputValue();
        Assert.assertEquals(valueAfterUp, "11", "Значение должно увеличиться на 1 после стрелки вверх");

        page.pressArrowDown();
        String valueAfterDown = page.getInputValue();
        Assert.assertEquals(valueAfterDown, "10", "Значение должно уменьшиться на 1 после стрелки вниз");
    }
}