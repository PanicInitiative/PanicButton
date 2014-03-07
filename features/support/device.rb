class Device
extend Calabash::Android::Operations

  def self.press_back_button
    cmd = "#{default_device.adb_command} shell input keyevent 4"
    system(cmd)
  end

end