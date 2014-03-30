@wizard
Feature: Current Important Problems
  Background: Go to Setup Contacts screen
    Given I wait up to 60 seconds for "Set-Up" to appear
      And I press "Set-Up"
      And I see the text "Get started"
      And I press "Get started"
      And I see the text "Setup contacts"

  Scenario: Clicking the phone book icon and back button returns to Setup Contacts
    When I press "Choose your emergency contact"
     And I wait for 5 seconds
    Then I take a screenshot
     And I press back button
    Then I take a screenshot
    Then I wait up to 5 seconds for "Think about who can" to appear