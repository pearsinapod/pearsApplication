package com.fb.pearsapplication.fragments;

import androidx.fragment.app.Fragment;

import com.fb.pearsapplication.models.Group;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;

public class matchProfileFragment extends Fragment {

    public void querySameGroups() {
        ParseQuery<Group> groupQuery = new ParseQuery<Group>(Group.class);
        groupQuery.include(Group.KEY_USERS);
        ArrayList<ParseUser> users = new ArrayList<>();
        users.add(ParseUser.getCurrentUser());

        groupQuery.whereContainsAll("users", users);

    }

}
