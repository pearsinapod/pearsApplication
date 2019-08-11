package com.fb.pearsapplication.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.fb.pearsapplication.R;
import com.fb.pearsapplication.adapters.exploreAdapter;
import com.fb.pearsapplication.models.Group;
import com.fb.pearsapplication.models.GroupUserRelation;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class exploreFragment extends Fragment {

    private ArrayList<Group> exploreGroups;
    private exploreAdapter exploreArrayAdapter;
    SwipeFlingAdapterView flingContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        exploreGroups = new ArrayList<>();
        exploreArrayAdapter = new exploreAdapter(getContext(), R.layout.item_explore_group, exploreGroups);
        flingContainer = view.findViewById(R.id.frame);
        flingContainer.setAdapter(exploreArrayAdapter);

        setUpExploreFlingContainer();
        setOnClickListenerFling();
        addGroupNames();
    }

    public void setUpExploreFlingContainer(){
        flingContainer.setBackgroundColor(0000);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                exploreGroups.remove(0);
                exploreArrayAdapter.notifyDataSetChanged();
                Log.d("Explore Fragment:", "Removed Object from Adapter");
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //If you want to use it just cast it (String) dataObject
                Log.d("Explore Fragment:", "Left");
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Log.d("Explore Fragment:", "Right");
                final GroupUserRelation GURQuery = ChildAddFragment.addUserToGroup(ParseUser.getCurrentUser(), (Group)dataObject);
                GURQuery.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.d("User joining:", "added successfully");
                        } else {
                            e.printStackTrace();
                        }
                    }
                });

            }
            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                addGroupNames();
            }
            @Override
            public void onScroll(float scrollProgressPercent) {
            }
        });

    }

    public void setOnClickListenerFling(){
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Log.d("Explore Fragment:", "Clicked");
            }
        });
    }

    public void addGroupNames(){
        exploreGroups.clear();
        exploreArrayAdapter.notifyDataSetChanged();
        final ParseQuery<Group> groupQuery = new ParseQuery<Group>(Group.class);
        ArrayList currentUser = new ArrayList(); //will change to make more efficient
        currentUser.add(ParseUser.getCurrentUser());
        groupQuery.whereNotContainedIn(Group.KEY_USERS, currentUser);
        groupQuery.addDescendingOrder(Group.KEY_CREATED_AT);
        groupQuery.findInBackground(new FindCallback<Group>() {
            @Override
            public void done(List<Group> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        Group group = objects.get(i);
                        if(!group.getUsers().contains(ParseUser.getCurrentUser())) {
                            exploreGroups.add(group);
                            exploreArrayAdapter.notifyDataSetChanged();
                        }
                    }

                }
                else{
                    Log.d("Explore Fragment","Loading items failed");
                }
            }
        });
    }

}

