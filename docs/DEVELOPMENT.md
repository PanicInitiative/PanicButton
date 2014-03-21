Setup up a development environment
==================================

If you're really up for this then why not help us move to Gradle instead? :)

* Download ADT Bundle

http://developer.android.com/sdk/index.html
Install in your /Applications folder if you're on OSX. 

* Download the default Android SDK version 22.3

```
Go to Window > Android SDK Manager

Select "Android 4.2 (API 17) > SDK Platform"
(Optionally) install the System Image (ARM or x86) if you want to run the debugger or emulator.

Click on Install packages button.
```

![Android SDK Manager](https://raw.githubusercontent.com/TeamPanicButton/PanicButton/master/docs/setup_images/1.png)

```
Accept the licenses
Click Install
(Note that this will take a while)
```

![Licenses](https://raw.githubusercontent.com/TeamPanicButton/PanicButton/master/docs/setup_images/2.png)

```
Close the SDK Manager.
```

* Install eclipse packages

```
Go to Help > Install new software
Choose "All Available Sites"
Select "MarketPlace Client" under General Purpose Tools
Also select "Eclipse Egit "
Click Next, Accept the License and Finish.
Accept to Restart.
```

![Import](https://raw.githubusercontent.com/TeamPanicButton/PanicButton/master/docs/setup_images/a.1.png)

```
Go to Help > Eclipse MarketPlace
Find "Android Configurator for M2E"
Click Next, Accept the License and Finish.

```

![Import](https://raw.githubusercontent.com/TeamPanicButton/PanicButton/master/docs/setup_images/a.2.png)

* Clone the repository

```
cd ~/workspace
git clone https://github.com/TeamPanicButton/PanicButton.git
```

* Import the project.
 
```
Go to File > Import
```

![Import](https://raw.githubusercontent.com/TeamPanicButton/PanicButton/master/docs/setup_images/3.png)

```
Select Maven > Import Maven Project
```

![Android](https://raw.githubusercontent.com/TeamPanicButton/PanicButton/master/docs/setup_images/4.png)

```
Locate the git folder you've cloned.
```

![Import](https://raw.githubusercontent.com/TeamPanicButton/PanicButton/master/docs/setup_images/5.png)

```
Accept the proposed Maven Plugins (resolve later is fine).
Once the project is open you should have some errors in the Problem window.
Choose the Quick Fix for the comsumer-aar related error and choose to 
```

![Import](https://raw.githubusercontent.com/TeamPanicButton/PanicButton/master/docs/setup_images/6.png)

```
Upgrade to maven 3.1.1 
http://maven.apache.org/download.cgi
or
brew install maven
Then change the version of maven used in Eclipse:
Go to Run > Run configurations
At the bottom right (Maven Runtime) choose "Configure..."
Navigate to /usr/local/Cellar/maven/3.1.1/libexec
Yes that's right. No copy and pasting the path on OSX... Still here? Wishing you'd helped us move to Gradle instead yet?
```

![Import](https://raw.githubusercontent.com/TeamPanicButton/PanicButton/master/docs/setup_images/a.4.png)

```
Last step.
In Run Configurations:
In "Goal" add the "install" goal. (Optionally if you've setup the Android Emulator you can add "android:deploy")
Add the "android.sdk.path" variable in your Maven Run configuration and set it to the sdk path (which is in a sister folder to Eclipse.app). for instance /Applications/adt-bundle-mac-x86_64-20131030/sdk/
```

```
Ok one more.
Build even if coverage tests fail set "failBuildIfCoverage" to false.
```

Now go ahead and Go to Project > Run > Maven build. There you go an APK ! 
