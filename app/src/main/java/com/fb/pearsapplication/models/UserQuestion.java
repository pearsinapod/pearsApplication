package com.fb.pearsapplication.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("UserQuestion")
public class UserQuestion extends ParseObject {
    public static final String KEY_QUESTION = "question";
    public static final String KEY_USER = "user";
    public static final String KEY_ANSWER = "answer";
    public static final String KEY_DATE = "targetDate";

    public Question getQuestion() {
        return (Question) getParseObject(KEY_QUESTION);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public String getAnswer() {
        return getString(KEY_ANSWER);
    }

    public Date getDate() {
        return getDate(KEY_DATE);
    }

    public void setQuestion(Question question) {
        put(KEY_QUESTION, question);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public void setAnswer(String answer) {
        put(KEY_ANSWER, answer);
    }

    public void setDate(Date date) {
        put(KEY_DATE, date);
    }


}
