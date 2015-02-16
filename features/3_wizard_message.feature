@android-10 @android-11 @android-12 @android-13 @android-14 @android-15 @android-16 @android-17 @android-18 @android-19
Feature: Message setup in the Wizard

  Background: Go to the Message Setup screen
    Given I wait up to 60 seconds for "Set-Up" to appear
      And I press "Set-Up"
      And I wait
      And I see the text "Get started"
      And I press "Get started"
      And I wait
      And I see the text "Setup contacts"
      And I enter "123456" into contact field 1
      And I press "Next"
      And I wait
      And I see the text "Setup alert message"

  Scenario: Emergency alert set message
    Then I see the text "Next"
    Then I verify "Next" button is "enabled"
#    Then I see the text "Help me, I'm in danger"
    Then I should see text "Help me, I'm in danger" in message view
    Then I see the text "I'm at https://maps.google.com/GPS-location" with special chars

  Scenario: Navigating back to start screen
    When I go back
     And I wait
     And I should see text "****56" in contact edit
     And I go back
     And I wait
    Then I see the text "Get started"

 # Scenario: Emergency alert 1-2-3
 #   Given I press "Take me to the training"
 #   And  I press "Next"
 #   And  I see the text "Step 1"
 #   And  I enter text "1234" into field with id "create_pin_edittext"
 #   And  I press "Next"

 #   And  I see the text "Step 2"
 #   And  I press "I understand, set-up my trusted contacts now"

 #   And I see the text "Step 2: Set-up your trusted contacts"
 #   And I press "Next"

 #   And I see the text "Step 3"
 #   And I press "I understand, write my message now"
 #   And I press "Next"

 #   And I see the text "Step 3: Create an emergency message"
 #   And I press "Next"

 #   Then I see the text "3 ways to send an emergency alert"
 #   Then I verify action button text is "Next"
 #   Then I verify action button is "enabled"
 #   Then I see the text "#1. The Emergency Alert button in Settings…"
 #   Then I press "Next"
 #   Then I see the text "#2. From any button on the app’s disguise…"
 #   Then I press "Next"
 #   Then I see the text "#3. Using the phone’s Power button…"



 # Scenario: Finish wizard
 #   Given I press "Take me to the training"
 #   And  I press "Next"
 #   And  I see the text "Step 1"
 #   And  I enter text "1234" into field with id "create_pin_edittext"
 #   And  I press "Next"

 #   And  I see the text "Step 2"
 #   And  I press "I understand, set-up my trusted contacts now"

 #   And I see the text "Step 2: Set-up your trusted contacts"
 #   And I press "Next"

 #   And I see the text "Step 3"
 #   And I press "I understand, write my message now"
 #   And I press "Next"

 #   And I see the text "Step 3: Create an emergency message"
 #   And I press "Next"

 #   And I see the text "3 ways to send an emergency alert"
 #   Then I press "Next"
 #   Then I press "Next"
 #   Then I see the text "Step 5: Activate Disguise"
 #   Then I verify action button text is "Try it now"
 #   Then I press "Try it now"
 #   Then I see the text "Panic Button will now disguise itself"



 # Scenario: Open facade
 #   Given I press "Take me to the training"
 #   And  I press "Next"
 #   And  I see the text "Step 1"
 #   And  I enter text "1234" into field with id "create_pin_edittext"
 #   And  I press "Next"

 #   And  I see the text "Step 2"
 #   And  I press "I understand, set-up my trusted contacts now"

 #   And I see the text "Step 2: Set-up your trusted contacts"
 #   And I press "Next"

 #   And I see the text "Step 3"
 #   And I press "I understand, write my message now"
 #   And I press "Next"

 #   And I see the text "Step 3: Create an emergency message"
 #   And I press "Next"

 #   And I see the text "3 ways to send an emergency alert"
 #   And I press "Next"
 #   And I press "Next"
 #   And I see the text "Step 5: Activate Disguise"
 #   And I press "Try it now"
 #   And I press "Finish & activate disguise"
 #   Then I wait upto 10 seconds for the "CalculatorActivity" screen to appear
