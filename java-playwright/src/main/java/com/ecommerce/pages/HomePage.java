package com.ecommerce.pages;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

/**
 * Home page object containing all elements and actions for the home page.
 * This class implements the Page Object Model pattern for the home page functionality.
 */
public class HomePage extends BasePage {
    
    // Page URL
    private static final String HOME_URL = "/";
    
    // Header elements
    private static final String HEADER = "[data-testid='header']";
    private static final String LOGO = "[data-testid='logo']";
    private static final String SEARCH_INPUT = "[data-testid='search-input']";
    private static final String SEARCH_BUTTON = "[data-testid='search-button']";
    private static final String CART_ICON = "[data-testid='cart-icon']";
    private static final String CART_COUNT = "[data-testid='cart-count']";
    private static final String USER_MENU = "[data-testid='user-menu']";
    private static final String LOGIN_BUTTON = "[data-testid='header-login-button']";
    private static final String LOGOUT_BUTTON = "[data-testid='logout-button']";
    
    // Navigation menu
    private static final String NAV_MENU = "[data-testid='nav-menu']";
    private static final String PRODUCTS_LINK = "[data-testid='products-link']";
    private static final String CATEGORIES_DROPDOWN = "[data-testid='categories-dropdown']";
    
    // Hero section
    private static final String HERO_SECTION = "[data-testid='hero-section']";
    private static final String HERO_TITLE = "[data-testid='hero-title']";
    private static final String HERO_SUBTITLE = "[data-testid='hero-subtitle']";
    private static final String SHOP_NOW_BUTTON = "[data-testid='shop-now-button']";
    
    // Featured products section
    private static final String FEATURED_SECTION = "[data-testid='featured-section']";
    private static final String FEATURED_TITLE = "[data-testid='featured-title']";
    private static final String FEATURED_PRODUCTS = "[data-testid='featured-product']";
    private static final String PRODUCT_CARD = "[data-testid='product-card']";
    private static final String PRODUCT_NAME = "[data-testid='product-name']";
    private static final String PRODUCT_PRICE = "[data-testid='product-price']";
    private static final String ADD_TO_CART_BUTTON = "[data-testid='add-to-cart-button']";
    
    // Footer elements
    private static final String FOOTER = "[data-testid='footer']";
    private static final String FOOTER_LINKS = "[data-testid='footer-link']";
    
    public HomePage(Page page) {
        super(page);
    }
    
    // Navigation
    @Step("Navigate to home page")
    public HomePage navigate() {
        navigateTo(HOME_URL);
        waitForHomePageToLoad();
        return this;
    }
    
    @Step("Wait for home page to load")
    public HomePage waitForHomePageToLoad() {
        waitForElementVisible(HEADER);
        waitForElementVisible(HERO_SECTION);
        logger.info("Home page loaded successfully");
        return this;
    }
    
    // Header actions
    @Step("Click logo")
    public HomePage clickLogo() {
        clickElement(LOGO);
        return this;
    }
    
    @Step("Perform search: {searchTerm}")
    public ProductsPage performSearch(String searchTerm) {
        fillInput(SEARCH_INPUT, searchTerm);
        clickElement(SEARCH_BUTTON);
        logger.info("Performed search for: {}", searchTerm);
        return new ProductsPage(page);
    }
    
    @Step("Click cart icon")
    public CartPage clickCartIcon() {
        clickElement(CART_ICON);
        logger.info("Clicked cart icon");
        return new CartPage(page);
    }
    
    @Step("Click login button")
    public LoginPage clickLoginButton() {
        clickElement(LOGIN_BUTTON);
        logger.info("Clicked login button");
        return new LoginPage(page);
    }
    
    @Step("Click logout button")
    public HomePage clickLogoutButton() {
        clickElement(LOGOUT_BUTTON);
        logger.info("Clicked logout button");
        return this;
    }
    
    @Step("Click user menu")
    public HomePage clickUserMenu() {
        clickElement(USER_MENU);
        return this;
    }
    
    // Navigation menu actions
    @Step("Click products link")
    public ProductsPage clickProductsLink() {
        clickElement(PRODUCTS_LINK);
        logger.info("Clicked products link");
        return new ProductsPage(page);
    }
    
    @Step("Click shop now button")
    public ProductsPage clickShopNowButton() {
        clickElement(SHOP_NOW_BUTTON);
        logger.info("Clicked shop now button");
        return new ProductsPage(page);
    }
    
    // Product actions
    @Step("Add featured product to cart by index: {index}")
    public HomePage addFeaturedProductToCart(int index) {
        String productSelector = FEATURED_PRODUCTS + ":nth-child(" + (index + 1) + ") " + ADD_TO_CART_BUTTON;
        clickElement(productSelector);
        logger.info("Added featured product {} to cart", index);
        return this;
    }
    
    @Step("Click featured product by index: {index}")
    public ProductDetailsPage clickFeaturedProduct(int index) {
        String productSelector = FEATURED_PRODUCTS + ":nth-child(" + (index + 1) + ")";
        clickElement(productSelector);
        logger.info("Clicked featured product {}", index);
        return new ProductDetailsPage(page);
    }
    
    // Validation methods
    @Step("Check if user is logged in")
    public boolean isUserLoggedIn() {
        return isElementVisible(USER_MENU) && isElementVisible(LOGOUT_BUTTON);
    }
    
    @Step("Check if login button is visible")
    public boolean isLoginButtonVisible() {
        return isElementVisible(LOGIN_BUTTON);
    }
    
    @Step("Get cart count")
    public String getCartCount() {
        if (isElementVisible(CART_COUNT)) {
            return getText(CART_COUNT);
        }
        return "0";
    }
    
    @Step("Get cart count as integer")
    public int getCartCountAsInt() {
        try {
            return Integer.parseInt(getCartCount());
        } catch (NumberFormatException e) {
            logger.warn("Could not parse cart count, returning 0");
            return 0;
        }
    }
    
    @Step("Get hero title")
    public String getHeroTitle() {
        return getText(HERO_TITLE);
    }
    
    @Step("Get hero subtitle")
    public String getHeroSubtitle() {
        return getText(HERO_SUBTITLE);
    }
    
    @Step("Get featured section title")
    public String getFeaturedSectionTitle() {
        return getText(FEATURED_TITLE);
    }
    
    @Step("Get number of featured products")
    public int getFeaturedProductsCount() {
        return getElementCount(FEATURED_PRODUCTS);
    }
    
    @Step("Get featured product name by index: {index}")
    public String getFeaturedProductName(int index) {
        String productSelector = FEATURED_PRODUCTS + ":nth-child(" + (index + 1) + ") " + PRODUCT_NAME;
        return getText(productSelector);
    }
    
    @Step("Get featured product price by index: {index}")
    public String getFeaturedProductPrice(int index) {
        String productSelector = FEATURED_PRODUCTS + ":nth-child(" + (index + 1) + ") " + PRODUCT_PRICE;
        return getText(productSelector);
    }
    
    // Validation helpers
    @Step("Verify home page is displayed")
    public boolean isHomePageDisplayed() {
        return isElementVisible(HEADER) &&
               isElementVisible(HERO_SECTION) &&
               isElementVisible(FEATURED_SECTION) &&
               isElementVisible(FOOTER);
    }
    
    @Step("Verify hero section contains text: {expectedText}")
    public boolean heroSectionContainsText(String expectedText) {
        return elementContainsText(HERO_SECTION, expectedText);
    }
    
    @Step("Verify featured products are displayed")
    public boolean areFeaturedProductsDisplayed() {
        return isElementVisible(FEATURED_SECTION) && getFeaturedProductsCount() > 0;
    }
}
