class Device
  extend Calabash::Android::Operations

  def self.adb_command(command)
    cmd = "#{default_device.adb_command} #{command}"
    system(cmd)

  end

  def self.press_back_button
    adb_command("shell input keyevent 4")
  end

  def self.press_home_button
    adb_command("shell input keyevent 3")
  end

  def self.install_development_app
    fullStr = ENV['APP_PATH']
    justHome = fullStr[0, fullStr.rindex('target') - 1]
    default_device.install_app("#{justHome}/features/support/Development.apk")
  end

  def self.start_dev_app
    #adb shell am start -n com.google.android.contacts/.ContactsActivity
  end

end