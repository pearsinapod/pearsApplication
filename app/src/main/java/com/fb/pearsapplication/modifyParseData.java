package com.fb.pearsapplication;

import android.util.Log;

import com.fb.pearsapplication.models.Group;
import com.fb.pearsapplication.models.GroupUserRelation;
import com.fb.pearsapplication.models.Hobby;
import com.fb.pearsapplication.models.Pear;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class modifyParseData {

    // makes all group users empty
    public void clearGroupUsers(){
        final ParseQuery<Group> groupQuery = new ParseQuery<Group>(Group.class);
        groupQuery.findInBackground(new FindCallback<Group>() {
            @Override
            public void done(List<Group> objects, ParseException e) {
                ArrayList empty = new ArrayList();
                for (int g = 0 ; g<objects.size();g++) {
                    Group group = objects.get(g);
                    group.setUsers(empty);
                    group.saveInBackground();
                    Log.d("Modified group users within group "+(g+1) +" out of " + objects.size(),"done");
                }
            }
        });
    }

    // clears ALL existing Group User Relations
    public void clearGroupUserRelations(){
        final ParseQuery<GroupUserRelation> GUR = new ParseQuery<GroupUserRelation>(GroupUserRelation.class);
        GUR.findInBackground(new FindCallback<GroupUserRelation>() {
            @Override
            public void done(List<GroupUserRelation> objects, ParseException e) {
                for (int r = 0 ; r<objects.size();r++) {
                    GroupUserRelation relation = objects.get(r);
                    relation.deleteInBackground();
                    Log.d("Cleared Group User Relation " + (r+1) +" out of " + objects.size(),"done");
                }
            }
        });
    }
    // clears ALL existing Pears

    public void clearPears(){
        final ParseQuery<Pear> pearQuery = new ParseQuery<Pear>(Pear.class);
        pearQuery.findInBackground(new FindCallback<Pear>() {
            @Override
            public void done(List<Pear> objects, ParseException e) {
                for (int p = 0 ; p<objects.size();p++) {
                    Pear pear = objects.get(p);
                    pear.deleteInBackground();
                    Log.d("Cleared Pear "+(p+1) +" out of " + objects.size(),"done");
                }
            }
        });
    }

    // clears ALL existing Hobbies
    public void clearHobbies(){
        final ParseQuery<Hobby> hobbyQuery = new ParseQuery<Hobby>(Hobby.class);
        hobbyQuery.findInBackground(new FindCallback<Hobby>() {
            @Override
            public void done(List<Hobby> objects, ParseException e) {
                for (int h=0; h<objects.size(); h++){
                    Hobby hobby = objects.get(h);
                    hobby.deleteInBackground();
                    Log.d("Cleared Hobby "+(h+1) +" out of " + objects.size(),"done");

                }
            }
        });
    }

    // deletes groups that have descriptions with character count greater than 200, names with character count count greater than 30,
    // and groups that have no name and/or no description
    public void deleteGroupsWithoutReq(){
        Log.d("Deleting groups w/o req", "entered");
        final ParseQuery<Group> groupQuery = new ParseQuery<Group>(Group.class);
        groupQuery.findInBackground(new FindCallback<Group>() {
            @Override
            public void done(List<Group> objects, ParseException e) {
                for (int g = 0 ; g<objects.size();g++) {
                    Group group = objects.get(g);
                    String group_name = group.getGroupName();
                    String group_description = group.getDescription();
                    String[] empty_array = new String[0];
                    possibleDeletingGroup(group, empty_array, empty_array, group_name, group_description);

                }
            }
        });
        Log.d("Deleting groups w/o req", "done");
    }

    public void possibleDeletingGroup(Group group, String[] arrName, String[] arrDescription, String name, String description){
        if (name==null && description==null){
            Log.d("Name & Desc. is null: ",  "deleted");
            group.deleteInBackground();
            return;
        }
        else if( name==null){
            Log.d("Name is null: ",  "deleted");
            group.deleteInBackground();
            return;
        }
        else if(description==null){
            Log.d("Description is null: ",  "deleted");
            group.deleteInBackground();
            return;
        }
        arrName = name.split("\\s+");
        arrDescription = description.split("\\s+");

        if ((arrName.length==0 || name.length()==0) && (arrDescription.length==0 ||description.length()==0)){
            Log.d("Has No Name or Desc.: ",  group.getGroupName() + " d: "+group.getDescription());
            group.deleteInBackground();
        }
        else if (arrName.length==0 || name.length()==0){
            Log.d("Has No Name: ",  group.getGroupName() + " d: "+group.getDescription());
            group.deleteInBackground();
        }

        else if(arrDescription.length==0 ||description.length()==0){
            Log.d("Has No Description: ",  group.getGroupName() + " d: "+group.getDescription());
            group.deleteInBackground();

        }
        else if(description.length()>200 && name.length()>30){
            Log.d("Name && Desc too big: ",  group.getGroupName() + " d: "+group.getDescription());
            group.deleteInBackground();
        }
        else if (name.length()>30) {
            Log.d("Name too big: ",  group.getGroupName() + " d: "+group.getDescription());
            group.deleteInBackground();

        }
        else if(description.length()>200){
            Log.d("Description too big: ",  group.getGroupName() + " d: "+group.getDescription());
            group.deleteInBackground();
        }
    }


}
