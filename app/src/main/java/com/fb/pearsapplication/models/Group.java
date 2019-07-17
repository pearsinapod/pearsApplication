package com.fb.pearsapplication.models;

import android.text.format.DateUtils;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.Serializable;

@ParseClassName("Group")
public class Group extends ParseObject implements Serializable {
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_GROUP_NAME = "groupName";
    public static final String KEY_GROUP_IMAGE = "groupImage";
    public static final String KEY_PRIVATE_STATUS = "privateStatus";
    public static final String KEY_USERS = "users";
    public static final String KEY_CREATED_AT = "createdAt";

    public String getDescription(){
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String Description) {
        put(KEY_DESCRIPTION, Description);
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo() {
        String relativeDate = "";
        long dateMillis = getCreatedAt().getTime();
        relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        return relativeDate;
    }

    public ParseFile getImage() {
        return getParseFile(KEY_GROUP_IMAGE);
    }

    public void setImage(ParseFile Image) {
        put(KEY_GROUP_IMAGE, Image);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USERS);
    }

    public void setUser(ParseUser user) {
        put(KEY_USERS, user);
    }

    public ParseFile getGroupName () {
        return getParseFile(KEY_GROUP_NAME);
    }

    public void setGroupName () {
        // TODO    put(KEY_GROUP_NAME, privateStatus);
    }

    public ParseFile getPrivateStatus() {
        return getParseFile(KEY_PRIVATE_STATUS);
    }

    public void setPrivateStatus () {
        // TODO   put(KEY_PRIVATE_STATUS, privateStatus);
    }


    public static class Query extends ParseQuery<Group> {
        public Query() {
            super(Group.class);
        }

        public Query getTop() {
            setLimit(20);
            return this;
        }

        public Query withUser() {
            include("user");
            return this;
        }
    }
}


