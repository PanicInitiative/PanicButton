# **Change Log**

All notable changes to this project will be documented in this file. 
Unreleased


##  **[v.1.4.1] - 2015-09-21**

### **Added**

-	Added “Stop alert message” box in message screen once the setup is done.#69

### **Changed**

-	Handling the nested fragments for contact screen programatically. #81
- Handling the nested fragments for message screen programatically. #98
 -Long press time is reduced from 5 seconds to 3 seconds in disguise screen.#90

### **Fixed**

- On some devices, the contact selection screen was malfunctioning. #57

## **[v.1.4.0](https://github.com/PanicInitiative/PanicButton/releases/tag/v1.4.0) - 2015-04-21**

### **Added**

-	Added “Stop alert message” box in message screen once the setup is done.#69

### **Changed**

- The trigger mechanism has changed and now requires a confirmation press
-	When the alert is manually deactivated (after entering your PIN) your contacts will receive a message which says "The alert has been stopped"
- The information text is clearer regarding what Panic Button does. When you trigger the alert, Panic Button will start sending a message with your location every 5 minutes.
- 	German translation generously contributed by https://github.com/emdete

### **Fixed**

- One some devices, the alert activates by itself in the middle of phone calls (it activates without triggering it with the power button or the calc app) #38
- Shortcut was created by Google Play. #35

##  **[v.1.4.0 -alpha] (https://github.com/PanicInitiative/PanicButton/releases/tag/1.4.0-alpha)- 2015-04-06**

### **Changed**
- The trigger mechanism has changed and now requires a confirmation press.
- The information text is clearer regarding what Panic Button does. When you trigger the alert, Panic Button will start sending a message with your location every 5 minutes

### **Fixed**
- On some devices, the alert activates by itself in the middle of phone calls (it activates without triggering it with the power button or the calc app). #38
- Shortcut was created by Google Play #35


[v.1.3.1-alpha] - 2015-09-21

##  **[v.1.4.0 -alpha] (https://github.com/PanicInitiative/PanicButton/releases/tag/v1.3.1)- 2014-08-06**

### **Fixed**

- On some devices, the alert activates by itself in the middle of phone calls (it activates without triggering it with the power button or the calc app). #38
- Shortcut was created by Google Play #35


##  **[v.1.3.0] (https://github.com/PanicInitiative/PanicButton/releases/tag/v1.3.0)- 2014-06-23**

### **Changed**

- The first message is now the customised message, while subsequent ones only contain the location or a location pending message #41
- Clicking Done on the keyboard when entering the PIN automatically validates it. #27
- Link to map is now using https: #28
- Removed Content API to reduce the attack surface. #40
- The help pages now contain more information about the risk of contact and message exposure #33

### **Fixed**

- Clicking on the Security Tips link in the checklist now links properly to the Help section. #30
- Fixed bug where two SMS were sent when the alert was active & location was retrieved for the first time
- Fixed problem where pushing the back button from the checklist exited to the calculator disguise.

