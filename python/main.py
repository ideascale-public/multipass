#!/usr/bin/env python
#
# IdeaScale Single Sign-On: Multipass-Compatible Token-Based Authentication
# Google App Engine / Python Implementation
#
# Help Article:
# http://support.ideascale.com/kb/ideascale-setup/single-sign-on-multipass-token-based
# Copyright 2011 IdeaScale, LLC
#
from google.appengine.ext import webapp
from google.appengine.ext.webapp import util
from google.appengine.api import users

from base64 import *

from Crypto.Cipher import AES
import base64
import hashlib
import urllib
import operator
import array
import simplejson as JSON
import sys
import os

class MainHandler(webapp.RequestHandler):
    def get(self):
        user = users.get_current_user()
        if user:
            # EXAMPLE: Multipass JSON Token
            message = {"email":user.email().strip(),"name":user.nickname(),"expires":"2099-02-24T19:55:31.111-08:00"}
            block_size = 16
            mode = AES.MODE_CBC

            # Replace api_key and app_key with your Community's API Key and Site Key
            # The keys below are for this community: http://multipass.ideascale.com/
            # Do not share your keys - the keys below are for testing purposes only.
            # See: http://support.ideascale.com/kb/ideascale-setup/single-sign-on-multipass-token-based
            api_key = "0d2c15da-b36f-4a9c-8f44-45d2669c3013-05e1fb36-54aa-44fc-888e-93eb95811e2e"
            app_key = '12849'

            json = JSON.dumps(message, separators=(',',':'))

            salted = api_key+app_key
            saltedHash = hashlib.sha1(salted).digest()[:16]

            json_bytes = array.array('b', json[0 : len(json)]) 

            pad = block_size - len(json_bytes.tostring()) % block_size
            data = json_bytes.tostring() + pad * chr(pad)
            aes = AES.new(saltedHash, mode)
            encrypted_bytes = aes.encrypt(data)

            b64token = base64.b64encode(encrypted_bytes)
            b64token = re.sub(r'\s+' ,'' ,b64token)
            b64token = re.sub(r'\=+$','' ,b64token)
            b64token = re.sub(r'\+'  ,'-',b64token)
            b64token = re.sub(r'\/'  ,'_',b64token)
            token = urllib.quote(b64token)

            greeting = ("Welcome, %s! (\
                        <a href=\"%s\">sign out</a>,\
                        <a href=\"%s?multipass=%s\">Multipass Example Community</a>)" %
                        (user.nickname()
                        ,users.create_logout_url("/")
                        ,"http://multipass.ideascale.com/a/panel.do"
                        ,token))
            self.response.out.write(
                    "<html><head></head><body>%s</body></html>" % (greeting))
        else:
            greeting = ("<a href=\"%s\">Sign in or register</a>." % users.create_login_url("/"))
            self.response.out.write("<html><body>%s</body></html>" % greeting)

def main():
    application = webapp.WSGIApplication([('/', MainHandler)],debug=True)
    util.run_wsgi_app(application)

if __name__ == '__main__':
    main()
