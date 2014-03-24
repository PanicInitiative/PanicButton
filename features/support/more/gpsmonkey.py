from com.android.monkeyrunner import MonkeyRunner, MonkeyDevice
import commands
import sys
device = MonkeyRunner.waitForConnection()
#Starting gps settings app
device.shell('am start --activity-reset-task-if-needed -W -a android.settings.LOCATION_SOURCE_SETTINGS')
MonkeyRunner.sleep(2)

print "start working with gps"
#KEYCODE_1

#Swirching gps
device.press('KEYCODE_BUTTON_1' , MonkeyDevice.DOWN_AND_UP)
MonkeyRunner.sleep(2)

#If submitting then pop up appears here
device.press('KEYCODE_DPAD_RIGHT' , MonkeyDevice.DOWN_AND_UP)
device.press('KEYCODE_ENTER' , MonkeyDevice.DOWN_AND_UP)
MonkeyRunner.sleep(2)

#Closing
device.shell('am force-stop com.android.settings')
# device.press('KEYCODE_BACK' , MonkeyDevice.DOWN_AND_UP)

#TODO: check that gps enabled

print "gps status changed"