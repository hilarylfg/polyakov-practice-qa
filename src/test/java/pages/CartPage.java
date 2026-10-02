package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.SauceDemoConfig;

import java.util.List;
import java.util.stream.Collectors;

/** Корзина Swag Labs (/cart.html). */
public class CartPage extends BasePage {

    private final By CART_LIST = By.cssSelector("[data-test=cart-list]");
    private final By CART_ITEM_NAME = By.cssSelector("[data-test=inventory-item-name]");
    private final By CART_ITEM_PRICE = By.cssSelector("[data-test=inventory-item-price]");
    private final By CHECKOUT_BUTTON = By.id("checkout");

    public CartPage(WebDriver driver) {
        super(driver, SauceDemoConfig.EXPLICIT_TIMEOUT);
        waitForVisibility(CART_LIST);
    }

    public List<String> getItemNames() {
        return getAllTexts(CART_ITEM_NAME);
    }

    public List<Double> getItemPrices() {
        return getAllTexts(CART_ITEM_PRICE).stream()
                .map(price -> Double.parseDouble(price.replace("$", "")))
                .collect(Collectors.toList());
    }

    @Step("Перейти к оформлению заказа")
    public CheckoutPage startCheckout() {
        clickUntilEffect(CHECKOUT_BUTTON, d -> d.getCurrentUrl().contains("checkout-step-one.html"));
        return new CheckoutPage(driver);
    }
}
