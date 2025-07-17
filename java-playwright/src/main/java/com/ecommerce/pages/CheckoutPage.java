package com.ecommerce.pages;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

/**
 * Checkout page object containing all elements and actions for the checkout process.
 * This class implements the Page Object Model pattern for the checkout functionality.
 */
public class CheckoutPage extends BasePage {
    
    // Page URL
    private static final String CHECKOUT_URL = "/checkout";
    
    // Page elements
    private static final String CHECKOUT_CONTAINER = "[data-testid='checkout-container']";
    private static final String PAGE_TITLE = "[data-testid='page-title']";
    private static final String CHECKOUT_STEPS = "[data-testid='checkout-steps']";
    
    // Shipping information
    private static final String SHIPPING_SECTION = "[data-testid='shipping-section']";
    private static final String FIRST_NAME_INPUT = "[data-testid='first-name-input']";
    private static final String LAST_NAME_INPUT = "[data-testid='last-name-input']";
    private static final String EMAIL_INPUT = "[data-testid='email-input']";
    private static final String PHONE_INPUT = "[data-testid='phone-input']";
    private static final String ADDRESS_INPUT = "[data-testid='address-input']";
    private static final String CITY_INPUT = "[data-testid='city-input']";
    private static final String STATE_SELECT = "[data-testid='state-select']";
    private static final String ZIP_INPUT = "[data-testid='zip-input']";
    private static final String COUNTRY_SELECT = "[data-testid='country-select']";
    
    // Payment information
    private static final String PAYMENT_SECTION = "[data-testid='payment-section']";
    private static final String CARD_NUMBER_INPUT = "[data-testid='card-number-input']";
    private static final String CARD_EXPIRY_INPUT = "[data-testid='card-expiry-input']";
    private static final String CARD_CVC_INPUT = "[data-testid='card-cvc-input']";
    private static final String CARD_NAME_INPUT = "[data-testid='card-name-input']";
    private static final String PAYMENT_METHOD_SELECT = "[data-testid='payment-method-select']";
    
    // Billing address
    private static final String BILLING_SECTION = "[data-testid='billing-section']";
    private static final String SAME_AS_SHIPPING_CHECKBOX = "[data-testid='same-as-shipping-checkbox']";
    private static final String BILLING_ADDRESS_INPUT = "[data-testid='billing-address-input']";
    private static final String BILLING_CITY_INPUT = "[data-testid='billing-city-input']";
    private static final String BILLING_STATE_SELECT = "[data-testid='billing-state-select']";
    private static final String BILLING_ZIP_INPUT = "[data-testid='billing-zip-input']";
    
    // Order summary
    private static final String ORDER_SUMMARY = "[data-testid='order-summary']";
    private static final String ORDER_ITEMS = "[data-testid='order-items']";
    private static final String ORDER_ITEM = "[data-testid='order-item']";
    private static final String ITEM_NAME = "[data-testid='item-name']";
    private static final String ITEM_QUANTITY = "[data-testid='item-quantity']";
    private static final String ITEM_PRICE = "[data-testid='item-price']";
    private static final String SUBTOTAL = "[data-testid='subtotal']";
    private static final String TAX = "[data-testid='tax']";
    private static final String SHIPPING_COST = "[data-testid='shipping-cost']";
    private static final String TOTAL = "[data-testid='total']";
    
    // Promo/Coupon
    private static final String PROMO_CODE_INPUT = "[data-testid='promo-code-input']";
    private static final String APPLY_PROMO_BUTTON = "[data-testid='apply-promo-button']";
    private static final String PROMO_SUCCESS_MESSAGE = "[data-testid='promo-success-message']";
    private static final String PROMO_ERROR_MESSAGE = "[data-testid='promo-error-message']";
    
    // Action buttons
    private static final String CONTINUE_TO_PAYMENT_BUTTON = "[data-testid='continue-to-payment-button']";
    private static final String BACK_TO_CART_BUTTON = "[data-testid='back-to-cart-button']";
    private static final String PLACE_ORDER_BUTTON = "[data-testid='place-order-button']";
    
    // Terms and conditions
    private static final String TERMS_CHECKBOX = "[data-testid='terms-checkbox']";
    private static final String TERMS_LINK = "[data-testid='terms-link']";
    
    // Error messages
    private static final String ERROR_MESSAGE = "[data-testid='error-message']";
    private static final String FIELD_ERROR = "[data-testid='field-error']";
    
    public CheckoutPage(Page page) {
        super(page);
    }
    
    // Navigation
    @Step("Navigate to checkout page")
    public CheckoutPage navigate() {
        navigateTo(CHECKOUT_URL);
        waitForCheckoutPageToLoad();
        return this;
    }
    
