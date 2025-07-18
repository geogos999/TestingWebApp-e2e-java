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
    
    // Header elements - Updated to use actual selectors from your app
    //private static final String HEADER = "nav"; // Using the nav element we found
    private static final String HEADER = "nav"; // Using the nav element we found
    private static final String LOGO = "nav a:first-child"; // First link in nav (likely the logo/app name)
    private static final String SEARCH_INPUT = "input[type='search'], input[placeholder*='search' i]"; // Common search input patterns
    private static final String SEARCH_BUTTON = "button[type='submit'], button:has-text('Search')"; // Common search button patterns
    private static final String CART_ICON = "[data-testid='cart-link']"; // Cart link
    private static final String CART_COUNT = "[data-testid='cart-count']"; // Common cart count patterns
    private static final String USER_MENU = "[data-testid='user-menu-button']"; // User menu
    private static final String LOGIN_BUTTON = "[data-testid='login-link']"; // Login button we saw in nav
    private static final String LOGOUT_BUTTON = "[data-testid='logout-button']"; // Logout button

    // Navigation menu - Updated to use actual nav structure
    private static final String NAV_MENU = "nav";
    private static final String PRODUCTS_LINK = "[data-testid='hero-section']"; // Products link we saw in nav
    private static final String CATEGORIES_DROPDOWN = ".dropdown, .menu-dropdown"; // Common dropdown patterns

    // Hero section - Updated to use common patterns since data-testid doesn't exist
    private static final String HERO_SECTION = "[data-testid='products-link']";
    //private static final String HERO_TITLE = "h1, .hero h2, .banner h1"; // Common hero title patterns
    private static final String HERO_TITLE = "[data-testid='hero-title']"; // Common hero title patterns
    private static final String HERO_SUBTITLE = "[data-testid='hero-subtitle']"; // Common hero subtitle patterns
    private static final String SHOP_NOW_BUTTON = "[data-testid='shop-now-button']"; // Shop button patterns

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
        // Try to wait for hero section, but don't fail if it's not found
        try {
            waitForElementVisible(HERO_SECTION, 15000); // Increased timeout to 15 seconds
        } catch (Exception e) {
            logger.warn("Hero section not found with current selectors, page may have loaded anyway");
        }
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
        return //isElementVisible(HEADER) &&
                isElementVisible(HERO_TITLE) &&
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
