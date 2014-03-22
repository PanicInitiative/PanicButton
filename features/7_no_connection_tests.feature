@welcome
Feature: Offline work

  Scenario: Open app without internet connection
    Then I wait upto 120 seconds for the "WizardActivity" screen to appear
    Then I wait for "Set-Up" to appear

  Scenario: Terning on wifi after TC
    Then I tern on wifi
    Then I wait for 10 seconds
    Then I press back button