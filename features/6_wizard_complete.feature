@android-10 @android-11 @android-12 @android-13 @android-14 @android-15 @android-16 @android-17 @android-18 @android-19
Feature: Wizard complete

  Scenario: Setting up
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
	    And I press power button 5 times
	    And I unlock device
      And I wait
     Then I see "Well done!"
      And I press "Next"
      And I wait
      And I press "Learn"
      And I wait
      And I click 5 times fast on calculation
      And I wait
     Then I see "Well done!"
      And I press "Next"
      And I wait
      And I press "Setup Disguise"
      And I wait
      And I press "Learn"
      And I wait
      And I press "Calculate!"
      And I wait
     Then I see "Try now! Hold down any button on the calculator."
      And I long press custom
      And I wait
     Then I see "Well done! Now enter your pincode to access settings."
      And I enter "1234" into input field number 1
      And I press "Ok"
      And I wait
      And I press "Finish"
#      And I wait
#     Then I see "Ready"