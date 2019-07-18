package com.fb.pearsapplication.models;

import android.text.format.DateUtils;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.Serializable;
import java.util.ArrayList;

@ParseClassName("Group")
public class Group extends ParseObject implements Serializable {
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_GROUP_NAME = "groupName";
    public static final String KEY_GROUP_IMAGE = "groupImage";
    public static final String KEY_PRIVATE_STATUS = "privateStatus";
    public static final String KEY_USERS = "users";
    public static final String KEY_CREATED_AT = "createdAt";


    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public String getGroupName() {
        return getString(KEY_GROUP_NAME);
    }

    public void setGroupName(String name) {
        put(KEY_GROUP_NAME, name);
    }

    public ParseFile getGroupImage() {
        return getParseFile(KEY_GROUP_IMAGE);
    }

    public void setImage(ParseFile groupImage) {
        put(KEY_GROUP_IMAGE, groupImage);
    }

    public Boolean getPrivateStatus() {
        return getBoolean(KEY_PRIVATE_STATUS);
    }

    public void setPrivateStatus(Boolean bool) {
        put(KEY_PRIVATE_STATUS, bool);
    }

    public ArrayList getUsers() {
        if (getList(KEY_USERS) == null) {
            put(KEY_USERS, new ArrayList<>());
            saveInBackground();
        }
        return (ArrayList) getList(KEY_USERS);
    }

    public void addUser(ParseUser user) {
        ArrayList users = (ArrayList) getUsers();
        users.add(user);
        put(KEY_USERS, users);
        saveInBackground();
    }

        // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
        public String getRelativeTimeAgo () {
            String relativeDate = "";
            long dateMillis = getCreatedAt().getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
            return relativeDate;
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




