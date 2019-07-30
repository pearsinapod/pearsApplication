package com.fb.pearsapplication;

import com.fb.pearsapplication.models.Group;
import com.fb.pearsapplication.models.Hobby;
import com.fb.pearsapplication.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class apioriAlgorithm {
    static int size;

    public static void populatingHobbies() {
        ParseQuery<Group> groups = new ParseQuery<Group>(Group.class);
        groups.findInBackground(new FindCallback<Group>() {
            @Override
            public void done(List<Group> objects, ParseException e) {
                if (e==null){
                    Group[] groups = new Group[objects.size()];//
                    List<List<Group>> groupSubsets = Powerset.subsets(objects.toArray(groups));//
                    for (int i = 0; i < groupSubsets.size(); i++) {
                        ArrayList<Group> subset = (ArrayList) groupSubsets.get(i);
                        Hobby newHobby = new Hobby();
                        newHobby.newSubset(subset);
                        countingHobbies(newHobby, subset);
                        calculatingThreshold(newHobby, 0);
                        newHobby.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e!=null){
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
                else{
                    e.printStackTrace();
                }
            }
        });
    }

    public static void countingHobbies(Hobby hobby, ArrayList<Group> subset){
        int occurences_count=0;
        for (int group = 0 ; group<subset.size() ;group++){
            occurences_count = subset.get(group).getUsers().size();
        }
        hobby.setOccurences(occurences_count);
        //do we need to save in background twice?

    }

    public static void setSize(int s){
        size= s;
    }

    public static int getHobbiesSize(){
        final ParseQuery<Hobby> hobbies = new ParseQuery<Hobby>(Hobby.class);
        hobbies.findInBackground(new FindCallback<Hobby>() {
            @Override
            public void done(List<Hobby> objects, ParseException e) {
                setSize(objects.size());
            }

        });
        return size;
    }

    public static void calculatingThreshold(Hobby hobby, final int minThreshold){ // min threshold in percentage form
        double hobbyThreshold= (hobby.getOccurences()/getHobbiesSize())*100;
        if (hobbyThreshold< minThreshold){
            hobby.setThreshold(-1); // you do not want this!
        }
        else{
            hobby.setThreshold(hobby.getOccurences()/size);
        }
    }

    public static void userReccomendations (User user){
        ArrayList arrayListUser = new ArrayList();
        arrayListUser.add(user);
        ParseQuery<Group> groupsQuery = new ParseQuery<Group>(Group.class);
        groupsQuery.whereContainedIn(Group.KEY_USERS, arrayListUser);
        groupsQuery.findInBackground(new FindCallback<Group>() {
            @Override
            public void done(List<Group> objects, ParseException e) {
                Group[] groups = new Group[objects.size()];//
                List<List<Group>> groupSubsets = Powerset.subsets(objects.toArray(groups));//
            }
        });

    }

}
