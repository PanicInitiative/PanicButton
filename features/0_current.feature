@wizard
Feature: Current Important Problems

  Scenario: Clicking the phone book icon and back button returns to Setup Contacts
    Given I wait up to 60 seconds for "Set-Up" to appear
      And I press "Set-Up"
      And I see the text "Get started"
      And I press "Get started"
      And I see the text "Setup contacts"

     When I press "Choose your emergency contact"
      And I wait for 5 seconds
     Then I take a screenshot
      And I press back button
     Then I take a screenshot
     Then I wait up to 5 seconds for "Think about who can" to appear

  Scenario: Triggering Alert with Hardware Button 
    Given I wait up to 60 seconds for "Set-Up" to appear
      And I press "Set-Up"
      And I press "Get started"
      And I wait
      And I enter "123456" into contact field 0
      And I press "Next"
      And I wait
      And I press "Next"
      And I wait
      And I enter "1234" into input field number 1
      And I press "Next"
      And I press "Setup Alarm"
      And I press "Learn"
     Then I see "Try Now! Repeatedly press the power button fast until you feel a vibration."

      And I press power button 5 times
      And I unlock device
      Then I see "Well done!"