    @Step("Wait for checkout page to load")
    public CheckoutPage waitForCheckoutPageToLoad() {
        waitForElementVisible(CHECKOUT_CONTAINER);
        waitForElementVisible(SHIPPING_SECTION);
        logger.info("Checkout page loaded successfully");
        return this;
    }
    
    // Shipping information
    @Step("Fill shipping information")
    public CheckoutPage fillShippingInformation(String firstName, String lastName, String email, 
                                               String phone, String address, String city, 
                                               String state, String zip, String country) {
        fillInput(FIRST_NAME_INPUT, firstName);
        fillInput(LAST_NAME_INPUT, lastName);
        fillInput(EMAIL_INPUT, email);
        fillInput(PHONE_INPUT, phone);
        fillInput(ADDRESS_INPUT, address);
        fillInput(CITY_INPUT, city);
        selectOptionByText(STATE_SELECT, state);
        fillInput(ZIP_INPUT, zip);
        selectOptionByText(COUNTRY_SELECT, country);
        
        logger.info("Filled shipping information for: {} {}", firstName, lastName);
        return this;
    }
    
    @Step("Fill first name: {firstName}")
    public CheckoutPage fillFirstName(String firstName) {
        fillInput(FIRST_NAME_INPUT, firstName);
        return this;
    }
    
    @Step("Fill last name: {lastName}")
    public CheckoutPage fillLastName(String lastName) {
        fillInput(LAST_NAME_INPUT, lastName);
        return this;
    }
    
    @Step("Fill email: {email}")
    public CheckoutPage fillEmail(String email) {
        fillInput(EMAIL_INPUT, email);
        return this;
    }
    
    @Step("Fill phone: {phone}")
    public CheckoutPage fillPhone(String phone) {
        fillInput(PHONE_INPUT, phone);
        return this;
    }
    
    @Step("Fill address: {address}")
    public CheckoutPage fillAddress(String address) {
        fillInput(ADDRESS_INPUT, address);
        return this;
    }
    
    @Step("Fill city: {city}")
    public CheckoutPage fillCity(String city) {
        fillInput(CITY_INPUT, city);
        return this;
    }
    
    @Step("Select state: {state}")
    public CheckoutPage selectState(String state) {
        selectOptionByText(STATE_SELECT, state);
        return this;
    }
    
    @Step("Fill zip code: {zip}")
    public CheckoutPage fillZipCode(String zip) {
        fillInput(ZIP_INPUT, zip);
        return this;
    }
    
    @Step("Select country: {country}")
    public CheckoutPage selectCountry(String country) {
        selectOptionByText(COUNTRY_SELECT, country);
        return this;
    }
    
    // Payment information
    @Step("Fill payment information")
    public CheckoutPage fillPaymentInformation(String cardNumber, String expiry, String cvc, String cardName) {
        fillInput(CARD_NUMBER_INPUT, cardNumber);
        fillInput(CARD_EXPIRY_INPUT, expiry);
        fillInput(CARD_CVC_INPUT, cvc);
        fillInput(CARD_NAME_INPUT, cardName);
        
        logger.info("Filled payment information for card ending in: {}", cardNumber.substring(Math.max(0, cardNumber.length() - 4)));
        return this;
    }
    
    @Step("Select payment method: {paymentMethod}")
    public CheckoutPage selectPaymentMethod(String paymentMethod) {
        selectOptionByText(PAYMENT_METHOD_SELECT, paymentMethod);
        return this;
    }
    
    // Billing address
    @Step("Check same as shipping address")
    public CheckoutPage checkSameAsShippingAddress() {
        if (!isElementChecked(SAME_AS_SHIPPING_CHECKBOX)) {
            clickElement(SAME_AS_SHIPPING_CHECKBOX);
            logger.info("Checked same as shipping address");
        }
        return this;
    }
    
    @Step("Uncheck same as shipping address")
    public CheckoutPage uncheckSameAsShippingAddress() {
        if (isElementChecked(SAME_AS_SHIPPING_CHECKBOX)) {
            clickElement(SAME_AS_SHIPPING_CHECKBOX);
            logger.info("Unchecked same as shipping address");
        }
        return this;
    }
    
    // Promo code
    @Step("Apply promo code: {promoCode}")
    public CheckoutPage applyPromoCode(String promoCode) {
        fillInput(PROMO_CODE_INPUT, promoCode);
        clickElement(APPLY_PROMO_BUTTON);
        logger.info("Applied promo code: {}", promoCode);
        return this;
    }
    
    // Terms and conditions
    @Step("Accept terms and conditions")
    public CheckoutPage acceptTermsAndConditions() {
        if (!isElementChecked(TERMS_CHECKBOX)) {
            clickElement(TERMS_CHECKBOX);
            logger.info("Accepted terms and conditions");
        }
        return this;
    }
    
