package com.fb.pearsapplication;

import android.app.Application;

import com.fb.pearsapplication.models.Group;
import com.fb.pearsapplication.models.GroupUserRelation;
import com.fb.pearsapplication.models.Hobby;
import com.fb.pearsapplication.models.Pear;
import com.fb.pearsapplication.models.PearMessage;
import com.fb.pearsapplication.models.User;
import com.fb.pearsapplication.models.Question;
import com.fb.pearsapplication.models.UserQuestion;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;


public class ParseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Group.class);
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(PearMessage.class);
        ParseObject.registerSubclass(Pear.class);
        ParseObject.registerSubclass(GroupUserRelation.class);
        ParseObject.registerSubclass(Hobby.class);
        ParseObject.registerSubclass(Question.class);
        ParseObject.registerSubclass(UserQuestion.class);


        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("fbu-pears")
                .clientKey("AngelicaAarushiJaleel")
                .server("http://fbu-pears.herokuapp.com/parse")
                .build();

        Parse.initialize(configuration);
        ParseFacebookUtils.initialize(this);


    }




}
