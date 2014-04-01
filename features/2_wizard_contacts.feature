Feature: Setup Contacts in Wizard
  In order to setup the application after installation
  As a user
  I want to go through the installation wizard

  Background: Go to Setup Contacts screen
    Given I wait up to 60 seconds for "Set-Up" to appear
      And I press "Set-Up"
      And I wait
      And I see the text "Get started"
      And I wait
      And I press "Get started"
      And I wait
      And I see the text "Setup contacts"

  Scenario: Next button is disabled
    Then I verify "Next" button is "disabled"

#  Scenario Outline: Phone numbers are validated
#    When I enter <number0> into contact field 0
#    When I enter <number1> into contact field 1
#    When I enter <number2> into contact field 2
#    Then I verify "Next" button is <status>

#    Examples:
#      | number0  | number1 | number2 | status     |
#      | "123456" |         |         | "enabled"  | 
#      | "1234"   |         |         | "disabled" | 
#      | "123"    | "1234"  | "12345" | "enabled"  | 

  Scenario: Saved phone numbers are obfuscated
    When I enter "123456789" into contact field 0
     And I enter "222-222-2222" into contact field 1
     And I enter "100" into contact field 2
     And I press "Next"

     Then I see the text "Setup alert message"
      And I go back

     Then I see the text "Setup contacts"
     Then I verify "Next" button is "enabled"
     Then I see the text "*******89"
     Then I see the text "********22"
     Then I see the text "*00"

  @current @android-10 @android-11 @android-12 @android-13 @android-14 @android-15 @android-16 @android-17 @android-18
  Scenario: Clicking the phone book icon and back button returns to Setup Contacts
    When I press "Choose your emergency contact"
     And I wait for 5 seconds
    Then I take a screenshot
     And I press back button
    Then I take a screenshot
    Then I wait up to 5 seconds for "Think about who can" to appear

  @current @android-19
  Scenario: Clicking the phone book icon and back button returns to Setup Contacts
    When I press "Choose your emergency contact"
     And I wait for 5 seconds
    Then I take a screenshot
     And I press back button
     And I press back button
    Then I take a screenshot
    Then I wait up to 5 seconds for "Think about who can" to appear

  Scenario: Hardware back in wizard takes back to previous screen
    When I go back
    Then I see the text "Get started"