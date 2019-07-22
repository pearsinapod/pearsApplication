package com.fb.pearsapplication.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.Serializable;

@ParseClassName("Pear")
public class Pear extends ParseObject implements Serializable {
    public static final String KEY_USER_1 = "user1";
    public static final String KEY_USER_2 = "user2";
    public static final String KEY_GROUP = "group";

    public ParseUser getUser1() {
        return getParseUser(KEY_USER_1);
    }

    public void setUser1(ParseUser user) {
        put(KEY_USER_1, user);
    }

    public ParseUser getUser2() {
        return getParseUser(KEY_USER_2);
    }

    public void setUser2(ParseUser user) {
        put(KEY_USER_2, user);
    }

    public Group getGroup() {
        return (Group) getParseObject(KEY_GROUP);
    }

    public void setGroup(Group group) {
        put(KEY_GROUP, group);
    }
}
