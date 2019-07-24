package com.fb.pearsapplication.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ParseClassName("Pear")
public class Pear extends ParseObject implements Serializable {
    public static final String KEY_USER = "user";
    public static final String KEY_OTHER_USER = "otherUser";
    public static final String KEY_GROUP = "group";
    public static final String KEY_USERS = "users";

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public ParseUser getOtherUser() {
        return getParseUser(KEY_OTHER_USER);
    }

    public void setOtherUser(ParseUser user) {
        put(KEY_OTHER_USER, user);
    }

    public Group getGroup() {
        return (Group) getParseObject(KEY_GROUP);
    }

    public void setGroup(Group group) {
        put(KEY_GROUP, group);
    }

    public List getUsers() {
        if (getList(KEY_USERS) == null) {
            setUsers(new ArrayList());
        }
        return (ArrayList) getList(KEY_USERS);
    }

    public void setUsers(ArrayList users) {
        put(KEY_USERS, users);
    }

    @Override
    public boolean equals(Object obj) {
        return ((Pear) obj).getObjectId().equals(getObjectId());
    }
}
