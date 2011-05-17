package com.acme.sso;

import com.ideascale.multipass.IdeaScaleMultipassTokenFactory;
import com.ideascale.multipass.IdeaScaleMultipassTokenFactoryWrapper;

import java.util.Date;

/**
 * User: jeremy
 * Date: 5/3/11
 * Time: 11:34 AM
 */
public class SsoExample {
    
    // Replace api_key and app_key with your Community's API Key and Site Key
    // The keys below are for this community: http://multipass.ideascale.com/
    // Do not share your keys - the keys below are for testing purposes only.
    // See: http://support.ideascale.com/kb/ideascale-setup/single-sign-on-multipass-token-based
    public static void main(String[] args) {
        String api_key = "0d2c15da-b36f-4a9c-8f44-45d2669c3013-05e1fb36-54aa-44fc-888e-93eb95811e2e";
        String app_key = "12849";
        IdeaScaleMultipassTokenFactory multipass = new IdeaScaleMultipassTokenFactory(app_key,api_key);

        long _1yearmills = 1000L * 60L * 60L * 24L * 365L;
        Date date = new Date(System.currentTimeMillis()+_1yearmills);
        String token = multipass.token("java@domain.com","Java Example",date);

        // Example Login URL (below). Use a similar URL in your website.
        // Your URL: http://yourcommunity.ideascale.com/a/panel.do?multipass=#{token}
        System.out.println(url(token));

        // Super super simple example (for systems like ColdFusion which require no-arg constructor)
        IdeaScaleMultipassTokenFactoryWrapper wrapper = new IdeaScaleMultipassTokenFactoryWrapper();
        token = wrapper.token(app_key,api_key,"java@domain.com","Java Example",date);
        System.out.println(url(token));

        // And a call to the sanity-check method on the wrapper... this should be called to help
        // diagnose problems if you can't get any token to be generated.
        System.out.println(wrapper.example());
    }

    private static String url(String token) {
        return "http://multipass.ideascale.com/a/panel.do?multipass="+token;
    }
}
