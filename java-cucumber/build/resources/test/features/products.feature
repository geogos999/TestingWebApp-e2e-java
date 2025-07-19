Feature: Product Search and Shopping
  As a customer
  I want to search for products and add them to my cart
  So that I can purchase items I need

  Background:
    Given I am on the e-commerce homepage

  @smoke @products
  Scenario: Search for a product successfully
    When I search for "laptop"
    Then I should see search results for "laptop"
    And the results should contain product information

  @products @cart
  Scenario: Add product to shopping cart
    Given I search for "phone"
    And I see search results for "phone"
    When I click on the first product
    And I add the product to cart
    Then the product should be added to cart
    And the cart count should increase

  @products
  Scenario Outline: Search for different product categories
    When I search for "<product>"
    Then I should see search results for "<product>"
    And the results should show "<expected_count>" or more products

    Examples:
      | product   | expected_count |
      | laptop    | 3              |
      | phone     | 5              |
      | headphones| 2              |
      | tablet    | 4              |
