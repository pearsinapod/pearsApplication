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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fb.pearsapplication.R;
import com.fb.pearsapplication.adapters.exploreAdapter;
import com.fb.pearsapplication.models.Group;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

public class exploreFragment extends Fragment {

    protected ArrayList<Group> exploreGroups;
    protected exploreAdapter eAdapter;
    private RecyclerView rvExploreGroups;
    private SwipeRefreshLayout swipeContainer;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rvExploreGroups = view.findViewById(R.id.rvExploreGroups);
        exploreGroups = new ArrayList<>();
        eAdapter = new exploreAdapter(exploreGroups);
        rvExploreGroups.setAdapter(eAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvExploreGroups.setLayoutManager(linearLayoutManager);

        swipeContainer = view.findViewById(R.id.swipeContainer);


        showGroups();
        setUpSwipeContainer();
    }

    public void setUpSwipeContainer(){
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                eAdapter.clear();
                exploreGroups.clear();
                showGroups();
                swipeContainer.setRefreshing(false);

            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


    }

    protected void showGroups(){
        Group.Query groupsQuery = new Group.Query();
        groupsQuery.getTop().withUser();
        groupsQuery.addDescendingOrder(Group.KEY_CREATED_AT);
        groupsQuery.findInBackground(new FindCallback<Group>() {
            @Override
            public void done(List<Group> objects, ParseException e) {
                if (e == null){
                    exploreGroups.addAll(objects);
                    eAdapter.notifyDataSetChanged();
                }
                else{
                    Log.d("Explore Fragment","Loading items failed");
                }
            }
        });
    }

}
