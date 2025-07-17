package com.ecommerce.pages;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

/**
 * Cart page object containing all elements and actions for the shopping cart page.
 * This class implements the Page Object Model pattern for the cart functionality.
 */
public class CartPage extends BasePage {
    
    // Page URL
    private static final String CART_URL = "/cart";
    
    // Page elements
    private static final String CART_CONTAINER = "[data-testid='cart-container']";
    private static final String PAGE_TITLE = "[data-testid='page-title']";
    private static final String EMPTY_CART_MESSAGE = "[data-testid='empty-cart-message']";
    private static final String CONTINUE_SHOPPING_BUTTON = "[data-testid='continue-shopping-button']";
    
    // Cart items
    private static final String CART_ITEMS = "[data-testid='cart-items']";
    private static final String CART_ITEM = "[data-testid='cart-item']";
    private static final String ITEM_IMAGE = "[data-testid='item-image']";
    private static final String ITEM_NAME = "[data-testid='item-name']";
    private static final String ITEM_PRICE = "[data-testid='item-price']";
    private static final String ITEM_QUANTITY = "[data-testid='item-quantity']";
    private static final String QUANTITY_INPUT = "[data-testid='quantity-input']";
    private static final String INCREASE_QUANTITY_BUTTON = "[data-testid='increase-quantity-button']";
    private static final String DECREASE_QUANTITY_BUTTON = "[data-testid='decrease-quantity-button']";
    private static final String REMOVE_ITEM_BUTTON = "[data-testid='remove-item-button']";
    private static final String ITEM_SUBTOTAL = "[data-testid='item-subtotal']";
    
    // Cart summary
    private static final String CART_SUMMARY = "[data-testid='cart-summary']";
    private static final String SUBTOTAL = "[data-testid='subtotal']";
    private static final String TAX = "[data-testid='tax']";
    private static final String SHIPPING = "[data-testid='shipping']";
    private static final String TOTAL = "[data-testid='total']";
    private static final String ITEM_COUNT = "[data-testid='item-count']";
    
    // Action buttons
    private static final String CLEAR_CART_BUTTON = "[data-testid='clear-cart-button']";
    private static final String UPDATE_CART_BUTTON = "[data-testid='update-cart-button']";
    private static final String CHECKOUT_BUTTON = "[data-testid='checkout-button']";
    
    // Coupon/Promo code
    private static final String PROMO_CODE_INPUT = "[data-testid='promo-code-input']";
    private static final String APPLY_PROMO_BUTTON = "[data-testid='apply-promo-button']";
    private static final String PROMO_SUCCESS_MESSAGE = "[data-testid='promo-success-message']";
    private static final String PROMO_ERROR_MESSAGE = "[data-testid='promo-error-message']";
    
    public CartPage(Page page) {
        super(page);
    }
    
    // Navigation
    @Step("Navigate to cart page")
    public CartPage navigate() {
        navigateTo(CART_URL);
        waitForCartPageToLoad();
        return this;
    }
    
    @Step("Wait for cart page to load")
    public CartPage waitForCartPageToLoad() {
        waitForElementVisible(CART_CONTAINER);
        logger.info("Cart page loaded successfully");
        return this;
    }
    
    // Cart item actions
    @Step("Update item quantity by index: {index} to quantity: {quantity}")
    public CartPage updateItemQuantity(int index, int quantity) {
        String quantitySelector = CART_ITEM + ":nth-child(" + (index + 1) + ") " + QUANTITY_INPUT;
        clearInput(quantitySelector);
        fillInput(quantitySelector, String.valueOf(quantity));
        logger.info("Updated item {} quantity to {}", index, quantity);
        return this;
    }
    
    @Step("Increase item quantity by index: {index}")
    public CartPage increaseItemQuantity(int index) {
        String buttonSelector = CART_ITEM + ":nth-child(" + (index + 1) + ") " + INCREASE_QUANTITY_BUTTON;
        clickElement(buttonSelector);
        logger.info("Increased quantity for item {}", index);
        return this;
    }
    
    @Step("Decrease item quantity by index: {index}")
    public CartPage decreaseItemQuantity(int index) {
        String buttonSelector = CART_ITEM + ":nth-child(" + (index + 1) + ") " + DECREASE_QUANTITY_BUTTON;
        clickElement(buttonSelector);
        logger.info("Decreased quantity for item {}", index);
        return this;
    }
    
    @Step("Remove item by index: {index}")
    public CartPage removeItem(int index) {
        String removeSelector = CART_ITEM + ":nth-child(" + (index + 1) + ") " + REMOVE_ITEM_BUTTON;
        clickElement(removeSelector);
        logger.info("Removed item {}", index);
        return this;
    }
    
    @Step("Clear entire cart")
    public CartPage clearCart() {
        if (isElementVisible(CLEAR_CART_BUTTON)) {
            clickElement(CLEAR_CART_BUTTON);
            logger.info("Cleared entire cart");
        }
        return this;
    }
    
    @Step("Update cart")
    public CartPage updateCart() {
        if (isElementVisible(UPDATE_CART_BUTTON)) {
            clickElement(UPDATE_CART_BUTTON);
            waitForCartPageToLoad();
            logger.info("Updated cart");
        }
        return this;
    }
    
