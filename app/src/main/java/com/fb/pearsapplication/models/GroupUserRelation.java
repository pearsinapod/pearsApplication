package com.fb.pearsapplication.models;
import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("GroupUserRelation")
public class GroupUserRelation extends ParseObject {
    public static final String KEY_USER = "user";
    public static final String KEY_GROUP = "group";
    public static final String KEY_PEAR = "pearRequest";
    public static final String KEY_USER_LOCATION = "userLocation";


    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public Group getGroup() {
        return (Group) getParseObject(KEY_GROUP);
    }

    public boolean getPearRequest() {
        return getBoolean(KEY_PEAR);
    }

    public ParseGeoPoint getUserLocation() {
        return getParseGeoPoint(KEY_USER_LOCATION);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public void setGroup(Group group) {
        put(KEY_GROUP, group);
    }

    public void setPearRequest(boolean request) {
        put(KEY_PEAR, request);
    }

    public void setUserLocation(ParseGeoPoint location) {
        put(KEY_USER_LOCATION, location);
    }
  
    public Date getDate() {
        return getDate("createdAt");
    }
}

