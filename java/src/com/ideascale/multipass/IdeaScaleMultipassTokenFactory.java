package com.ideascale.multipass;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
    
    public String token(String email, String name) {
        return token(email,name,null);
    }
    
    public interface Populator {
        public void populate(JSONObject json);
    }
    
    public String token(String email, String name, Date expiration) {
        return token(email,name,expiration,null,null);
    }

    public String token(String email, String name, Date expiration, Map attributes) {
        return token(email,name,expiration,attributes,null);
    }

    public String token(String email, String name, Date expiration, Map attributes, Populator populator) {
        JSONObject json = new JSONObject();
        try {
            json.put("email",email);
            json.put("name",name);
            if (expiration != null) {
                json.put("expires",ISODateTimeFormat.dateTime().print(new DateTime(expiration.getTime())));
            }
            if (attributes != null) {
                JSONObject attributesJSON = new JSONObject();
                for (Iterator i = attributes.keySet().iterator(); i.hasNext();) {
                    Object key = i.next();
                    Object value = attributes.get(key);
                    attributesJSON.put(key.toString(),value);
                }
                json.put("attributes",attributesJSON);
            }
            if (populator != null) {
                populator.populate(json);
            }
            String unencrypted = json.toString();
            System.out.println("Unencrypted JSON: " + unencrypted);
            String token = encode(json);
            return token;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
