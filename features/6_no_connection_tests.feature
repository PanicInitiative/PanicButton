@welcome
Feature: Welcome page

  Scenario: Open app without internet connection
    Given I wait upto 60 seconds for the "WizardActivity" screen to appear
    Then I press home button
    Then There is no internet connection
#    Then I see the text "Panic Button"
#    Then I press "Set-Up"
#    Then I see the text "Get started"