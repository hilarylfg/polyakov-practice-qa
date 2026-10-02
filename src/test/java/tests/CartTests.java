package tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.InventoryPage;

import java.util.List;

/** Сценарии работы с корзиной Swag Labs: добавление, удаление, состав корзины. */
@Epic("Swag Labs (saucedemo.com)")
@Feature("Корзина")
@Owner("Иван Полеяков")
public class CartTests extends SauceDemoBaseTest {

    private static final String BACKPACK_NAME = "Sauce Labs Backpack";
    private static final double BACKPACK_PRICE = 29.99;
    private static final String BIKE_LIGHT_NAME = "Sauce Labs Bike Light";

    @Test(description = "Добавление товара в корзину: счётчик корзины, наименование и цена в корзине")
    @Story("Добавление товара")
    @Severity(SeverityLevel.BLOCKER)
    public void testAddItemToCart() {
        InventoryPage inventoryPage = loginAsStandardUser();

        inventoryPage.addItemToCart(InventoryPage.ITEM_BACKPACK);
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 1,
                "После добавления одного товара счётчик корзины должен равняться 1");

        CartPage cartPage = inventoryPage.openCart();
        Assert.assertEquals(cartPage.getItemNames(), List.of(BACKPACK_NAME),
                "Ожидался товар «Sauce Labs Backpack» в корзине");
        Assert.assertEquals(cartPage.getItemPrices(), List.of(BACKPACK_PRICE),
                "Ожидалась цена 29.99 у товара в корзине");
    }

    @Test(description = "Добавление нескольких товаров и удаление одного из корзины")
    @Story("Удаление товара")
    @Severity(SeverityLevel.CRITICAL)
    public void testAddMultipleItemsAndRemoveOne() {
        InventoryPage inventoryPage = loginAsStandardUser();

        inventoryPage.addItemToCart(InventoryPage.ITEM_BACKPACK);
        inventoryPage.addItemToCart(InventoryPage.ITEM_BIKE_LIGHT);
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 2,
                "После добавления двух товаров счётчик корзины должен равняться 2");

        inventoryPage.removeItemFromCart(InventoryPage.ITEM_BIKE_LIGHT);
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 1,
                "После удаления одного товара счётчик корзины должен равняться 1");

        CartPage cartPage = inventoryPage.openCart();
        Assert.assertEquals(cartPage.getItemNames(), List.of(BACKPACK_NAME),
                "В корзине должен остаться только «Sauce Labs Backpack»");
    }
}
