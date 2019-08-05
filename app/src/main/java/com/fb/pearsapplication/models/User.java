package com.fb.pearsapplication.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("User")
public class User extends ParseObject {
    public static final String KEY_OBJECT_ID = "objectId";

    public String getObjectId() {
        return getString(KEY_OBJECT_ID);
    }



}

