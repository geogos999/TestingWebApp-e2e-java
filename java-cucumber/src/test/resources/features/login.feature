Feature: User Login
  As a customer
  I want to log into the e-commerce application
  So that I can access my account and make purchases

  Background:
    Given I am on the e-commerce homepage

  @smoke @login
  Scenario: Successful login with valid credentials
    When I navigate to the login page
    And I enter valid email "user@test.com"
    And I enter valid password "user123"
    And I click the login button
    Then I should be logged in successfully
    And I should see the home page with user logged in

  @login @negative
  Scenario: Failed login with invalid credentials
    When I navigate to the login page
    And I enter invalid email "invalid@example.com"
    And I enter invalid password "wrongpassword"
    And I click the login button
    Then I should see an error message
    And I should remain on the login page

  @login
  Scenario Outline: Login attempts with different credential combinations
    When I navigate to the login page
    And I enter email "<email>"
    And I enter password "<password>"
    And I click the login button
    Then I should see "<result>"

    Examples:
      | email                 | password     | result                |
      | user@test.com         | user123      | successful login      |
      | admin@ecommerce.com   | admin123     | successful login      |
      | invalid@test.com      | wrongpass    | error message         |
      |                       | user123      | error message         |
      | user@test.com         |              | error message         |
