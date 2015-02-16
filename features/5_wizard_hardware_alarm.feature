@android-10 @android-11 @android-12 @android-13 @android-14 @android-15 @android-16 @android-17 @android-18 @android-19
Feature: Hardware Alarm Test in the Wizard

  Background: Go to the Hardware Alarm screen
    Given I wait up to 60 seconds for "Set-Up" to appear
      And I press "Set-Up"
      And I wait
      And I press "Get started"
      And I wait
      And I enter "123456" into contact field 0
      And I press "Next"
      And I wait
      And I press "Next"
      And I wait
      And I enter "1234" into input field number 1
      And I press "Next"
      And I wait
      And I press "Setup Alarm"
      And I wait
      And I press "Learn"
      And I wait
      Then I see "Try Now! Repeatedly press the power button until you feel a vibration."

  @current @PB-721
  Scenario: Triggering Alert with Hardware Button 
      And I press power button 5 times
      And I unlock device
      Then I see "Well done!"
