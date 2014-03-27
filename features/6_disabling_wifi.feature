@welcome
Feature: Setting up

  Scenario: Turning off wifi for TC
    Then I turn off wifi
    Then I wait for 2 seconds
    Then I press back button