package com.ecommerce.tests;

import org.junit.jupiter.api.*;
import io.qameta.allure.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for home page functionality.
 * Tests home page display, navigation, featured products, search, etc.
 */
@Epic("User Interface")
@Feature("Home Page")
public class HomePageTests extends BaseTest {
    
    @BeforeEach
    void setupHomePageTests() {
        // Navigate to home page before each test
        navigateToHome();
    }
    
    @Test
    @DisplayName("Home page loads successfully")
    @Description("Verify that home page loads with all necessary elements")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Page Loading")
    void testHomePageLoads() {
        // Given: User navigates to home page
        // When: Page loads
        // Then: All main sections should be visible
        assertTrue(homePage.isHomePageDisplayed(), "Home page should be displayed with all sections");
        assertFalse(homePage.getHeroTitle().isEmpty(), "Hero title should not be empty");
        assertFalse(homePage.getHeroSubtitle().isEmpty(), "Hero subtitle should not be empty");
        assertTrue(homePage.areFeaturedProductsDisplayed(), "Featured products should be displayed");
    }
    
    @Test
    @DisplayName("Hero section content verification")
    @Description("Verify that hero section displays correct content")
    @Severity(SeverityLevel.NORMAL)
    @Story("Content Display")
    void testHeroSectionContent() {
        // Given: User is on home page
        assertTrue(homePage.isHomePageDisplayed(), "Home page should be displayed");
        
        // Then: Hero section should contain expected content
        String heroTitle = homePage.getHeroTitle();
        String heroSubtitle = homePage.getHeroSubtitle();
        
        assertNotNull(heroTitle, "Hero title should not be null");
        assertNotNull(heroSubtitle, "Hero subtitle should not be null");
        assertFalse(heroTitle.trim().isEmpty(), "Hero title should not be empty");
        assertFalse(heroSubtitle.trim().isEmpty(), "Hero subtitle should not be empty");
        
        // Verify hero section contains relevant e-commerce content
        assertTrue(homePage.heroSectionContainsText("shop") || 
                  homePage.heroSectionContainsText("store") ||
                  homePage.heroSectionContainsText("product"), 
                  "Hero section should contain e-commerce related content");
    }
    
    @Test
    @DisplayName("Featured products display")
    @Description("Verify that featured products are displayed correctly")
    @Severity(SeverityLevel.NORMAL)
    @Story("Product Display")
    void testFeaturedProductsDisplay() {
        // Given: User is on home page
        assertTrue(homePage.isHomePageDisplayed(), "Home page should be displayed");
        
        // Then: Featured products should be displayed
        assertTrue(homePage.areFeaturedProductsDisplayed(), "Featured products should be displayed");
        int productCount = homePage.getFeaturedProductsCount();
        assertTrue(productCount > 0, "There should be at least one featured product");
        
        // Verify first featured product has required information
        if (productCount > 0) {
            String productName = homePage.getFeaturedProductName(0);
            String productPrice = homePage.getFeaturedProductPrice(0);
            
            assertNotNull(productName, "Product name should not be null");
            assertNotNull(productPrice, "Product price should not be null");
            assertFalse(productName.trim().isEmpty(), "Product name should not be empty");
            assertFalse(productPrice.trim().isEmpty(), "Product price should not be empty");
        }
    }
    
    @Test
    @DisplayName("Navigation menu functionality")
    @Description("Verify that navigation menu works correctly")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Navigation")
    void testNavigationMenu() {
        // Given: User is on home page
        assertTrue(homePage.isHomePageDisplayed(), "Home page should be displayed");
        
        // When: User clicks products link
        ProductsPage productsPage = homePage.clickProductsLink();
        
        // Then: User should be navigated to products page
        assertTrue(productsPage.isProductsPageDisplayed(), "Products page should be displayed");
        assertTrue(getCurrentUrl().contains("products"), "URL should contain 'products'");
    }
    
    @Test
    @DisplayName("Search functionality from home page")
    @Description("Verify that search works from home page")
    @Severity(SeverityLevel.NORMAL)
    @Story("Search")
    void testSearchFunctionality() {
        // Given: User is on home page
        assertTrue(homePage.isHomePageDisplayed(), "Home page should be displayed");
        
        // When: User performs a search
        String searchTerm = "laptop";
        ProductsPage productsPage = homePage.performSearch(searchTerm);
        
        // Then: User should be taken to products page with search results
        assertTrue(productsPage.isProductsPageDisplayed(), "Products page should be displayed");
        assertTrue(getCurrentUrl().contains("products"), "URL should contain 'products'");
        // Note: Actual search results validation would depend on search implementation
    }
    
