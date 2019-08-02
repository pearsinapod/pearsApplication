package com.fb.pearsapplication;

import android.content.Context;

import com.fb.pearsapplication.models.Group;
import com.fb.pearsapplication.models.Hobby;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class apioriAlgorithm {
    static int size;
    private Context context;

    /*
        curl -X GET \-H "X-Parse-Application-Id: fbu-pears" \ -H "X-Parse-Master-Key: AngelicaAarushiJaleel" \http://fbu-pears.herokuapp.com/parse/classes/Group

        --output JSONObject.txt
*/

    public static void populatingHobbies() {
        System.out.println("Initiating populating hobbies");
        final ParseQuery<Group> groupQuery = new ParseQuery<Group>(Group.class);
        System.out.println("Parsed groups");
        try {
            groupQuery.find();
            System.out.println("no error!");
        } catch (ParseException e) {
            System.out.println("error!");
            e.printStackTrace();

        }
/*        groups.findInBackground(new FindCallback<Group>() {
            public void done(List<Group> objects, ParseException e) {
                System.out.println("Initiating done");
                if (e==null){
                    System.out.println("e equals null");
                    Group[] groups = new Group[objects.size()];
                    List<List<Group>> groupSubsets = Powerset.subsets(objects.toArray(groups));
                    for (int i = 0; i < groupSubsets.size(); i++) {
                        ArrayList<Group> subset = (ArrayList) groupSubsets.get(i);
                        if (subset.size()>10){
                            System.out.println("this passed"+subset);
                            Hobby newHobby = new Hobby();
                            newHobby.newSubset(subset);
                            countingHobbies(newHobby, subset);
                            calculatingThreshold(newHobby, -1);
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
                }
                else{
                    System.out.println("e does not equal null");
                    e.printStackTrace();
                }
            }
        });*/
    }

    public static void countingHobbies(Hobby hobby, ArrayList<Group> subset){
        int occurences_count=0;
        for (int group = 0 ; group<subset.size() ;group++){
            occurences_count = subset.get(group).getUsers().size();
        }
        System.out.println(subset+ " : "+ occurences_count);
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
        System.out.println("current size"+ " : "+ size);
        return size;
    }

    public static void calculatingThreshold(Hobby hobby, final int minThreshold){ // min threshold in percentage form
        double hobbyThreshold= (hobby.getOccurences()/getHobbiesSize())*100;
        System.out.println(hobby.getSubsets()+ " threshold : "+  hobbyThreshold);
        if (hobbyThreshold< minThreshold){
            hobby.setThreshold(-1); // you do not want this!
        }
        else{
            hobby.setThreshold(hobby.getOccurences()/size);
        }
    }

    public JSONObject fileToJSON(String filename){
        String filePath = "/Users/angcast/AndroidStudioProjects/pearsApplication/app/src/main/assets/"+filename;
        File file = new File(filePath);

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String st;
        try {
            while ((st = br.readLine()) != null) {
                JSONParser parser = new JSONParser();
                JSONObject json = null;
                try {
                    json = (JSONObject) parser.parse(st);
                } catch (org.json.simple.parser.ParseException e) {
                    e.printStackTrace();
                }
                return json;
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static void main(String args[]){

        apioriAlgorithm a = new apioriAlgorithm();
        a.fileToJSON("JSONObject.json");
    }

/**/
/*    public static void userReccomendations (User user){
        ArrayList arrayListUser = new ArrayList();
        arrayListUser.add(user);
        ParseQuery<Group> groupsQuery = new ParseQuery<Group>(Group.class);
        groupsQuery.whereContainedIn(Group.KEY_USERS, arrayListUser);
        groupsQuery.findInBackground(new FindCallback<Group>() {
            @Override
            public void done(List<Group> objects, ParseException e) {
                Group[] groups = new Group[objects.size()];
                List<List<Group>> groupSubsets = Powerset.subsets(objects.toArray(groups));

            }
        });

    }*/

}
