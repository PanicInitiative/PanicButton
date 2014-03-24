# @settings
# Feature: Facade and Settings page

#  Scenario: Activate the App
#    Given I wait up to 30 seconds for the "WizardActivity" screen to appear
#    And I press "Take me to the training"
#    And I press "Next"
#    And I enter text "1234" into field with id "create_pin_edittext"
#    And I press "Next"
#    And I press "I understand, set-up my trusted contacts now"
#    And I enter "123456789" into contact field 0
#    And I press "Next"

#    And I press "I understand, write my message now"
#    And I press "Next"

#    And I press "Next"

#    And I press "Next"
#    And I press "Next"
#    And I press "Try it now"
#    And I press "Finish & activate disguise"

#    And I long press "="
#    Then I enter text "1234" into field with id "pin_edit_text"
#    Then I press "Enter"
#    Then I see the text "Send emergency alert"
#    Then I see the text "Alert is in standby"
#    Then I see the text "SMS settings"
#    Then I see the text "Twitter settings"

#  Scenario: Enter wrong pin
#    Given I long press "="
#    And I enter text "4321" into field with id "pin_edit_text"
#    And I press "Enter"
#    Then I see the text "Please enter the correct pin"

#  Scenario: Verify that Activate alert button is active
#    Given I long press "="
#    And I enter text "1234" into field with id "pin_edit_text"
#    Then I press "Enter"
#    Then I see the text "Send emergency alert"
#    Then I verify "activate_alert" button is "enabled"

#  Scenario: Verify that SMSSettingsActivity is opened on SMS click
#    Given I long press "="
#    And I enter text "1234" into field with id "pin_edit_text"
#    And I press "Enter"
#    And I press "SMS settings"
#    Then I wait up to 10 seconds for the "SMSSettingsActivity" screen to appear

#  Scenario: Verify that TwitterSettingsActivity is opened on Twitter click
#    Given I long press "="
#    And I enter text "1234" into field with id "pin_edit_text"
#    And I press "Enter"
#    And I press "Twitter Settings"
#    Then I wait up to 10 seconds for the "TwitterSettingsActivity" screen to appear

#  Scenario: Verify the back button takes user to Facade
#    Given I long press "="
#    When I press view with id "login_back_button"
#    Then I wait up to 10 seconds for the "CalculatorActivity" screen to appear


