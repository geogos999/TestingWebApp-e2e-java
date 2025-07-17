package com.ecommerce.pages;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

/**
 * Products page object containing all elements and actions for the products listing page.
 * This class implements the Page Object Model pattern for the products functionality.
 */
public class ProductsPage extends BasePage {
    
    // Page URL
    private static final String PRODUCTS_URL = "/products";
    
    // Page elements
    private static final String PRODUCTS_CONTAINER = "[data-testid='products-container']";
    private static final String PAGE_TITLE = "[data-testid='page-title']";
    private static final String LOADING_SPINNER = "[data-testid='loading-spinner']";
    
    // Search and filters
    private static final String SEARCH_INPUT = "[data-testid='search-input']";
    private static final String SEARCH_BUTTON = "[data-testid='search-button']";
    private static final String CLEAR_SEARCH_BUTTON = "[data-testid='clear-search-button']";
    private static final String FILTERS_SECTION = "[data-testid='filters-section']";
    private static final String CATEGORY_FILTER = "[data-testid='category-filter']";
    private static final String PRICE_FILTER_MIN = "[data-testid='price-filter-min']";
    private static final String PRICE_FILTER_MAX = "[data-testid='price-filter-max']";
    private static final String APPLY_FILTERS_BUTTON = "[data-testid='apply-filters-button']";
    private static final String CLEAR_FILTERS_BUTTON = "[data-testid='clear-filters-button']";
    
    // Sorting
    private static final String SORT_DROPDOWN = "[data-testid='sort-dropdown']";
    
    // Product grid
    private static final String PRODUCTS_GRID = "[data-testid='products-grid']";
    private static final String PRODUCT_CARD = "[data-testid='product-card']";
    private static final String PRODUCT_IMAGE = "[data-testid='product-image']";
    private static final String PRODUCT_NAME = "[data-testid='product-name']";
    private static final String PRODUCT_PRICE = "[data-testid='product-price']";
    private static final String PRODUCT_RATING = "[data-testid='product-rating']";
    private static final String ADD_TO_CART_BUTTON = "[data-testid='add-to-cart-button']";
    private static final String VIEW_DETAILS_BUTTON = "[data-testid='view-details-button']";
    
    // Pagination
    private static final String PAGINATION = "[data-testid='pagination']";
    private static final String PREVIOUS_PAGE_BUTTON = "[data-testid='previous-page-button']";
    private static final String NEXT_PAGE_BUTTON = "[data-testid='next-page-button']";
    private static final String PAGE_INFO = "[data-testid='page-info']";
    
    // No results
    private static final String NO_RESULTS_MESSAGE = "[data-testid='no-results-message']";
    
    public ProductsPage(Page page) {
        super(page);
    }
    
    // Navigation
    @Step("Navigate to products page")
    public ProductsPage navigate() {
        navigateTo(PRODUCTS_URL);
        waitForProductsPageToLoad();
        return this;
    }
    
    @Step("Wait for products page to load")
    public ProductsPage waitForProductsPageToLoad() {
        waitForElementVisible(PRODUCTS_CONTAINER);
        // Wait for loading spinner to disappear if present
        if (isElementVisible(LOADING_SPINNER)) {
            waitForElementHidden(LOADING_SPINNER);
        }
        logger.info("Products page loaded successfully");
        return this;
    }
    
    // Search functionality
    @Step("Search for products: {searchTerm}")
    public ProductsPage searchProducts(String searchTerm) {
        fillInput(SEARCH_INPUT, searchTerm);
        clickElement(SEARCH_BUTTON);
        waitForProductsPageToLoad();
        logger.info("Searched for products: {}", searchTerm);
        return this;
    }
    
    @Step("Clear search")
    public ProductsPage clearSearch() {
        if (isElementVisible(CLEAR_SEARCH_BUTTON)) {
            clickElement(CLEAR_SEARCH_BUTTON);
            waitForProductsPageToLoad();
        }
        logger.info("Cleared search");
        return this;
    }
    
    // Filter functionality
    @Step("Select category filter: {category}")
    public ProductsPage selectCategoryFilter(String category) {
        selectOptionByText(CATEGORY_FILTER, category);
        logger.info("Selected category filter: {}", category);
        return this;
    }
    
    @Step("Set price filter range: {minPrice} - {maxPrice}")
    public ProductsPage setPriceFilter(String minPrice, String maxPrice) {
        if (!minPrice.isEmpty()) {
            fillInput(PRICE_FILTER_MIN, minPrice);
        }
        if (!maxPrice.isEmpty()) {
            fillInput(PRICE_FILTER_MAX, maxPrice);
        }
        logger.info("Set price filter range: {} - {}", minPrice, maxPrice);
        return this;
    }
    
    @Step("Apply filters")
    public ProductsPage applyFilters() {
        clickElement(APPLY_FILTERS_BUTTON);
        waitForProductsPageToLoad();
        logger.info("Applied filters");
        return this;
    }
    
    @Step("Clear all filters")
    public ProductsPage clearFilters() {
        clickElement(CLEAR_FILTERS_BUTTON);
        waitForProductsPageToLoad();
        logger.info("Cleared all filters");
        return this;
    }
    
    // Sorting functionality
    @Step("Sort products by: {sortOption}")
    public ProductsPage sortProductsBy(String sortOption) {
        selectOptionByText(SORT_DROPDOWN, sortOption);
        waitForProductsPageToLoad();
        logger.info("Sorted products by: {}", sortOption);
        return this;
    }
    
