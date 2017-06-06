#  Panic Button - Security Documentation

## Introduction

This Security Documentation for Panic Button aims to communicate some of the ways that Panic Button attempts to protect its user or user's data against different types of adverse situations. It also helps understand what the application is NOT designed to achieve in terms of security.

### What do I need to know as a user?

It is important that you understand and assess the trade-offs you make in your security when using Panic Button. 

Always remember: 

> Panic Button only improves your safety when your contacts are in a position to act. The disguise aims to delay the discovery of the application for as long as possible while it sends location updates to your chosen contacts. However, this does not prevent a competent attacker from accessing the content or recipient of your messages, including your location. 

The mobile networks that allow you to send an SMS are not built to protect your privacy or the content of your messages. This is true for all calls and SMS communications you make, including SMS alerts sent by Panic Button. 
This present you and your contacts with direct risks if the groups who are threatening you have partnerships with the telecommunications companies in your country or have access to equipment that allows them to intercept telecommunications.

If you are using Panic Button in your work, we encourage you to visit our help pages to understand more about the general security risks of using a mobile phone. There is a list of suggested resources where you can learn about additional tools and strategies to mitigate against some of the risks of using a mobile phone.

The rest of this document is primarily for use by the technology team as a basis to develop a formal threat model that will be used to evaluate the implications of technology implementation choices throughout the application development life-cycle.


### How can I get involved in improving the security of Panic Button?

We're looking into all options to make Panic Button safer and we want to hear about your ideas and recommendations. We have listed a few Open Questions in this document and we're hoping to discuss their feasibility and desirability openly.

## Threat related Issues

### Main goals

  * Send alert messages and location to trusted contacts in case of emergency.
  * Disguise the application to send alert messages and location for as long as possible.

### Security goals

 * Prevent discovery of the application and disguise for non-competent users exploring the phone (such as during a routine check by a non technically savvy law enforcement officer)
 * Prevent access to application data (in particular contacts) to non-competent users.
 * Prevent data network operators (wifi and mobile data) from being able to identify users of the application. Telecommunication operators may be able to identify users.
 * Prevent the distribution of corrupted or malicious updates of the application. 

### Security non goals

  * No protection against the identification of the application and access to the application data by technically competent adversaries with access to the phone or to the GSM infrastructure.
  * No protection against access to the existence of and the content of messages sent from the App by technically competent adversaries, and adversaries with access to the GSM infrastructure.
  * No protection against the tracking of the location of the user of the application by technically competent adversaries.

## Open Questions

 - How much is the Content API needed? How needed is it to protect the Content API? It is using HTTPS currently, it could use pinned SSL certificate to prevent MITM. Content from the content API goes into the database but the code protects against SQL injection.

 - With regards to encrypted data store. While adversaries that have access to the infrastructure would know the identity of the contacts that are being alerted, in the event that a user is stopped before an alert is sent, if the data store is encrypted then we won't be revealing that information. 

> (Development note: The current recommended approach should probably be to use MODE_PRIVATE as described here (http://android-developers.blogspot.de/2013/02/using-cryptography-to-store-credentials.html) which would be equivalent to encrypting the preference file and storing the encryption key in internal storage. If so then Android device encryption should also be recommended. Possibly the "even more security" approach in this doc could be used but might not be safe for Android < 4.2. Using it with a 4 digit pins seem to also open it to brute force attacks.

 - Delete SMS sent items : SMS alerts should be deleted from the phone or not stored at all in the first place.

 - Should the application help with face-to-face setup of codewords? For example, if Alice, Bob and Carol were face-to-face, we could simultaneously use the application to establish a codeword for each contact. When Alice enters Bob into her phone she would enter the codeword. I could use a different word for Carol. It would never be transmitted over the network, EXCEPT in an alert. If Bob or Carol received an alert from me without the codeword they would know it was not authentic. This might prevent an adversary from sending messages like “I am in danger” with fake location data. 

 - Should the application be able to identify inbound alerts? For example, if the application on my phone sees an alert from the application on Tanya’s phone it might use a more aggressive sound/vibrate/on screen alert. I’d hate to miss an alert from a friend because I ignored the cute everyday SMS notifier sound. When/if the application makes it onto iOS, this might help override the “quiet time” functionality in newer versions of iOS. 

 - Should the application facilitate “Alert if unresponsive” notifications? If I am heading into a contentious situation, it might be useful to notify my contacts if I do not somehow indicate that I am ok. Perhaps this is a countdown timer. I set the timer to 60 minutes and if I do not clear or reset the timer within 60 minutes my contacts are notified.
