from com.android.monkeyrunner import MonkeyRunner, MonkeyDevice
import commands
import sys
device = MonkeyRunner.waitForConnection()

print "Clicking"

#Unloking device
width = int(device.getProperty('display.width'))
height = int(device.getProperty('display.height'))

device.touch(width/2, height*2/3 , MonkeyDevice.DOWN_AND_UP)
MonkeyRunner.sleep(0.3)
device.touch(width/2, height*2/3 , MonkeyDevice.DOWN_AND_UP)
MonkeyRunner.sleep(0.3)
device.touch(width/2, height*2/3 , MonkeyDevice.DOWN_AND_UP)
MonkeyRunner.sleep(0.3)
device.touch(width/2, height*2/3 , MonkeyDevice.DOWN_AND_UP)
MonkeyRunner.sleep(0.3)
device.touch(width/2, height*2/3 , MonkeyDevice.DOWN_AND_UP)
MonkeyRunner.sleep(0.3)
device.touch(width/2, height*2/3 , MonkeyDevice.DOWN_AND_UP)

print "done"