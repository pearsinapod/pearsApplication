package com.fb.pearsapplication;

import java.util.ArrayList;

public class Tuple<X, Y> {
    public  String user;
    public  String group;
    public String subset;
    public ArrayList lol;

    public Tuple(String x, String y) {
        this.user = x;
        this.group = y;
    }

    public String getX(){
        return user;
    }
    public String getY(){
        return group;
    }
}