package com.fb.pearsapplication.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fb.pearsapplication.EndlessRecyclerViewScrollListener;
import com.fb.pearsapplication.R;
import com.fb.pearsapplication.adapters.groupsAdapter;
import com.fb.pearsapplication.models.Group;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

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

        // create the data source
        mGroups = new ArrayList<>();
        // create the adapter
        adapter = new groupsAdapter(getContext(), mGroups);
        // set the adapter on the recycler view
        rvGroups.setAdapter(adapter);
        // set the layout manager on the recycler view
        rvGroups.setLayoutManager(new LinearLayoutManager(getContext()));


        // Configure the RecyclerView
        RecyclerView rvGroups = (RecyclerView) view.findViewById(R.id.rvGroups);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvGroups.setLayoutManager(linearLayoutManager);
        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
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


    public void loadNextDataFromApi(int offset) {
        queryGroups();

    }


    protected void queryGroups() {
        ParseQuery<Group> groupQuery = new ParseQuery<Group>(Group.class);
        groupQuery.include(Group.KEY_USERS);
        groupQuery.setLimit(20);
        groupQuery.addDescendingOrder(Group.KEY_CREATED_AT);
        if (mGroups.size()>0){
            Group.whereLessThan(Group.KEY_CREATED_AT, mGroups.get(mGroups.size()-1).getCreatedAt());
        }
        groupQuery.findInBackground(new FindCallback<Group>() {
            @Override
            public void done(List<Group> objects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error with query");
                    e.printStackTrace();
                    return;
                }
                mGroups.addAll(objects);
                adapter.notifyDataSetChanged();
                for (int i = 0; i < objects.size(); i++) {
                    Log.d(TAG, "Group: " + objects.get(i).getDescription() + ", username: " + objects.get(i).getUser().getUsername());
                }
            }
        });
    }
}
