from com.android.monkeyrunner import MonkeyRunner, MonkeyDevice
from subprocess import call
import commands
import sys
import os

#print "restart adb"
#call(os.environ['ANDROID_HOME'] + '/platform-tools/adb kill-server', shell=True)
#call(os.environ['ANDROID_HOME'] + '/platform-tools/adb start-server', shell=True)

MonkeyRunner.sleep(2)
device = MonkeyRunner.waitForConnection()
 
print "start pressing power button"

device.press('KEYCODE_POWER' , MonkeyDevice.DOWN_AND_UP)
MonkeyRunner.sleep(0.2)
device.press('KEYCODE_POWER' , MonkeyDevice.DOWN_AND_UP)
MonkeyRunner.sleep(0.2)
device.press('KEYCODE_POWER' , MonkeyDevice.DOWN_AND_UP)
MonkeyRunner.sleep(0.2)
device.press('KEYCODE_POWER' , MonkeyDevice.DOWN_AND_UP)
MonkeyRunner.sleep(0.2)
device.press('KEYCODE_POWER' , MonkeyDevice.DOWN_AND_UP)
MonkeyRunner.sleep(0.2)
device.press('KEYCODE_POWER' , MonkeyDevice.DOWN_AND_UP)
MonkeyRunner.sleep(0.2)
device.press('KEYCODE_POWER' , MonkeyDevice.DOWN_AND_UP)
MonkeyRunner.sleep(0.2)
device.press('KEYCODE_POWER' , MonkeyDevice.DOWN_AND_UP)
MonkeyRunner.sleep(0.2)
device.press('KEYCODE_POWER' , MonkeyDevice.DOWN_AND_UP)
MonkeyRunner.sleep(0.2)
device.press('KEYCODE_POWER' , MonkeyDevice.DOWN_AND_UP)
MonkeyRunner.sleep(0.2)
device.press('KEYCODE_POWER' , MonkeyDevice.DOWN_AND_UP)
MonkeyRunner.sleep(0.2)
device.press('KEYCODE_POWER' , MonkeyDevice.DOWN_AND_UP)
MonkeyRunner.sleep(0.2)
device.press('KEYCODE_POWER' , MonkeyDevice.DOWN_AND_UP)
MonkeyRunner.sleep(0.2)
device.press('KEYCODE_POWER' , MonkeyDevice.DOWN_AND_UP)
MonkeyRunner.sleep(0.2)
device.press('KEYCODE_POWER' , MonkeyDevice.DOWN_AND_UP)
MonkeyRunner.sleep(0.2)
device.press('KEYCODE_POWER' , MonkeyDevice.DOWN_AND_UP)
MonkeyRunner.sleep(0.2)

print "done"