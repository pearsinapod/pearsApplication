package com.fb.pearsapplication.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fb.pearsapplication.R;
import com.fb.pearsapplication.models.Group;
import com.fb.pearsapplication.models.GroupUserRelation;
import com.fb.pearsapplication.models.Pear;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class groupDetailsFragment extends Fragment {

    Group group;
    Context context;
    ParseUser currentUser;

    // the view objects
    ImageView ivGroupImage;
    TextView tvGroupName;
    TextView tvGroupNumber;
    TextView tvDescription;
    Switch swPear;

    // pear details
    ParseUser pearUser;
    Pear pear;
    GroupUserRelation gur;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        group = (Group) getArguments().getSerializable("anything");
        return inflater.inflate(R.layout.fragment_group_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ivGroupImage = (ImageView) view.findViewById(R.id.ivGroupImage);
        tvGroupName = (TextView) view.findViewById(R.id.tvGroupName);
        tvDescription = (TextView) view.findViewById(R.id.tvDescription);
        tvGroupNumber = (TextView) view.findViewById(R.id.tvGroupNumber);
        swPear = (Switch) view.findViewById(R.id.swPear);
        currentUser = ParseUser.getCurrentUser();
        bindViews();
        determineChildFragment();

    }

    private void bindViews() {
        tvGroupName.setText(group.getGroupName());
        ParseFile image = group.getGroupImage();
        if (image != null) {
            Glide.with(getContext()).load(image.getUrl()).apply(RequestOptions.circleCropTransform()).into(ivGroupImage);
        }
        if (group.getUsers() != null) {
            Integer size = group.getUsers().size();
            String sizeString = size.toString();
            tvGroupNumber.setText(sizeString);
        }
        tvDescription.setText(group.getDescription());
        String timeAgo = group.getRelativeTimeAgo();

        if (currentUser.getList("pearRequests").contains(group)) {
            swPear.setChecked(true);
        }
    }

    private void determineChildFragment() {
        ParseQuery<GroupUserRelation> query = new ParseQuery<GroupUserRelation>(GroupUserRelation.class);
        query.whereEqualTo("user", currentUser);
        query.whereEqualTo("group", group);
        query.findInBackground(new FindCallback<GroupUserRelation>() {
            @Override
            public void done(List<GroupUserRelation> objects, ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                    return;
                }
                if (objects.isEmpty()) {
                    insertNestedAddFragment();
                } else if (objects.get(0).getPearRequest()) {
                    gur = objects.get(0);
                    insertNestedPearButtonFragment();
                } else {
                    // TODO: Make new query here
                }
            }
        });
    }

    public void insertNestedPearFragment() {
        Fragment childFragment = new ChildPearFragment();
        ((ChildPearFragment) childFragment).setPear(pear);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.child_fragment_container, childFragment).commit();
    }

    public void insertNestedAddFragment() {
        Fragment childFragment = new ChildAddFragment();
        ((ChildAddFragment) childFragment).setGroup(group);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.child_fragment_container, childFragment).commit();
    }

    public void insertNestedPearButtonFragment() {
        Fragment childFragment = new ChildPearButtonFragment();
        ((ChildPearButtonFragment) childFragment).setGUR(gur);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.child_fragment_container, childFragment).commit();
    }

    public void insertNestedWaitingFragment() {
        Fragment childFragment = new ChildWaitingFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.child_fragment_container, childFragment).commit();
    }

}



