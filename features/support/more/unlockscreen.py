from com.android.monkeyrunner import MonkeyRunner, MonkeyDevice
import commands
import sys
device = MonkeyRunner.waitForConnection()

print "Unlocking"

MonkeyRunner.sleep(2)
device.wake()
MonkeyRunner.sleep(2)
#Unloking device
width = int(device.getProperty('display.width'))
height = int(device.getProperty('display.height'))
device.drag((width/2, height*3/4), (width, height*3/4), 2.0, 50)

print "done"