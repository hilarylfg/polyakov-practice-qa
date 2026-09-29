package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import utils.SauceDemoConfig;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Каталог товаров Swag Labs (/inventory.html).
 * Кнопки товаров имеют стабильные id вида add-to-cart-{itemId} / remove-{itemId}.
 */
public class InventoryPage extends BasePage {

    // Значения id товаров без префикса add-to-cart- / remove-
    public static final String ITEM_BACKPACK = "sauce-labs-backpack";
    public static final String ITEM_BIKE_LIGHT = "sauce-labs-bike-light";

    private final By PAGE_TITLE = By.cssSelector("[data-test=title]");
    private final By INVENTORY_LIST = By.cssSelector("[data-test=inventory-list]");
    private final By INVENTORY_ITEM_NAME = By.cssSelector("[data-test=inventory-item-name]");
    private final By INVENTORY_ITEM_PRICE = By.cssSelector("[data-test=inventory-item-price]");
    private final By CART_LINK = By.cssSelector("[data-test=shopping-cart-link]");
    private final By CART_BADGE = By.cssSelector("[data-test=shopping-cart-badge]");
    private final By SORT_CONTAINER = By.cssSelector("[data-test=product-sort-container]");

    public InventoryPage(WebDriver driver) {
        super(driver, SauceDemoConfig.EXPLICIT_TIMEOUT);
        waitForUrlContains("/inventory.html");
        waitForVisibility(INVENTORY_LIST);
    }

    public String getPageTitle() {
        return getText(PAGE_TITLE);
    }

    /** Добавляет товар в корзину и дожидается увеличения счётчика —
     *  React обновляет бейдж асинхронно после клика. */
    public void addItemToCart(String itemId) {
        int countBefore = readCartBadgeCount();
        clickUntilEffect(By.id("add-to-cart-" + itemId), d -> readCartBadgeCount() == countBefore + 1);
    }

    /** Удаляет товар из корзины и дожидается уменьшения счётчика. */
    public void removeItemFromCart(String itemId) {
        int countBefore = readCartBadgeCount();
        clickUntilEffect(By.id("remove-" + itemId), d -> readCartBadgeCount() == countBefore - 1);
    }

    /** Возвращает количество товаров в корзине; при пустой корзине счётчик отсутствует — 0. */
    public int getCartBadgeCount() {
        return readCartBadgeCount();
    }

    private int readCartBadgeCount() {
        List<WebElement> badges = driver.findElements(CART_BADGE);
        return badges.isEmpty() ? 0 : Integer.parseInt(badges.get(0).getText());
    }

    public List<String> getItemNames() {
        return getAllTexts(INVENTORY_ITEM_NAME);
    }

    public List<Double> getItemPrices() {
        return getAllTexts(INVENTORY_ITEM_PRICE).stream()
                .map(price -> Double.parseDouble(price.replace("$", "")))
                .collect(Collectors.toList());
    }

    public void sortBy(SortOption option) {
        waitForVisibility(SORT_CONTAINER);
        Select sortDropdown = new Select(driver.findElement(SORT_CONTAINER));
        sortDropdown.selectByValue(option.getValue());
    }

    public CartPage openCart() {
        clickUntilEffect(CART_LINK, d -> d.getCurrentUrl().contains("cart.html"));
        return new CartPage(driver);
    }
}
