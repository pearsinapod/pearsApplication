package com.fb.pearsapplication.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.Serializable;
import java.util.Date;

@ParseClassName("Messages")
public class PearMessage extends ParseObject implements Serializable {
    public static final int STATUS_SENDING = 0;
    public static final int STATUS_SENT = 1;
    public static final int STATUS_FAILED = 2;
    public static ParseUser user;
    private String body;
    private int status = STATUS_SENT;
    private Date date;
    private String sender;


    public PearMessage(String body, Date createdAt, String messageAuthor) {

        this.body = body;
        this.date = createdAt;
        this.sender = messageAuthor;
    }

    public PearMessage(){
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isSent() {
        return user.getUsername().equals(sender);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date createdAt) {
        this.date = createdAt;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String messageAuthor){
        this.sender = messageAuthor;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
