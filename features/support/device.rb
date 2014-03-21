class Device
  extend Calabash::Android::Operations

  def self.adb_command(command)
    cmd = "#{default_device.adb_command} #{command}"
    system(cmd)
  end

  def self.install_development_app
    fullStr = ENV['APP_PATH']
    justHome = fullStr[0, fullStr.rindex('target') - 1]
    default_device.install_app("#{justHome}/features/support/Development.apk")
  end

  def self.open_dev_app_connection_settings
    adb_command("shell am start -n com.android.development/.Connectivity")
  end

end