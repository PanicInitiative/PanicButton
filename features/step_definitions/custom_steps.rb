Then /^I verify "(.*?)" button is "(.*?)"$/ do|button_id,button_status|
  query("button id:'#{button_id}'")[0]["button_status"]
end

Then /^I verify action button is "(.*?)"$/ do|button_status|
  query("button id:'action_button'")[0]["button_status"]
end

Then /^I verify action button text is "(.*?)"$/ do|button_text|
  query("button id:'action_button'")[0]["text"] == button_text
end

Then /^I enter "(.*?)" into contact field (\d+)$/ do |text_to_enter, index|
  query("editText id:'contact_edit_text' index:#{index}", {:setText => text_to_enter})
end

Then /^I press back button$/ do
  Device.adb_command("shell input keyevent 4")
end

Then /^I press home button$/ do
  Device.adb_command("shell input keyevent 3")
end

Then /^I tern off wifi$/ do
  Device.install_development_app
  Device.open_dev_app_connection_settings
  Device.adb_command("shell input keyevent 61")
  Device.adb_command("shell input keyevent 23")
end

Then /^I switch gps$/ do
  Device.cmd_command("monkeyrunner #{Device.get_more_dir}/gpsmonkey.py")
end

Then /^I tern on wifi$/ do
  Device.install_development_app
  Device.open_dev_app_connection_settings
  Device.adb_command("shell input keyevent 23")
end