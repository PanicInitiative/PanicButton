# @sms_settings
# Feature: SMS Settings page

#  @clear_sms
#  Scenario: Open the SMS Settings page
#    Given I wait upto 30 seconds for the "WizardActivity" screen to appear
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
#    And I press "SMS settings"
#    Then I wait upto 10 seconds for the "SMSSettingsActivity" screen to appear

#  Scenario: Save SMS settings
#    Given I long press "="
#    Then I enter text "1234" into field with id "pin_edit_text"
#    Then I press "Enter"
#    And I press "SMS settings"
#    And I clear input field number 1
#    And I enter "123456789" into contact field 0
#    And I enter "222-222-2222" into contact field 1
#    And I enter "100" into contact field 2
#    And I enter text " testing message" into field with id "message_edit_text"
#    When I press "Save"
#    Then I see the text "*******89"
#    Then I see the text "**********22"
#    Then I see the text "*00"
#    Then I see the text "Help me, I'm in danger testing message"

#  Scenario: View Saved Settings
#    Given I long press "="
#    Then I enter text "1234" into field with id "pin_edit_text"
#    Then I press "Enter"
#    And I press "SMS settings"
#    Then I see the text "*******89"
#    Then I see the text "**********22"
#    Then I see the text "*00"
#    Then I see the text "Help me, I'm in danger testing message"

#  Scenario: Verify no save without valid number
#    Given I long press "="
#    Then I enter text "1234" into field with id "pin_edit_text"
#    Then I press "Enter"
#    And I press "SMS settings"
#    And I clear input field number 1
#    And I clear input field number 2
#    And I clear input field number 3
#    Then I verify "sms_save_button" button is "disabled"