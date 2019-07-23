package com.fb.pearsapplication.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fb.pearsapplication.R;
import com.fb.pearsapplication.adapters.exploreAdapter;
import com.fb.pearsapplication.models.Group;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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
        exploreGroups = new ArrayList<>();
        exploreArrayAdapter = new exploreAdapter(getContext(), R.layout.item_explore_group , exploreGroups);
        flingContainer = view.findViewById(R.id.frame);
        flingContainer.setAdapter(exploreArrayAdapter);

        setUpExploreFlingContainer();
        setOnClickListenerFling();
        addGroupNames();

    }

    public void setUpExploreFlingContainer(){
        flingContainer.setBackgroundColor(0000);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            //Group currentGroup = exploreGroups.get(0);
            @Override
            public void removeFirstObjectInAdapter() {
                exploreGroups.remove(0);
                exploreArrayAdapter.notifyDataSetChanged();
                Log.d("Explore Fragment:", "Removed Object from Adapter");
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                Log.d("Explore Fragment:", "Left");
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Log.d("Explore Fragment:", "Right");
                ChildAddFragment.addUserToGroup(ParseUser.getCurrentUser(), (Group)dataObject);

            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // So it never ends
               // addGroupNames();?
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
        final ParseQuery<Group> groupQuery = new ParseQuery<Group>(Group.class);
        groupQuery.addDescendingOrder(Group.KEY_CREATED_AT);
        groupQuery.findInBackground(new FindCallback<Group>() {
            @Override
            public void done(List<Group> objects, ParseException e) {
                if(e==null){
                    exploreGroups.addAll(objects);
                    exploreArrayAdapter.notifyDataSetChanged();
                }
                else{
                    e.printStackTrace();
                }
            }
        });
    }


}

