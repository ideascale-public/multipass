package com.ideascale.multipass;

import java.util.Date;
import java.util.HashMap;

/**
 * User: jeremy
 * Date: 1/19/12
 * Time: 12:54 PM
 */
public class IdeaScaleTokenizer {

    private IdeaScaleMultipassTokenFactory delegate;
    
    private String email;
    private String name;
    private Date expiration;
    private HashMap attributes = new HashMap();

    public static IdeaScaleTokenizer tokenizer(String appKey, String apiKey) {
        IdeaScaleTokenizer tokenizer = new IdeaScaleTokenizer(appKey,apiKey);
        return tokenizer;
    }

    public IdeaScaleTokenizer(String appKey, String apiKey) {
        this(new IdeaScaleMultipassTokenFactory(appKey,apiKey));
    }

    public IdeaScaleTokenizer(IdeaScaleMultipassTokenFactory ideaScaleMultipassTokenFactory) {
        this.delegate = ideaScaleMultipassTokenFactory;
    }
    
    public IdeaScaleTokenizer setEmail(String email) {
        this.email = email;
        return this;
    }
    
    public IdeaScaleTokenizer setName(String name) {
        this.name = name;
        return this;
    }

    public IdeaScaleTokenizer setExpiration(Date expiration) {
        this.expiration = expiration;
        return this;
    }
    
    public IdeaScaleTokenizer addAttrbiute(String key, Object value) {
        attributes.put(key,value);
        return this;
    }
    
    public String token() {
        if (email == null || email.isEmpty()) {
            throw new RuntimeException("Email has not been set");
        }
        if (name == null || name.isEmpty()) {
            throw new RuntimeException("Name has not been set");
        }
        return delegate.token(email,name,expiration,attributes);
    }
}
