package tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CheckoutCompletePage;
import pages.CheckoutOverviewPage;
import pages.CheckoutPage;
import pages.InventoryPage;

import java.util.List;

/** Сценарии оформления заказа Swag Labs: полный цикл и валидация обязательных полей. */
@Epic("Swag Labs (saucedemo.com)")
@Feature("Оформление заказа")
@Owner("Иван Полеяков")
public class CheckoutTests extends SauceDemoBaseTest {

    private static final String FIRST_NAME = "Ivan";
    private static final String LAST_NAME = "Polyakov";
    private static final String POSTAL_CODE = "12345";

    private static final String BACKPACK_NAME = "Sauce Labs Backpack";
    private static final double BACKPACK_PRICE = 29.99;

    @Test(description = "Полный цикл оформления заказа: данные покупателя, сверка суммы, подтверждение")
    @Story("Успешное оформление заказа")
    @Severity(SeverityLevel.BLOCKER)
    public void testCompleteCheckoutFlow() {
        InventoryPage inventoryPage = loginAsStandardUser();
        inventoryPage.addItemToCart(InventoryPage.ITEM_BACKPACK);

        CheckoutPage checkoutPage = inventoryPage.openCart().startCheckout();
        checkoutPage.fillCustomerInfo(FIRST_NAME, LAST_NAME, POSTAL_CODE);
        CheckoutOverviewPage overviewPage = checkoutPage.continueToOverview();

        Assert.assertEquals(overviewPage.getItemNames(), List.of(BACKPACK_NAME),
                "На шаге Overview ожидался товар «Sauce Labs Backpack»");
        Assert.assertEquals(overviewPage.getSubtotalAmount(), BACKPACK_PRICE,
                "Ожидалась сумма товаров 29.99 на шаге Overview");

        CheckoutCompletePage completePage = overviewPage.finishOrder();
        Assert.assertEquals(completePage.getCompleteHeaderText(), "Thank you for your order!",
                "Ожидалось подтверждение оформления заказа");
    }

    @Test(description = "Валидация: пустое обязательное поле First Name не позволяет продолжить")
    @Story("Валидация обязательных полей")
    @Severity(SeverityLevel.CRITICAL)
    public void testCheckoutValidationEmptyFirstName() {
        InventoryPage inventoryPage = loginAsStandardUser();
        inventoryPage.addItemToCart(InventoryPage.ITEM_BACKPACK);

        CheckoutPage checkoutPage = inventoryPage.openCart().startCheckout();
        checkoutPage.fillCustomerInfo("", LAST_NAME, POSTAL_CODE);
        checkoutPage.clickContinue();

        Assert.assertEquals(checkoutPage.getErrorMessage(), "Error: First Name is required",
                "Ожидалась ошибка обязательного поля First Name");
    }
}
