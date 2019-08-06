package com.fb.pearsapplication.models;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.Date;

@ParseClassName("Question")
public class Question extends ParseObject {
    public static final String KEY_QUESTION = "question";
    public static final String KEY_DATE = "targetDate";

    public String getQuestion() {
        String question = "";
        try {
            question = fetchIfNeeded().getString("question");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return question;
    }

    public Date getTargetDate() {
        Date date = new Date();
        try {
            date = fetchIfNeeded().getDate("targetDate");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public void setQuestion(String question) {
        put(KEY_QUESTION, question);
    }

    public void setTargetDate(Date date) {
        put(KEY_DATE, date);
    }
}
