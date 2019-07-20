package com.fb.pearsapplication.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.io.Serializable;

@ParseClassName("Messages")
public class PearMessage extends ParseObject implements Serializable {
    public static final String USER_ID_KEY = "userId";
    public static final String BODY_KEY = "body";


    public String getReceiverId() {
        return getString("receiverId");
    }

    public void setReceiverId(String receiverId) {
        put("receiverId", receiverId);
    }

    public String getUserId() {
        return getString(USER_ID_KEY);
    }

    public String getBody() {
        return getString(BODY_KEY);
    }

    public void setUserId(String userId) {
        put(USER_ID_KEY, userId);
    }

    public void setBody(String body) {
        put(BODY_KEY, body);
    }



}
