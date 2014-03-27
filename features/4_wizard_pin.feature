@wizard
Feature: Pin setup in the Wizard

  Background: Go to the Message Setup screen
    Given I wait up to 60 seconds for "Set-Up" to appear
      And I press "Set-Up"
      And I see the text "Get started"
      And I press "Get started"
      And I see the text "Setup contacts"
      And I enter "123456" into contact field 0
      And I press "Next"
      And I see the text "Setup alert message"
      And I press "Next"
      And I wait for 1 second

  Scenario: Entering Pin
      When I enter "1234" into input field number 1
       And I press "Next"
      Then I see "You cannot use Panic Button"