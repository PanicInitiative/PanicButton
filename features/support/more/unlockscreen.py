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
device.drag((width/2, height*2/3), (width/2, height*1/3), 2.0, 300)

print "done"