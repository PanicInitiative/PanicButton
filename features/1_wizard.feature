@android-10 @android-11 @android-12 @android-13 @android-14 @android-15 @android-16 @android-17 @android-18 @android-19
Feature: Wizard

  Scenario: Wizard is displayed and can get started after Installation
    Given I wait up to 60 seconds for "Set-Up" to appear
     When I press "Set-Up"
      And I wait
     Then I see the text "Get started"