    @Test
    @DisplayName("Cart icon functionality")
    @Description("Verify that cart icon works correctly")
    @Severity(SeverityLevel.NORMAL)
    @Story("Cart")
    void testCartIconFunctionality() {
        // Given: User is on home page
        assertTrue(homePage.isHomePageDisplayed(), "Home page should be displayed");
        
        // When: User clicks cart icon
        CartPage cartPage = homePage.clickCartIcon();
        
        // Then: User should be navigated to cart page
        assertTrue(cartPage.isCartPageDisplayed(), "Cart page should be displayed");
        assertTrue(getCurrentUrl().contains("cart"), "URL should contain 'cart'");
    }
    
    @Test
    @DisplayName("Shop now button functionality")
    @Description("Verify that shop now button navigates to products page")
    @Severity(SeverityLevel.NORMAL)
    @Story("Navigation")
    void testShopNowButton() {
        // Given: User is on home page
        assertTrue(homePage.isHomePageDisplayed(), "Home page should be displayed");
        
        // When: User clicks shop now button
        ProductsPage productsPage = homePage.clickShopNowButton();
        
        // Then: User should be navigated to products page
        assertTrue(productsPage.isProductsPageDisplayed(), "Products page should be displayed");
        assertTrue(getCurrentUrl().contains("products"), "URL should contain 'products'");
    }
    
    @Test
    @DisplayName("Add featured product to cart")
    @Description("Verify that featured products can be added to cart")
    @Severity(SeverityLevel.NORMAL)
    @Story("Shopping")
    void testAddFeaturedProductToCart() {
        // Given: User is on home page with featured products
        assertTrue(homePage.isHomePageDisplayed(), "Home page should be displayed");
        assertTrue(homePage.areFeaturedProductsDisplayed(), "Featured products should be displayed");
        
        int initialCartCount = homePage.getCartCountAsInt();
        
        // When: User adds first featured product to cart
        if (homePage.getFeaturedProductsCount() > 0) {
            homePage.addFeaturedProductToCart(0);
            
            // Wait a moment for cart to update
            waitFor(1000);
            
            // Then: Cart count should increase
            int newCartCount = homePage.getCartCountAsInt();
            assertTrue(newCartCount > initialCartCount, 
                      "Cart count should increase after adding product");
        }
    }
    
    @Test
    @DisplayName("Featured product details navigation")
    @Description("Verify that clicking featured product navigates to product details")
    @Severity(SeverityLevel.NORMAL)
    @Story("Product Navigation")
    void testFeaturedProductDetailsNavigation() {
        // Given: User is on home page with featured products
        assertTrue(homePage.isHomePageDisplayed(), "Home page should be displayed");
        assertTrue(homePage.areFeaturedProductsDisplayed(), "Featured products should be displayed");
        
        // When: User clicks first featured product
        if (homePage.getFeaturedProductsCount() > 0) {
            homePage.clickFeaturedProduct(0);
            
            // Then: User should be navigated to product details page
            assertTrue(getCurrentUrl().contains("product"), "URL should contain 'product'");
        }
    }
    
    @Test
    @DisplayName("User authentication state display")
    @Description("Verify that home page displays correct authentication state")
    @Severity(SeverityLevel.NORMAL)
    @Story("Authentication State")
    void testAuthenticationStateDisplay() {
        // Given: User is on home page (not logged in)
        assertTrue(homePage.isHomePageDisplayed(), "Home page should be displayed");
        
        // Then: Login button should be visible
        assertTrue(homePage.isLoginButtonVisible(), "Login button should be visible when not logged in");
        assertFalse(homePage.isUserLoggedIn(), "User should not appear as logged in");
        
        // When: User logs in
        loginAsTestUser();
        navigateToHome();
        
        // Then: User menu should be visible instead of login button
        assertTrue(homePage.isUserLoggedIn(), "User should appear as logged in");
    }
    
    @Test
    @DisplayName("Logo functionality")
    @Description("Verify that clicking logo returns to home page")
    @Severity(SeverityLevel.MINOR)
    @Story("Navigation")
    void testLogoFunctionality() {
        // Given: User is on home page
        assertTrue(homePage.isHomePageDisplayed(), "Home page should be displayed");
        
        // When: User navigates away and clicks logo
        navigateToProducts();
        assertTrue(getCurrentUrl().contains("products"), "Should be on products page");
        
        homePage.clickLogo();
        
        // Then: User should return to home page
        assertTrue(isOnHomePage(), "Should return to home page after clicking logo");
    }
}
