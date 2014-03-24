class Device
  extend Calabash::Android::Operations

  def self.adb_command(command)
    cmd = "#{default_device.adb_command} #{command}"
    system(cmd)
  end

  def self.cmd_command(command)
    return system("#{command}")
  end

  def self.get_more_dir
    fullStr = ENV['APP_PATH']
    justHome = fullStr[0, fullStr.rindex('target') - 1] + "/features/support/more"
  end

  def self.install_development_app
    moreDir = get_more_dir()
    default_device.install_app("#{moreDir}/Development.apk")
  end

  # Wifi
  def self.open_dev_app_connection_settings
    adb_command("shell am start -n com.android.development/.Connectivity --activity-no-history")
  end

  # Location
  def self.start_settings
    adb_command("shell am start -a android.settings.LOCATION_SOURCE_SETTINGS")
  end
end