    // Action buttons
    @Step("Continue to payment")
    public CheckoutPage continueToPayment() {
        clickElement(CONTINUE_TO_PAYMENT_BUTTON);
        logger.info("Continued to payment");
        return this;
    }
    
    @Step("Back to cart")
    public CartPage backToCart() {
        clickElement(BACK_TO_CART_BUTTON);
        logger.info("Navigated back to cart");
        return new CartPage(page);
    }
    
    @Step("Place order")
    public CheckoutPage placeOrder() {
        clickElement(PLACE_ORDER_BUTTON);
        logger.info("Placed order");
        return this;
    }
    
    // Validation methods
    @Step("Get order total")
    public String getOrderTotal() {
        return getText(TOTAL);
    }
    
    @Step("Get subtotal")
    public String getSubtotal() {
        return getText(SUBTOTAL);
    }
    
    @Step("Get tax amount")
    public String getTaxAmount() {
        return getText(TAX);
    }
    
    @Step("Get shipping cost")
    public String getShippingCost() {
        return getText(SHIPPING_COST);
    }
    
    @Step("Get number of order items")
    public int getOrderItemsCount() {
        return getElementCount(ORDER_ITEM);
    }
    
    @Step("Get order item name by index: {index}")
    public String getOrderItemName(int index) {
        String itemSelector = ORDER_ITEM + ":nth-child(" + (index + 1) + ") " + ITEM_NAME;
        return getText(itemSelector);
    }
    
    @Step("Get order item quantity by index: {index}")
    public String getOrderItemQuantity(int index) {
        String quantitySelector = ORDER_ITEM + ":nth-child(" + (index + 1) + ") " + ITEM_QUANTITY;
        return getText(quantitySelector);
    }
    
    @Step("Get order item price by index: {index}")
    public String getOrderItemPrice(int index) {
        String priceSelector = ORDER_ITEM + ":nth-child(" + (index + 1) + ") " + ITEM_PRICE;
        return getText(priceSelector);
    }
    
    @Step("Check if place order button is enabled")
    public boolean isPlaceOrderButtonEnabled() {
        return isElementEnabled(PLACE_ORDER_BUTTON);
    }
    
    @Step("Check if continue to payment button is enabled")
    public boolean isContinueToPaymentButtonEnabled() {
        return isElementEnabled(CONTINUE_TO_PAYMENT_BUTTON);
    }
    
    @Step("Check if terms and conditions are accepted")
    public boolean areTermsAccepted() {
        return isElementChecked(TERMS_CHECKBOX);
    }
    
    @Step("Check if promo success message is visible")
    public boolean isPromoSuccessMessageVisible() {
        return isElementVisible(PROMO_SUCCESS_MESSAGE);
    }
    
    @Step("Check if promo error message is visible")
    public boolean isPromoErrorMessageVisible() {
        return isElementVisible(PROMO_ERROR_MESSAGE);
    }
    
    @Step("Get error message")
    public String getErrorMessage() {
        return getText(ERROR_MESSAGE);
    }
    
    @Step("Check if checkout page is displayed")
    public boolean isCheckoutPageDisplayed() {
        return isElementVisible(CHECKOUT_CONTAINER) &&
               isElementVisible(SHIPPING_SECTION) &&
               isElementVisible(ORDER_SUMMARY);
    }
    
    // Validation helpers
    @Step("Verify order summary matches cart")
    public boolean doesOrderSummaryMatchExpected(double expectedSubtotal, double expectedTax, double expectedShipping) {
        try {
            double actualSubtotal = extractPriceValue(getSubtotal());
            double actualTax = extractPriceValue(getTaxAmount());
            double actualShipping = extractPriceValue(getShippingCost());
            double actualTotal = extractPriceValue(getOrderTotal());
            
            double expectedTotal = expectedSubtotal + expectedTax + expectedShipping;
            
            return Math.abs(actualSubtotal - expectedSubtotal) < 0.01 &&
                   Math.abs(actualTax - expectedTax) < 0.01 &&
                   Math.abs(actualShipping - expectedShipping) < 0.01 &&
                   Math.abs(actualTotal - expectedTotal) < 0.01;
        } catch (Exception e) {
            logger.warn("Error validating order summary: {}", e.getMessage());
            return false;
        }
    }
    
    private double extractPriceValue(String priceText) {
        try {
            String numericPrice = priceText.replaceAll("[^0-9.]", "");
            return Double.parseDouble(numericPrice);
        } catch (NumberFormatException e) {
            logger.warn("Could not parse price: {}", priceText);
            return 0.0;
        }
    }
}
