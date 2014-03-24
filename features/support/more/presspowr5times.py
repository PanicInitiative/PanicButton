from com.android.monkeyrunner import MonkeyRunner, MonkeyDevice
import commands
import sys
device = MonkeyRunner.waitForConnection()

print "start pressing power button"

device.press('KEYCODE_POWER' , MonkeyDevice.DOWN_AND_UP)
MonkeyRunner.sleep(1)
device.press('KEYCODE_POWER' , MonkeyDevice.DOWN_AND_UP)
MonkeyRunner.sleep(1)
device.press('KEYCODE_POWER' , MonkeyDevice.DOWN_AND_UP)
MonkeyRunner.sleep(1)
device.press('KEYCODE_POWER' , MonkeyDevice.DOWN_AND_UP)
MonkeyRunner.sleep(1)
device.press('KEYCODE_POWER' , MonkeyDevice.DOWN_AND_UP)
MonkeyRunner.sleep(1)

print "done"