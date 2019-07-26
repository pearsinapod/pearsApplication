package com.fb.pearsapplication.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fb.pearsapplication.R;
import com.fb.pearsapplication.models.Group;
import com.fb.pearsapplication.models.Pear;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.IOException;
import java.util.ArrayList;

public class matchProfileFragment extends Fragment {

    public ParseUser pearUser;
    public ImageView ivProfileOther;
    public TextView tvName;
    public TextView tvDescription;
    public TextView tvDistance;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_match_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ivProfileOther = view.findViewById(R.id.ivProfileOther);
        tvName = view.findViewById(R.id.tvName);
        tvDescription = view.findViewById(R.id.tvDescription);
        tvDistance = view.findViewById(R.id.tvDistance);

        ParseFile profileImage = pearUser.getParseFile("profileImage");
        String profileImageString = pearUser.getString("profilePicString");
        if (profileImage != null) {
            Glide.with(getContext()).load(profileImage.getUrl()).apply(RequestOptions.circleCropTransform()).into(ivProfileOther);
        } else if (profileImageString != null) {
            Glide.with(getContext()).load(profileImageString).apply(RequestOptions.circleCropTransform()).into(ivProfileOther);
        } else {
            Glide.with(getContext()).load(R.drawable.user).apply(RequestOptions.circleCropTransform()).into(ivProfileOther);
        }

        tvName.setText(pearUser.getUsername());
        tvDistance.setText(calculateDistance() + " miles away");

        if (pearUser.getString("description") != null) {
            tvDescription.setText(pearUser.getString("description"));
        } else {
            tvDescription.setVisibility(View.GONE);
        }

    }

    public double calculateDistance() {
        double distance = pearUser.getParseGeoPoint("location").distanceInMilesTo(ParseUser.getCurrentUser().getParseGeoPoint("location"));
        return Math.round(distance * 10) / 10.0;
    }

    public void querySameGroups() {
        ParseQuery<Group> groupQuery = new ParseQuery<Group>(Group.class);
        groupQuery.include(Group.KEY_USERS);
        ArrayList<ParseUser> users = new ArrayList<>();
        users.add(ParseUser.getCurrentUser());
        users.add(pearUser);

        groupQuery.whereContainsAll("users", users);

    }

    public void setPearUser(ParseUser pearUser) {
        this.pearUser = pearUser;
    }

}
