package com.fb.pearsapplication.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
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

import java.util.List;

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
        pearQuery();
        bindViews();
    }

    private void bindViews() {
        tvGroupName.setText(group.getGroupName());
        ParseFile image = group.getGroupImage();
        if (image != null) {
            Glide.with(getContext()).load(image.getUrl()).into(ivGroupImage);
        } else {
            Glide.with(getContext()).load(R.drawable.people).into(ivGroupImage);
        }
/*        else{
            Glide.with(getContext()) //for some reason placeholder and error doesn't work for me: if it works for you lmk! - ang
                    .load(R.drawable.group_search_placeholder)
                    .apply(RequestOptions.circleCropTransform())
                    .into(ivGroupImage);
        }*/
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
                } else if (!objects.isEmpty() && pear == null) {
                    gur = objects.get(0);
                    insertNestedPearButtonFragment();
                } else {
                    insertNestedPearFragment();
                }
            }
        });
    }

    private void pearQuery() {
        ParseQuery<Pear> pearQuery = new ParseQuery<Pear>(Pear.class);
        pearQuery.whereEqualTo("user", currentUser);
        pearQuery.whereEqualTo("group", group);
        pearQuery.findInBackground( new FindCallback<Pear>() {
            @Override
            public void done(List<Pear> objects, ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                    return;
                }
                if (objects.isEmpty()) {
                    pear = null;
                } else {
                    pear = objects.get(0);
                }
                determineChildFragment();
            }
        });
    }

    // used for database
    public void addUsersToGroup() {
        Log.d("XYZ", "inside function");
        ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
        userQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                } else {
                    for (ParseUser user : objects) {
                        GroupUserRelation gur = ChildAddFragment.addUserToGroup(user, group);
                        gur.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e != null) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
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

    public ParseUser getOtherUser() {
        return pear.getOtherUser();
    }

}



