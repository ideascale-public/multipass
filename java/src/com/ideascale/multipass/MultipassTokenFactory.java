package com.ideascale.multipass;

import org.json.JSONObject;

import java.util.Date;

public interface MultipassTokenFactory {
    String encode(JSONObject json);
    JSONObject decode(String token);
    String time(Date time);
}
