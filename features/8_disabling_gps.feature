@android-10 @android-11 @android-12 @android-13 @android-14 @android-15 @android-16 @android-17 @android-18 @android-19
Feature: Sending sms with different gps status

  Scenario: Setting up
    Given I wait up to 60 seconds for "Set-Up" to appear
    And I press "Set-Up"
    And I press "Get started"
    And I wait for 1 second
    And I enter "123456" into contact field 0
    And I press "Next"
    And I wait for 1 second
    And I press "Next"
    And I wait for 1 second
    And I enter "1234" into input field number 1
    And I press "Next"
    And I press "Setup Alarm"
    And I press "Learn"
    Then I see "Try Now! Repeatedly press the power button until you feel a vibration."
    And I press power button 5 times
    And I unlock device
    And I wait for 5 seconds
    Then I see "Well done!"
    And I press "Next"
    And I press "Learn"
    And I click 5 times fast on calculation
    And I wait for 5 seconds
    Then I see "Well done!"
    And I press "Next"
    And I press "Setup Disguise"
    And I press "Learn"
    And I press "Calculate!"
    Then I see "Try now! Hold down any button on the calculator."
    And I long press custom
    Then I see "Well done! Now enter your pincode to access settings."
    And I enter "1234" into input field number 1
    And I press "Ok"
    And I press "Finish"

  Scenario: Start alarm with hardware button
    Given I clear log
    And I wait up to 5 seconds for "=" to appear
    And I press power button 5 times
    And I unlock device
    And I start application
    And I long press custom
    And I enter "1234" into input field number 1
    And I press "Ok"
    Then I see "Alerting"
    And I press "Stop Alerting"
    Then I see "Ready"
    Then I check sms text contains "Help me, I'm in danger"


  Scenario: Change GPS status
    Then I switch gps


  Scenario: Start alarm with clicking in calculation
    Given I clear log
    And I wait up to 5 seconds for "=" to appear
    And I click 5 times fast on calculation
    And I start application
    And I long press custom
    And I enter "1234" into input field number 1
    And I press "Ok"
    Then I see "Alerting"
    And I press "Stop Alerting"
    Then I see "Ready"
    Then I check sms text contains "I am out of danger"

  Scenario: Change GPS status
    Then I switch gps
