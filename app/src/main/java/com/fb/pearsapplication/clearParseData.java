package com.fb.pearsapplication;

import android.util.Log;

import com.fb.pearsapplication.models.Group;
import com.fb.pearsapplication.models.GroupUserRelation;
import com.fb.pearsapplication.models.Pear;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class clearParseData {

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
}
