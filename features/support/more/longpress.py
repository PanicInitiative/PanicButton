from com.android.monkeyrunner import MonkeyRunner, MonkeyDevice
import commands
import sys
device = MonkeyRunner.waitForConnection()

print "Long press"

#Unloking device
width = int(device.getProperty('display.width'))
height = int(device.getProperty('display.height'))


device.drag((width/2, height*2/3), (width/2, height*2/3), 4, 1)

print "done"