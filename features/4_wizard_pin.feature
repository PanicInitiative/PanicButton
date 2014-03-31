@android-10 @android-11 @android-12 @android-13 @android-14 @android-15 @android-16 @android-17 @android-18 @android-19
Feature: Pin setup in the Wizard

  Background: Go to the Message Setup screen
    Given I wait up to 60 seconds for "Set-Up" to appear
      And I press "Set-Up"
      And I wait
      And I see the text "Get started"
      And I press "Get started"
      And I wait
      And I see the text "Setup contacts"
      And I enter "123456" into contact field 0
      And I press "Next"
      And I wait
      And I see the text "Setup alert message"
      And I press "Next"
      And I wait

  Scenario: Entering Pin
      When I enter "1234" into input field number 1
       And I press "Next"
      Then I see "You cannot use Panic Button"