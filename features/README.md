PanicButtonAutomation
=====================

PanicButton android app automation

[Running test](https://github.com/calabash/calabash-android)
------------
To run your test:

    calabash-android run <apk>

For example:

     calabash-android run target/panic-button-dev.apk MAIN_ACTIVITY=org.iilab.pb.HomeActivity

Calabash-android will install an instrumentation along with your app when executing the app. We call this instrumentation for "test server". The "test server" has special permission that allows it to interact very closely with your app during test.
Everytime you test a new binary or use an upgraded version of calabash a new test server will be build.
The test server is an intrumentation that will run along with your app on the device to execute the test.

[Installation](https://github.com/calabash/calabash-android/blob/master/documentation/installation.md)
============
### Prerequisites
You need to have Ruby installed. Verify your installation by running ruby -v in a terminal - it should print "ruby 1.8.7" (or higher).

If you are on Windows you can get Ruby from [RubyInstaller.org](http://rubyinstaller.org/)

You should have the Android SDK installed and the environment variable `ANDROID_HOME` should be pointing to it.

You also need to have Ant installed and added to your path

### Installation

Install `calabash-android` by running

- `gem install calabash-android`
- You might have to run `sudo gem install calabash-android` if you do not have the right permissions.
 


[Steps for tests](https://github.com/calabash/calabash-android/blob/master/ruby-gem/lib/calabash-android/canned_steps.md)



