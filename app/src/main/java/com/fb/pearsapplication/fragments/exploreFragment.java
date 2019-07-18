package com.fb.pearsapplication.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

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
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class exploreFragment extends Fragment {

    protected ArrayList<Group> exploreGroups;
    protected exploreAdapter eAdapter;
    private RecyclerView rvExploreGroups;
    private SwipeRefreshLayout swipeContainer;
    EditText etSearch;
    Button btnSearchSubmit;


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
        etSearch = view.findViewById(R.id.etSearch);
        btnSearchSubmit = view.findViewById(R.id.btnSearchSubmit);


        updatingListAdapter(initialQuery());
        setUpOnSubmitListener();
        setUpSwipeContainer();
    }

    public void setUpSwipeContainer(){
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                eAdapter.clear();
                exploreGroups.clear();
                updatingListAdapter(initialQuery());
                swipeContainer.setRefreshing(false);

            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    public void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public String getSearchedText(){
        return etSearch.getText().toString();
    }

    protected ParseQuery initialQuery(){
        Group.Query groupsQuery = new Group.Query();
        groupsQuery.getTop();
        groupsQuery.addDescendingOrder(Group.KEY_CREATED_AT);
        updatingListAdapter(groupsQuery);
        return groupsQuery;
    }

    protected ParseQuery specificQuery(){
        Group.Query groupsQuery = new Group.Query();
        // can be used to alphabatize : groupsQuery.addAscendingOrder(Group.KEY_GROUP_NAME);
       groupsQuery.whereContains(Group.KEY_GROUP_NAME, getSearchedText());
       return groupsQuery;

    }

    public void setUpOnSubmitListener(){
        btnSearchSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(getActivity());
                Log.d("Search Submit","Clicked");
                updatingListAdapter(specificQuery());
                etSearch.setText("");

            }
        });

    }

    protected void updatingListAdapter(ParseQuery groupsQuery){
        exploreGroups.clear();
        groupsQuery.findInBackground(new FindCallback<Group>() {
            @Override
            public void done(List<Group> objects, ParseException e) {
                if (e == null){
                    for (int i =0; i<objects.size();i++){
                        exploreGroups.add(objects.get(i));
                        Log.d("Position "+i+":",objects.get(i).getGroupName());
                    }
                    eAdapter.notifyDataSetChanged();
                }
                else{
                    Log.d("Explore Fragment","Loading items failed");
                }
            }
        });
    }
    /*TODO:
        problem: onRefresh will go back to initialQuery
        problem: click only submit without anything - error in hiding keyboard!!!!
    */
}
