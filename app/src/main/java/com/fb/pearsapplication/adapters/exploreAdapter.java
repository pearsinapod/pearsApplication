package com.fb.pearsapplication.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.fb.pearsapplication.R;
import com.fb.pearsapplication.Tuple;
import com.fb.pearsapplication.models.Group;
import com.fb.pearsapplication.models.Hobby;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

// why do u need this? is this necessary?
public class exploreAdapter extends ArrayAdapter<Group> {
    Context context;
    private ArrayList<Group> userGroups;

    public exploreAdapter(Context context, int resourceId, List<Group> groups){
        super(context, resourceId, groups);
        userGroups = new ArrayList<>();

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Group group = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_explore_group, parent,false);

        }

        TextView tvExploreName = convertView.findViewById(R.id.tvExploreName);
        final CardView cardViewExplore = convertView.findViewById(R.id.cardViewExplore);
        ImageView ivExploreImage = convertView.findViewById(R.id.ivExploreImage);
        ImageButton btnDetails = convertView.findViewById(R.id.btnDetails);


//        tvExploreDescription.setText(group.getDescription());
        tvExploreName.setText(group.getGroupName());
//        cardViewExplore.setCardBackgroundColor(Color.parseColor("#F0F0F0"));
//        tvExploreDescription.setBackgroundColor(Color.parseColor("#F0F0F0"));
//        tvExploreName.setBackgroundColor(Color.parseColor("#F0F0F0"));

        addingUserGroup();
        if (userGroups.size()>0){
            String[] nameThreshold = gettingNameThreshold(userGroups, group);
            String name = nameThreshold[0];
            double threshold = Double.parseDouble(nameThreshold[1]); //if -1... don't show anything!
            int rating = pearRating(threshold);

        }

        btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater factory = LayoutInflater.from(getContext());
                final View alertView = factory.inflate(R.layout.explore_group_details, null);
                TextView tvGroupName = alertView.findViewById(R.id.tvGroupName);
                TextView tvDescription = alertView.findViewById(R.id.tvDescription);
                tvGroupName.setText(group.getGroupName());
                tvDescription.setText(group.getDescription());
                new AlertDialog.Builder(getContext())
                        .setView(alertView)
                        .show();
            }
        });
        ParseFile image = group.getGroupImage();

    if (image!=null){
            Glide.with(getContext()).load(group.getGroupImage().getUrl()).into(ivExploreImage);

        }
        else{
            Glide.with(getContext()) .load(R.drawable.group_search_placeholder) .into(ivExploreImage);
        }
        return convertView;

    }

    public int pearRating(double threshold){
        if (threshold<25){
            return 0;
        }
        if(threshold<50){
            return 1;
        }
        if(threshold<75){
            return 2;
        }
        return 3;
    }

    public void addingUserGroup(){
        userGroups.clear();
        final ParseQuery<Group> groupQuery = new ParseQuery<Group>(Group.class);
        ArrayList currentUser = new ArrayList(); //will change to make more efficient
        currentUser.add(ParseUser.getCurrentUser());
        groupQuery.whereContainedIn(Group.KEY_USERS, currentUser);
        groupQuery.findInBackground(new FindCallback<Group>() {
            @Override
            public void done(List<Group> objects, ParseException e) {
                    Log.d("this", Integer.toString(objects.size()));
                if (e == null) {
                    userGroups.addAll(objects);

                }
                else{
                    Log.d("AllUserGroups","Loading items failed");
                }
            }
        });
    }


    public String[] gettingNameThreshold(List<Group> userGroup, Group currentGroup){
        final ParseQuery<Hobby> hobbyQuery = new ParseQuery<Hobby>(Hobby.class);
        ArrayList currentObjectId = new ArrayList();
        currentObjectId.add(currentGroup.getObjectId());
        hobbyQuery.whereContainedIn(Hobby.KEY_SUBSET, currentObjectId);
        String[] arrMax = new String[2];
        hobbyQuery.findInBackground(new FindCallback<Hobby>() {
            @Override
            public void done(List<Hobby> objects, ParseException e) {
                int max = -1;
                Tuple tempTuple= new Tuple(Integer.toString(max), "hello");
                for (int subset =0 ; subset<objects.size(); subset++){
                    Hobby h = objects.get(subset);
                    ArrayList arrSubset = h.getSubsets();
                    for (int ug=0 ; ug< userGroup.size(); ug++){
                        Group currentGroup = userGroup.get(ug);
                        if (arrSubset.contains(currentGroup.getObjectId()) && h.getThreshold()> max){
                            tempTuple = new Tuple<>(currentGroup.getGroupName(), Integer.toString(h.getThreshold()));
                            max = h.getThreshold();
                        }
                    }

                }
                arrMax[0]= tempTuple.getX();
                arrMax[1]=tempTuple.getY();
            }
        });
        return arrMax;

    }

}
