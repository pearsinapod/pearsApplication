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
import com.fb.pearsapplication.models.Pear;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Random;

public class ChildPearButtonFragment extends Fragment {

    Button btnPear;
    Switch swPear;
    ParseUser currentUser;
    ParseUser pearUser;
    Group group;
    Pear pear;


    // TODO: need to figure out how to send information between the parent and child fragments

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_child_pear_button, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        btnPear = (Button) view.findViewById(R.id.btnPear);
        currentUser = ParseUser.getCurrentUser();
        groupDetailsFragment parentFrag = ((groupDetailsFragment)ChildPearButtonFragment.this.getParentFragment());
        swPear = parentFrag.swPear;
        swPear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (swPear.isChecked()) {
                    Log.d("switch", "on");
                    ArrayList groupPears = group.getPears();
                    groupPears.add(currentUser);
                    group.put(Group.KEY_PEARS, groupPears);
                    group.saveInBackground();
                    btnListener();
                } else {
                    Log.d("switch", "off");
                    btnPear.setEnabled(false);
                    btnPear.setClickable(false);
                    ArrayList groupPears = group.getPears();
                    groupPears.remove(currentUser);
                    group.put(Group.KEY_PEARS, groupPears);
                    group.saveInBackground();
                }
            }
        });

    }

    private void btnListener() {
        btnPear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<ParseUser> pearUsers = group.getPears();

                if (pearUsers.size() == 0) {
                    goToWaitingFragment();
                } else {
                    Random randomIntGen = new Random();
                    Log.d("XYZ", "pears size is " + ((Integer) pearUsers.size()).toString());
                    int pearPosition = randomIntGen.nextInt(pearUsers.size());
                    pearUser = pearUsers.get(pearPosition);

//                    // used to ensure that the user doesn't get paired up with themselves
//                    while (pearUser.getObjectId().equals(currentUser.getObjectId())) {
//                        pearPosition = randomIntGen.nextInt(pearUsers.size());
//                        pearUser = pearUsers.get(pearPosition);
//                    }
                    createPear();
                    goToPearFragment();
                }
            }
        });
    }

    private void createPear() {
        Pear newPear = new Pear();
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
                    Log.d("XYZ", "Pear created successfully!");
                } else {
                    e.printStackTrace();
                }
            }
        });

        ArrayList updatedPears = (ArrayList) group.getList("pears");
        updatedPears.remove(currentUser);
        updatedPears.remove(pearUser);
        group.put(Group.KEY_PEARS, updatedPears);

        group.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("XYZ", "Pear created successfully!");
                } else {
                    e.printStackTrace();
                }
            }
        });
        pear = newPear;
    }

    public void setGroup(Group group) {
        this.group = group;
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
