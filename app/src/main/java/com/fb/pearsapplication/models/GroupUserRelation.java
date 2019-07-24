package com.fb.pearsapplication.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import java.util.Date;

@ParseClassName("GroupUserRelation")
public class GroupUserRelation extends ParseObject {
    public Date getDate() {
        return getDate("createdAt");
    }
}