from com.android.monkeyrunner import MonkeyRunner, MonkeyDevice
import commands
import sys
device = MonkeyRunner.waitForConnection()

print "Unlocking"

device.wake()
MonkeyRunner.sleep(1)
#Unloking device
width = int(device.getProperty('display.width'))
height = int(device.getProperty('display.height'))
device.drag((width/2, height*3/4), (width, height*3/4), 2.0, 50)

print "done"