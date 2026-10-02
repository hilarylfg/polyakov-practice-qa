package tests;

import pages.InventoryPage;
import pages.LoginPage;
import utils.SauceDemoConfig;

/** Общая инфраструктура saucedemo-тестов: типовой вход под standard_user. */
public abstract class SauceDemoBaseTest extends BaseTest {

    protected InventoryPage loginAsStandardUser() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(SauceDemoConfig.STANDARD_USER, SauceDemoConfig.PASSWORD);
        return new InventoryPage(driver);
    }
}
