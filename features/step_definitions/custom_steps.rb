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
  Device.press_back_button
end

Then /^I press home button$/ do
  Device.press_home_button
end

Then /^There is no internet connection$/ do
  Device.install_development_app
end
