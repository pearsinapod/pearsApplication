package com.fb.pearsapplication.fragments;

import android.util.Log;

import com.fb.pearsapplication.models.Group;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class exploreFragmentChildWhenSearch extends exploreFragmentChildNoSearch {
    @Override
    protected ParseQuery getQuery(){
        Group.Query groupsQuery = new Group.Query();
        groupsQuery.addDescendingOrder(Group.KEY_CREATED_AT);
        return groupsQuery;
    }

    @Override
    protected void updatingListAdapter(ParseQuery groupsQuery) {
        exploreGroups.clear();
        eAdapter.notifyDataSetChanged();
        groupsQuery.findInBackground(new FindCallback<Group>() {
            @Override
            public void done(List<Group> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        Group group = objects.get(i);
                        String lowercaseGroupName = group.getGroupName().toLowerCase();
                        if (lowercaseGroupName.length() >= exploreFragmentParent.lengthSearchedText() && lowercaseGroupName.substring(0, exploreFragmentParent.lengthSearchedText()).equals(exploreFragmentParent.getSearchedText())) {
                            exploreGroups.add(group);
                            eAdapter.notifyDataSetChanged();
                        }
                    }
                    exploreFragmentParent.etSearch.setText("");
                }
                else{
                    Log.d("Explore Fragment", "Loading items failed");
                }
            }
        });
    }
}

/* TODO- goals:
        Problem 1: onSwipeRefresh will go back to initial query
        Problem 2: potential hide keyboard error?
        Problem 3: should explicitly say there are no groups when the user searches something that is not in our database
        Goal: infinite scrolling
 *//*
}




*/


