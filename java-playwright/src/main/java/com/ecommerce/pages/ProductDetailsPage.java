package com.ecommerce.pages;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

/**
 * Product details page object containing all elements and actions for the product details page.
 * This class implements the Page Object Model pattern for the product details functionality.
 */
public class ProductDetailsPage extends BasePage {
    
    // Page elements
    private static final String PRODUCT_CONTAINER = "[data-testid='product-container']";
    private static final String PRODUCT_IMAGE = "[data-testid='product-image']";
    private static final String PRODUCT_NAME = "[data-testid='product-name']";
    private static final String PRODUCT_PRICE = "[data-testid='product-price']";
    private static final String PRODUCT_DESCRIPTION = "[data-testid='product-description']";
    private static final String PRODUCT_RATING = "[data-testid='product-rating']";
    private static final String PRODUCT_CATEGORY = "[data-testid='product-category']";
    private static final String PRODUCT_SKU = "[data-testid='product-sku']";
    private static final String PRODUCT_AVAILABILITY = "[data-testid='product-availability']";
    
    // Product actions
    private static final String QUANTITY_INPUT = "[data-testid='quantity-input']";
    private static final String INCREASE_QUANTITY_BUTTON = "[data-testid='increase-quantity-button']";
    private static final String DECREASE_QUANTITY_BUTTON = "[data-testid='decrease-quantity-button']";
    private static final String ADD_TO_CART_BUTTON = "[data-testid='add-to-cart-button']";
    private static final String BUY_NOW_BUTTON = "[data-testid='buy-now-button']";
    private static final String ADD_TO_WISHLIST_BUTTON = "[data-testid='add-to-wishlist-button']";
    
    // Product images
    private static final String IMAGE_GALLERY = "[data-testid='image-gallery']";
    private static final String MAIN_IMAGE = "[data-testid='main-image']";
    private static final String THUMBNAIL_IMAGES = "[data-testid='thumbnail-image']";
    
    // Related products
    private static final String RELATED_PRODUCTS_SECTION = "[data-testid='related-products-section']";
    private static final String RELATED_PRODUCT = "[data-testid='related-product']";
    
    // Reviews section
    private static final String REVIEWS_SECTION = "[data-testid='reviews-section']";
    private static final String REVIEW_ITEM = "[data-testid='review-item']";
    private static final String ADD_REVIEW_BUTTON = "[data-testid='add-review-button']";
    
    // Navigation
    private static final String BREADCRUMB = "[data-testid='breadcrumb']";
    private static final String BACK_BUTTON = "[data-testid='back-button']";
    
    public ProductDetailsPage(Page page) {
        super(page);
    }
    
    // Navigation
    @Step("Wait for product details page to load")
    public ProductDetailsPage waitForProductDetailsPageToLoad() {
        waitForElementVisible(PRODUCT_CONTAINER);
        waitForElementVisible(PRODUCT_NAME);
        waitForElementVisible(PRODUCT_PRICE);
        logger.info("Product details page loaded successfully");
        return this;
    }
    
    // Product information
    @Step("Get product name")
    public String getProductName() {
        return getText(PRODUCT_NAME);
    }
    
    @Step("Get product price")
    public String getProductPrice() {
        return getText(PRODUCT_PRICE);
    }
    
    @Step("Get product description")
    public String getProductDescription() {
        return getText(PRODUCT_DESCRIPTION);
    }
    
    @Step("Get product rating")
    public String getProductRating() {
        return getText(PRODUCT_RATING);
    }
    
    @Step("Get product category")
    public String getProductCategory() {
        return getText(PRODUCT_CATEGORY);
    }
    
    @Step("Get product SKU")
    public String getProductSKU() {
        return getText(PRODUCT_SKU);
    }
    
    @Step("Get product availability")
    public String getProductAvailability() {
        return getText(PRODUCT_AVAILABILITY);
    }
    
    // Quantity actions
    @Step("Set quantity: {quantity}")
    public ProductDetailsPage setQuantity(int quantity) {
        clearInput(QUANTITY_INPUT);
        fillInput(QUANTITY_INPUT, String.valueOf(quantity));
        logger.info("Set quantity to: {}", quantity);
        return this;
    }
    
    @Step("Get current quantity")
    public int getCurrentQuantity() {
        try {
            return Integer.parseInt(getAttribute(QUANTITY_INPUT, "value"));
        } catch (NumberFormatException e) {
            logger.warn("Could not parse quantity, returning 1");
            return 1;
        }
    }
    
