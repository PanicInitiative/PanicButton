@wizard
Feature: One time setup wizard

  Background: Start the wizard
    Given I wait up to 60 seconds for the "WizardActivity" screen to appear

  Scenario: In Wizard, clicking Setup then the Get Started screen is shown
    Given I see the text "Panic Button"
    And I press "Set-Up"
    Then I see the text "Get started"

  Scenario: In Setup Contacts the Next button is disabled
    Given I see the text "Panic Button"
    And I press "Set-Up"
    And I press "Get started"
    Then I see the text "Setup contacts"
    Then I verify "Next" button is "disabled"

  Scenario: In Setup Contacts, entering valid phone number then the Next button is enabled
    Given I see the text "Panic Button"
    And I press "Set-Up"
    And I press "Get started"

    And I see the text "Setup contacts"
    And I enter "123456" into contact field 0
    Then I verify "Next" button is "enabled"

  Scenario: In Setup Contacts, entering invalid phone number then the Next button is disabled
    Given I see the text "Panic Button"
    And I press "Set-Up"
    And I press "Get started"

    And I see the text "Setup contacts"
    And I enter "1234" into contact field 0

    Then I verify "Next" button is "disabled"

  Scenario: In Setup Contacts, entering one valid phone number then the Next button is enabled
    Given I see the text "Panic Button"
    And I press "Set-Up"
    And I press "Get started"

    And I see the text "Setup contacts"
    And I enter "123" into contact field 0
    And I enter "1234" into contact field 1
    And I enter "12345" into contact field 2
    Then I verify action button is "enabled"

  Scenario: In Setup Contacts, clicking on a phone book icon and back button returns to Setup Contacts
    Given I see the text "Panic Button"
    And I press "Set-Up"
    And I press "Get started"
    Then I see the text "Setup contacts"
    Then I press "Choose your emergency contact"
    Then I wait up to 5 seconds

    Then I press back button
    Then I wait up to 10 seconds for "Setup contacts" to appear

 #  Scenario: SMS Settings
 #    Given I press "Take me to the training"
 #    And  I see the text "Panic Button Training"
 #    And  I press "Next"

 #    And  I see the text "Step 1"
 #    And  I enter text "1234" into field with id "create_pin_edittext"
 #    And  I press "Next"

 #    And  I see the text "Step 2"
 #    And  I press "I understand, set-up my trusted contacts now"

 #    Then I see the text "Step 2: Set-up your trusted contacts"
 #    Then I verify action button text is "Next"
 #    Then I verify action button is "disabled"
 #    Then I see the text "Think first! Your trusted contacts should be people that you know personally and should be prepared and able to respond fast."

 #  @hardware_back
 #  Scenario: Hardware back in wizard takes back to previous screen
 #    Given I press "Take me to the training"
 #    And I go back
 #    Then I see the text "Panic Button"
 #    Then I see the text "Welcome"
 #    Then I verify action button text is "Take me to the training"
 #    Then I verify action button is "enabled"

 #  Scenario: Navigating back to start screen
 #    Given I press "Take me to the training"
 #    And I press view with id "previous_button"
 #    Then I see the text "Panic Button"
 #    Then I see the text "Welcome"
 #    Then I verify action button text is "Take me to the training"
 #    Then I verify action button is "enabled"

 

 #  Scenario: Save SMS settings
 #    Given I press "Take me to the training"
 #    And  I see the text "Panic Button Training"
 #    And  I press "Next"

 #    And  I see the text "Step 1"
 #    And  I enter text "1234" into field with id "create_pin_edittext"
 #    And  I press "Next"

 #    And  I see the text "Step 2"
 #    And  I press "I understand, set-up my trusted contacts now"

 #    And I see the text "Step 2: Set-up your trusted contacts"
 #    And I enter "123456789" into contact field 0
 #    And I enter "222-222-2222" into contact field 1
 #    And I enter "100" into contact field 2
 #    Then I press "Next"

 #    Then I see the text "Step 3"
 #    Then I verify action button text is "I understand, write my message now"
 #    Then I verify action button is "enabled"

 #  Scenario: Verifying the saved sms settings
 #    Given I press "Take me to the training"
 #    And  I see the text "Panic Button Training"
 #    And  I press "Next"

 #    And  I see the text "Step 1"
 #    And  I enter text "1234" into field with id "create_pin_edittext"
 #    And  I press "Next"

 #    And  I see the text "Step 2"
 #    And  I press "I understand, set-up my trusted contacts now"

 #    And I see the text "Step 2: Set-up your trusted contacts"
 #    And I enter "123456789" into contact field 0
 #    And I enter "222-222-2222" into contact field 1
 #    And I enter "100" into contact field 2
 #    And I press "Next"

 #   And I see the text "Step 3"
 #   And I press view with id "previous_button"
 #   Then I see the text "Step 2: Set-up your trusted contacts"
 #   Then I verify action button is "enabled"
 #   Then I see the text "*******89"
 #   Then I see the text "**********22"
 #   Then I see the text "*00"

 # Scenario: Emergency alert set message
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

 #   Then I see the text "Step 3: Create an emergency message"
 #   Then I verify action button text is "Next"
 #   Then I verify action button is "enabled"
 #   Then I see the text "Help me, I'm in danger"
 #   Then I see the text "I'm at http://maps.google.com/GPS-location"


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