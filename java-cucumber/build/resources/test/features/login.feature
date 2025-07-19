Feature: User Login
  As a customer
  I want to log into the e-commerce application
  So that I can access my account and make purchases

  Background:
    Given I am on the e-commerce homepage

  @smoke @login
  Scenario: Successful login with valid credentials
    When I navigate to the login page
    And I enter valid username "user@example.com"
    And I enter valid password "password123"
    And I click the login button
    Then I should be logged in successfully
    And I should see the user dashboard

  @login @negative
  Scenario: Failed login with invalid credentials
    When I navigate to the login page
    And I enter invalid username "invalid@example.com"
    And I enter invalid password "wrongpassword"
    And I click the login button
    Then I should see an error message "Invalid credentials"
    And I should remain on the login page

  @login
  Scenario Outline: Login attempts with different credential combinations
    When I navigate to the login page
    And I enter username "<username>"
    And I enter password "<password>"
    And I click the login button
    Then I should see "<result>"

    Examples:
      | username          | password     | result                |
      | user@example.com  | password123  | user dashboard        |
      | admin@example.com | admin123     | admin dashboard       |
      | invalid@test.com  | wrongpass    | Invalid credentials   |
      |                   | password123  | Username is required  |
      | user@example.com  |              | Password is required  |
