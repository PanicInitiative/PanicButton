@welcome
Feature: Welcome page

  Scenario: Verify wizard start screen is displayed on clicking wizard entry point
    Given I wait upto 60 seconds for the "WizardActivity" screen to appear
    Then I see the text "Panic Button"
    Then I press "Set-Up"
    Then I see the text "Get started"