package com.fb.pearsapplication.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

@ParseClassName("Question")
public class Question extends ParseObject {
    public static final String KEY_QUESTION = "question";
    public static final String KEY_DATE = "targetDate";

    public String getQuestion() {
        return getString(KEY_QUESTION);
    }

    public Date getTargetDate() {
        return getDate("targetDate");
    }

    public void setQuestion(String question) {
        put(KEY_QUESTION, question);
    }

    public void setTargetDate(Date date) {
        put(KEY_DATE, date);
    }
}
