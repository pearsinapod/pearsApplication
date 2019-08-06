package com.fb.pearsapplication.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fb.pearsapplication.EndlessRecyclerViewScrollListener;
import com.fb.pearsapplication.R;
import com.fb.pearsapplication.adapters.groupsAdapter;
import com.fb.pearsapplication.models.Group;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static com.parse.Parse.getApplicationContext;

public class groupFragment extends Fragment {

    public static final String TAG = "GroupFragment";

    private RecyclerView rvGroups;
    protected groupsAdapter adapter;
    protected List<Group> mGroups;
    private EndlessRecyclerViewScrollListener scrollListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_group, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rvGroups = view.findViewById(R.id.rvGroups);

        setParameters();

        queryGroups();

        recyclerViewConfig(view);

    }

    public void loadNextDataFromApi(int offset) {
        queryGroups();

    }

    public void recyclerViewConfig(View view) {
        // Configure the RecyclerView
        RecyclerView rvGroups = (RecyclerView) view.findViewById(R.id.rvGroups);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        rvGroups.setLayoutManager(gridLayoutManager);
        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);
            }
        };
        // Adds the scroll listener to RecyclerView
        rvGroups.addOnScrollListener(scrollListener);
    }


    private void setParameters() {
        // create the data source
        mGroups = new ArrayList<>();
        // create the adapter
        adapter = new groupsAdapter(getContext(), mGroups);
        // set the adapter on the recycler view
        rvGroups.setAdapter(adapter);
        // set the layout manager on the recycler view
        rvGroups.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    protected void queryGroups() {
        ParseQuery<Group> groupQuery = new ParseQuery<Group>(Group.class);
        ArrayList Users = new ArrayList<>();
        Users.add(ParseUser.getCurrentUser());
        groupQuery.include(Group.KEY_USERS);
        groupQuery.setLimit(20);
        groupQuery.whereContainedIn(Group.KEY_USERS, Users);
//        ArrayList<ParseUser> currentUser = new ArrayList<>();
//        currentUser.add(ParseUser.getCurrentUser());
//        groupQuery.whereContainedIn("users", currentUser);
        groupQuery.addDescendingOrder(Group.KEY_CREATED_AT);
        if (mGroups.size()>0){
            groupQuery.whereLessThan(Group.KEY_CREATED_AT, mGroups.get(mGroups.size()-1).getCreatedAt());
        }
        groupQuery.findInBackground(new FindCallback<Group>() {
            @Override
            public void done(List<Group> groups, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error with query");
                    e.printStackTrace();
                    return;
                }
                mGroups.addAll(groups);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