    // Navigation actions
    @Step("Continue shopping")
    public ProductsPage continueShopping() {
        clickElement(CONTINUE_SHOPPING_BUTTON);
        logger.info("Clicked continue shopping");
        return new ProductsPage(page);
    }
    
    @Step("Proceed to checkout")
    public CheckoutPage proceedToCheckout() {
        clickElement(CHECKOUT_BUTTON);
        logger.info("Proceeded to checkout");
        return new CheckoutPage(page);
    }
    
    // Promo code actions
    @Step("Apply promo code: {promoCode}")
    public CartPage applyPromoCode(String promoCode) {
        fillInput(PROMO_CODE_INPUT, promoCode);
        clickElement(APPLY_PROMO_BUTTON);
        logger.info("Applied promo code: {}", promoCode);
        return this;
    }
    
    // Validation methods
    @Step("Check if cart is empty")
    public boolean isCartEmpty() {
        return isElementVisible(EMPTY_CART_MESSAGE) || getItemCount() == 0;
    }
    
    @Step("Get number of items in cart")
    public int getItemCount() {
        return getElementCount(CART_ITEM);
    }
    
    @Step("Get item count from summary")
    public String getItemCountFromSummary() {
        return getText(ITEM_COUNT);
    }
    
    @Step("Get item name by index: {index}")
    public String getItemName(int index) {
        String nameSelector = CART_ITEM + ":nth-child(" + (index + 1) + ") " + ITEM_NAME;
        return getText(nameSelector);
    }
    
    @Step("Get item price by index: {index}")
    public String getItemPrice(int index) {
        String priceSelector = CART_ITEM + ":nth-child(" + (index + 1) + ") " + ITEM_PRICE;
        return getText(priceSelector);
    }
    
    @Step("Get item quantity by index: {index}")
    public String getItemQuantity(int index) {
        String quantitySelector = CART_ITEM + ":nth-child(" + (index + 1) + ") " + QUANTITY_INPUT;
        return getAttribute(quantitySelector, "value");
    }
    
    @Step("Get item subtotal by index: {index}")
    public String getItemSubtotal(int index) {
        String subtotalSelector = CART_ITEM + ":nth-child(" + (index + 1) + ") " + ITEM_SUBTOTAL;
        return getText(subtotalSelector);
    }
    
    @Step("Get cart subtotal")
    public String getSubtotal() {
        return getText(SUBTOTAL);
    }
    
    @Step("Get tax amount")
    public String getTax() {
        return getText(TAX);
    }
    
    @Step("Get shipping amount")
    public String getShipping() {
        return getText(SHIPPING);
    }
    
    @Step("Get total amount")
    public String getTotal() {
        return getText(TOTAL);
    }
    
    @Step("Get empty cart message")
    public String getEmptyCartMessage() {
        return getText(EMPTY_CART_MESSAGE);
    }
    
    @Step("Get promo success message")
    public String getPromoSuccessMessage() {
        return getText(PROMO_SUCCESS_MESSAGE);
    }
    
    @Step("Get promo error message")
    public String getPromoErrorMessage() {
        return getText(PROMO_ERROR_MESSAGE);
    }
    
    @Step("Check if checkout button is enabled")
    public boolean isCheckoutButtonEnabled() {
        return isElementEnabled(CHECKOUT_BUTTON);
    }
    
    @Step("Check if promo success message is visible")
    public boolean isPromoSuccessMessageVisible() {
        return isElementVisible(PROMO_SUCCESS_MESSAGE);
    }
    
    @Step("Check if promo error message is visible")
    public boolean isPromoErrorMessageVisible() {
        return isElementVisible(PROMO_ERROR_MESSAGE);
    }
    
    // Validation helpers
    @Step("Verify cart page is displayed")
    public boolean isCartPageDisplayed() {
        return isElementVisible(CART_CONTAINER);
    }
    
    @Step("Verify cart contains item: {itemName}")
    public boolean cartContainsItem(String itemName) {
        int itemCount = getItemCount();
        for (int i = 0; i < itemCount; i++) {
            if (getItemName(i).toLowerCase().contains(itemName.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
    
    @Step("Verify cart total is correct")
    public boolean isCartTotalCorrect() {
        try {
            double subtotal = extractPriceValue(getSubtotal());
            double tax = extractPriceValue(getTax());
            double shipping = extractPriceValue(getShipping());
            double total = extractPriceValue(getTotal());
            
            double expectedTotal = subtotal + tax + shipping;
            double difference = Math.abs(total - expectedTotal);
            
            // Allow for small rounding differences
            return difference < 0.01;
        } catch (Exception e) {
            logger.warn("Error validating cart total: {}", e.getMessage());
            return false;
        }
    }
    
    @Step("Calculate expected item subtotal for index: {index}")
    public boolean isItemSubtotalCorrect(int index) {
        try {
            double price = extractPriceValue(getItemPrice(index));
            int quantity = Integer.parseInt(getItemQuantity(index));
            double subtotal = extractPriceValue(getItemSubtotal(index));
            
            double expectedSubtotal = price * quantity;
            double difference = Math.abs(subtotal - expectedSubtotal);
            
            // Allow for small rounding differences
            return difference < 0.01;
        } catch (Exception e) {
            logger.warn("Error validating item subtotal for index {}: {}", index, e.getMessage());
            return false;
        }
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
