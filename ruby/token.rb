#!/usr/bin/env ruby
#
# IdeaScale Single Sign-On: Multipass-Compatible Token-Based Authentication
# Ruby Implementation
#
# Help Article:
# http://support.ideascale.com/kb/ideascale-setup/single-sign-on-multipass-token-based
# Copyright 2011 IdeaScale, LLC
#

require 'openssl'
require 'rubygems'
require 'json'
require 'cgi'
require 'base64'

require 'ezcrypto'
require 'multipass'

# Example Usage
if __FILE__ == $0
  # Replace api_key and app_key with your Community's API Key and Site Key
  # The keys below are for this community: http://multipass.ideascale.com/
  # Do not share your keys - the keys below are for testing purposes only.
  # See: http://support.ideascale.com/kb/ideascale-setup/single-sign-on-multipass-token-based
  APP_KEY = "12849"
  API_KEY = "0d2c15da-b36f-4a9c-8f44-45d2669c3013-05e1fb36-54aa-44fc-888e-93eb95811e2e"
  
  # MultiPass example
  mp = MultiPass.new(APP_KEY,API_KEY)
  mpenc = mp.encode(:email=>"ruby@domain.com",:name=>"Ruby Example",:expires=>"2099-02-24T19:55:31.111-08:00")
  token = CGI.escape(mpenc)

  mpdec = mp.decode(mpenc)
  puts "Multipass Token Encoded: #{mpenc}"
  puts "Multipass Token Decoded: #{mpdec}"

  # Example Login URL (below). Use a similar URL in your website.
  # Your URL: http://yourcommunity.ideascale.com/a/panel.do?multipass=#{token}
  puts "http://multipass.ideascale.com/a/panel.do?multipass=#{token}"
  
end