package com.fb.pearsapplication.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fb.pearsapplication.R;
import com.fb.pearsapplication.models.Group;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class exploreFragment extends Fragment {

    private ArrayList<String> exploreGroups;
    private ArrayAdapter<String> exploreArrayAdapter;
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
        exploreArrayAdapter = new ArrayAdapter<>(getContext(), R.layout.item_explore_group, R.id.tvExploreName, exploreGroups);
        flingContainer = view.findViewById(R.id.frame);
        flingContainer.setAdapter(exploreArrayAdapter);
        setUpExploreFlingContainer();
        setOnClickListenerFling();

    }

    public void setUpExploreFlingContainer(){
        flingContainer.setBackgroundColor(0000);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("Explore Fragment:", "Removed Object from Adapter");
                exploreGroups.remove(0);
                exploreArrayAdapter.notifyDataSetChanged();
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
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // So it never ends
                addGroupNames();
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
            }
        });

    }

    public void setOnClickListenerFling(){
        // Optionally add an OnItemClickListener
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
                    for (int i = 0; i<objects.size(); i++){
                        exploreGroups.add(objects.get(i).getGroupName());
                    }
                    exploreArrayAdapter.notifyDataSetChanged();
                }
                else{
                    e.printStackTrace();
                }
            }
        });
    }



}