    @Step("Increase quantity")
    public ProductDetailsPage increaseQuantity() {
        clickElement(INCREASE_QUANTITY_BUTTON);
        logger.info("Increased quantity");
        return this;
    }
    
    @Step("Decrease quantity")
    public ProductDetailsPage decreaseQuantity() {
        clickElement(DECREASE_QUANTITY_BUTTON);
        logger.info("Decreased quantity");
        return this;
    }
    
    // Product actions
    @Step("Add product to cart")
    public ProductDetailsPage addToCart() {
        clickElement(ADD_TO_CART_BUTTON);
        logger.info("Added product to cart");
        return this;
    }
    
    @Step("Buy now")
    public CheckoutPage buyNow() {
        clickElement(BUY_NOW_BUTTON);
        logger.info("Clicked buy now");
        return new CheckoutPage(page);
    }
    
    @Step("Add to wishlist")
    public ProductDetailsPage addToWishlist() {
        clickElement(ADD_TO_WISHLIST_BUTTON);
        logger.info("Added product to wishlist");
        return this;
    }
    
    // Image gallery
    @Step("Click thumbnail image by index: {index}")
    public ProductDetailsPage clickThumbnailImage(int index) {
        String thumbnailSelector = THUMBNAIL_IMAGES + ":nth-child(" + (index + 1) + ")";
        clickElement(thumbnailSelector);
        logger.info("Clicked thumbnail image: {}", index);
        return this;
    }
    
    @Step("Get number of thumbnail images")
    public int getThumbnailCount() {
        return getElementCount(THUMBNAIL_IMAGES);
    }
    
    // Related products
    @Step("Click related product by index: {index}")
    public ProductDetailsPage clickRelatedProduct(int index) {
        String relatedProductSelector = RELATED_PRODUCT + ":nth-child(" + (index + 1) + ")";
        clickElement(relatedProductSelector);
        logger.info("Clicked related product: {}", index);
        return this;
    }
    
    @Step("Get number of related products")
    public int getRelatedProductsCount() {
        return getElementCount(RELATED_PRODUCT);
    }
    
    // Reviews
    @Step("Click add review button")
    public ProductDetailsPage clickAddReview() {
        clickElement(ADD_REVIEW_BUTTON);
        logger.info("Clicked add review button");
        return this;
    }
    
    @Step("Get number of reviews")
    public int getReviewsCount() {
        return getElementCount(REVIEW_ITEM);
    }
    
    // Navigation
    @Step("Click back button")
    public void clickBackButton() {
        clickElement(BACK_BUTTON);
        logger.info("Clicked back button");
    }
    
    @Step("Navigate back to products")
    public ProductsPage navigateBackToProducts() {
        clickBackButton();
        return new ProductsPage(page);
    }
    
    // Validation methods
    @Step("Check if product details page is displayed")
    public boolean isProductDetailsPageDisplayed() {
        return isElementVisible(PRODUCT_CONTAINER) &&
               isElementVisible(PRODUCT_NAME) &&
               isElementVisible(PRODUCT_PRICE);
    }
    
    @Step("Check if add to cart button is enabled")
    public boolean isAddToCartButtonEnabled() {
        return isElementEnabled(ADD_TO_CART_BUTTON);
    }
    
    @Step("Check if buy now button is enabled")
    public boolean isBuyNowButtonEnabled() {
        return isElementEnabled(BUY_NOW_BUTTON);
    }
    
    @Step("Check if product is available")
    public boolean isProductAvailable() {
        String availability = getProductAvailability().toLowerCase();
        return availability.contains("available") || availability.contains("in stock");
    }
    
    @Step("Check if related products are displayed")
    public boolean areRelatedProductsDisplayed() {
        return isElementVisible(RELATED_PRODUCTS_SECTION) && getRelatedProductsCount() > 0;
    }
    
    @Step("Check if reviews section is displayed")
    public boolean isReviewsSectionDisplayed() {
        return isElementVisible(REVIEWS_SECTION);
    }
    
    // Validation helpers
    @Step("Verify product has required information")
    public boolean hasRequiredProductInformation() {
        return !getProductName().trim().isEmpty() &&
               !getProductPrice().trim().isEmpty() &&
               !getProductDescription().trim().isEmpty();
    }
}
