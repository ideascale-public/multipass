package com.ideascale.multipass;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * IdeaScale Single Sign-On: Multipass-Compatible Token-Based Authentication
 * Java Implementation
 *
 * Help Article:
 * http://support.ideascale.com/kb/ideascale-setup/single-sign-on-multipass-token-based
 * Copyright 2011 IdeaScale, LLC
 *
 * User: jeremy
 * Date: 2/25/11
 * Time: 2:52 PM
 */
public class IdeaScaleMultipassTokenFactory extends MultipassTokenFactoryBase {

    public IdeaScaleMultipassTokenFactory(String siteKey, String apiKey) {
        super(siteKey, apiKey);
    }

    public String token(String email, String name, Date expiration) {
        JSONObject json = new JSONObject();
        try {
            json.put("email",email);
            json.put("name",name);
            json.put("expires",ISODateTimeFormat.dateTime().print(new DateTime(expiration.getTime())));
            String unencrypted = json.toString();
            System.out.println("Unencrypted JSON: " + unencrypted);
            String token = encode(json);
            return token;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
