@welcome
Feature: Offline work

  Scenario: Open app without internet connection
    Then I wait up to 10 seconds for the "WizardActivity" screen to appear
    Then I wait for "Set-Up" to appear

  Scenario: Turning on wifi after TC
    Then I turn on wifi
    Then I wait for 10 seconds
    Then I press back button