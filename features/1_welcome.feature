@welcome
Feature: Welcome page

  Scenario: Verify wizard start screen is displayed on clicking wizard entry point
    Given I wait upto 30 seconds for the "WizardActivity" screen to appear
    Then I see the text "Panic Button"
    Then I verify action button text is "Take me to the training"
    Then I verify action button is "enabled"