    // Product actions
    @Step("Add product to cart by index: {index}")
    public ProductsPage addProductToCart(int index) {
        String productSelector = PRODUCT_CARD + ":nth-child(" + (index + 1) + ") " + ADD_TO_CART_BUTTON;
        clickElement(productSelector);
        logger.info("Added product {} to cart", index);
        return this;
    }
    
    @Step("View product details by index: {index}")
    public ProductDetailsPage viewProductDetails(int index) {
        String productSelector = PRODUCT_CARD + ":nth-child(" + (index + 1) + ") " + VIEW_DETAILS_BUTTON;
        clickElement(productSelector);
        logger.info("Viewing details for product {}", index);
        return new ProductDetailsPage(page);
    }
    
    @Step("Click product by index: {index}")
    public ProductDetailsPage clickProduct(int index) {
        String productSelector = PRODUCT_CARD + ":nth-child(" + (index + 1) + ")";
        clickElement(productSelector);
        logger.info("Clicked product {}", index);
        return new ProductDetailsPage(page);
    }
    
    // Pagination actions
    @Step("Go to next page")
    public ProductsPage goToNextPage() {
        if (isElementEnabled(NEXT_PAGE_BUTTON)) {
            clickElement(NEXT_PAGE_BUTTON);
            waitForProductsPageToLoad();
            logger.info("Navigated to next page");
        }
        return this;
    }
    
    @Step("Go to previous page")
    public ProductsPage goToPreviousPage() {
        if (isElementEnabled(PREVIOUS_PAGE_BUTTON)) {
            clickElement(PREVIOUS_PAGE_BUTTON);
            waitForProductsPageToLoad();
            logger.info("Navigated to previous page");
        }
        return this;
    }
    
    // Validation methods
    @Step("Get page title")
    public String getPageTitle() {
        return getText(PAGE_TITLE);
    }
    
    @Step("Get number of products displayed")
    public int getProductCount() {
        return getElementCount(PRODUCT_CARD);
    }
    
    @Step("Get product name by index: {index}")
    public String getProductName(int index) {
        String productSelector = PRODUCT_CARD + ":nth-child(" + (index + 1) + ") " + PRODUCT_NAME;
        return getText(productSelector);
    }
    
    @Step("Get product price by index: {index}")
    public String getProductPrice(int index) {
        String productSelector = PRODUCT_CARD + ":nth-child(" + (index + 1) + ") " + PRODUCT_PRICE;
        return getText(productSelector);
    }
    
    @Step("Get product rating by index: {index}")
    public String getProductRating(int index) {
        String productSelector = PRODUCT_CARD + ":nth-child(" + (index + 1) + ") " + PRODUCT_RATING;
        return getText(productSelector);
    }
    
    @Step("Check if no results message is displayed")
    public boolean isNoResultsMessageDisplayed() {
        return isElementVisible(NO_RESULTS_MESSAGE);
    }
    
    @Step("Get no results message text")
    public String getNoResultsMessage() {
        return getText(NO_RESULTS_MESSAGE);
    }
    
    @Step("Check if pagination is visible")
    public boolean isPaginationVisible() {
        return isElementVisible(PAGINATION);
    }
    
    @Step("Check if next page button is enabled")
    public boolean isNextPageEnabled() {
        return isElementEnabled(NEXT_PAGE_BUTTON);
    }
    
    @Step("Check if previous page button is enabled")
    public boolean isPreviousPageEnabled() {
        return isElementEnabled(PREVIOUS_PAGE_BUTTON);
    }
    
    @Step("Get current page info")
    public String getPageInfo() {
        return getText(PAGE_INFO);
    }
    
    // Validation helpers
    @Step("Verify products page is displayed")
    public boolean isProductsPageDisplayed() {
        return isElementVisible(PRODUCTS_CONTAINER) &&
               isElementVisible(PRODUCTS_GRID);
    }
    
    @Step("Verify products are displayed")
    public boolean areProductsDisplayed() {
        return getProductCount() > 0;
    }
    
    @Step("Verify search results contain term: {searchTerm}")
    public boolean searchResultsContainTerm(String searchTerm) {
        int productCount = getProductCount();
        if (productCount == 0) {
            return false;
        }
        
        for (int i = 0; i < Math.min(productCount, 5); i++) { // Check first 5 products
            String productName = getProductName(i).toLowerCase();
            if (productName.contains(searchTerm.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
    
    @Step("Verify products are sorted by price ascending")
    public boolean areProductsSortedByPriceAscending() {
        int productCount = getProductCount();
        if (productCount < 2) {
            return true; // Cannot verify sorting with less than 2 products
        }
        
        for (int i = 0; i < productCount - 1; i++) {
            double currentPrice = extractPriceValue(getProductPrice(i));
            double nextPrice = extractPriceValue(getProductPrice(i + 1));
            if (currentPrice > nextPrice) {
                return false;
            }
        }
        return true;
    }
    
    private double extractPriceValue(String priceText) {
        try {
            // Remove currency symbols and extract numeric value
            String numericPrice = priceText.replaceAll("[^0-9.]", "");
            return Double.parseDouble(numericPrice);
        } catch (NumberFormatException e) {
            logger.warn("Could not parse price: {}", priceText);
            return 0.0;
        }
    }
}
