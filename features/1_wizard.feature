@welcome
Feature: Wizard

  Scenario: Wizard is displayed and can get started after Installation
    Given I wait up to 60 seconds for "Set-Up" to appear
     When I press "Set-Up"
     Then I see the text "Get started"
