package com.fb.pearsapplication.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.fb.pearsapplication.R;
import com.fb.pearsapplication.models.Group;
import com.fb.pearsapplication.models.GroupUserRelation;
import com.fb.pearsapplication.models.Pear;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChildPearButtonFragment extends Fragment {

    Button btnPear;
    Switch swPear;
    ParseUser currentUser;
    ParseUser pearUser;
    Group group;
    Pear pear;
    GroupUserRelation gur;
    GroupUserRelation otherGUR;


    // TODO: need to figure out how to send information between the parent and child fragments

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_child_pear_button, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        btnPear = (Button) view.findViewById(R.id.btnPear);
        groupDetailsFragment parentFrag = ((groupDetailsFragment)ChildPearButtonFragment.this.getParentFragment());
        swPear = parentFrag.swPear;
        currentUser = ParseUser.getCurrentUser();
        if (gur.getPearRequest()) {
            swPear.setChecked(true);
            btnPear.setEnabled(true);
            btnPear.setClickable(true);
            btnListener();
        } else {
            swPear.setChecked(false);
            btnPear.setEnabled(false);
            btnPear.setClickable(false);
        }
        currentUser = ParseUser.getCurrentUser();
        swPear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (swPear.isChecked()) {
                    Log.d("switch", "on");
                    switchOnMethod();
                    btnListener();
                } else {
                    Log.d("switch", "off");
                    switchOffMethod();
                }
            }
        });
    }

    private void switchOnMethod() {
        btnPear.setEnabled(true);
        btnPear.setClickable(true);
        gur.setPearRequest(true);
        gur.saveInBackground();
    }

    private void switchOffMethod() {
        btnPear.setEnabled(false);
        btnPear.setClickable(false);
        gur.setPearRequest(false);
        gur.saveInBackground();
    }

    private void btnListener() {
        btnPear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<ParseUser> pearUsers = group.getPears();

                ParseQuery<GroupUserRelation> query = ParseQuery.getQuery(GroupUserRelation.class);
                query.whereNotEqualTo("user", currentUser);
                query.whereEqualTo("group", group);
                query.whereEqualTo("pearRequest", true);

                query.findInBackground(new FindCallback<GroupUserRelation>() {
                    @Override
                    public void done(List<GroupUserRelation> objects, ParseException e) {
                        if (e != null) {
                            e.printStackTrace();
                        } else if (objects.isEmpty()) {
                            goToWaitingFragment();
                        } else {
                            otherGUR = objects.get(0);
                            pearUser = otherGUR.getUser();
                            createPear();
                        }
                    }
                });
            }
        });
    }

    private void createPear() {
        final Pear newPear = new Pear();
        ArrayList pearedUsers = new ArrayList();
        pearedUsers.add(currentUser.getObjectId());
        pearedUsers.add(pearUser.getObjectId());
        newPear.setUsers(pearedUsers);
        newPear.setUser1(currentUser);
        newPear.setUser2(pearUser);
        newPear.setGroup(group);

        newPear.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    pear = newPear;
                    updateRelations();
                    goToPearFragment();
                    Log.d("XYZ", "Pear created successfully!");
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateRelations() {
        otherGUR.setPearRequest(false);
        gur.setPearRequest(false);
        otherGUR.saveInBackground();
        gur.saveInBackground();
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void setGUR(GroupUserRelation gur) {
        this.gur = gur;
        this.group = gur.getGroup();
    }

    private void goToPearFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = new ChildPearFragment();
        ((ChildPearFragment) fragment).setPear(pear);
        fragmentManager.beginTransaction().replace(R.id.child_fragment_container, fragment).addToBackStack(null).commit();
    }

    private void goToWaitingFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = new ChildWaitingFragment();
        fragmentManager.beginTransaction().replace(R.id.child_fragment_container, fragment).addToBackStack(null).commit();
    }
}
