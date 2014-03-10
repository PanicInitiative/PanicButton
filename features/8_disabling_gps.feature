@gps_settings
Feature: Offline work

  Scenario: Change GPS status
    Then I switch gps

  Scenario: Opening app
    Given I wait upto 60 seconds for the "WizardActivity" screen to appear
    Then I see the text "Panic Button"
    Then I press "Set-Up"
    Then I see the text "Get started"

  Scenario: Change GPS status
    Then I switch gps
