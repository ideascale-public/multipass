package com.ideascale.multipass;

import java.util.Date;

/**
 * User: jeremy
 * Date: 5/6/11
 * Time: 10:13 AM
 */
public class IdeaScaleMultipassTokenFactoryWrapper {

    public String token(String appKey, String apiKey, String email, String name) {
        long _1daymills = 1000L * 60L * 60L * 24L;
        Date expiration = new Date(System.currentTimeMillis()+_1daymills);
        return token(appKey,apiKey,email,name,expiration);
    }

    public String token(String appKey, String apiKey, String email, String name, Date expiration) {
        IdeaScaleMultipassTokenFactory multipass = new IdeaScaleMultipassTokenFactory(appKey,apiKey);
        return multipass.token(email,name,expiration);
    }

    public String example() {
        String appKey = "12849";
        String apiKey = "0d2c15da-b36f-4a9c-8f44-45d2669c3013-05e1fb36-54aa-44fc-888e-93eb95811e2e";
        String token = token(appKey,apiKey,"java@domain.com","Java Example");
        return "http://multipass.ideascale.com/a/panel.do?multipass=" + token;
    }
}
