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
        if (currentUser.getList("pearRequests").contains(group)) {
            swPear.setChecked(true);
            btnPear.setEnabled(true);
            btnPear.setClickable(true);
            btnListener();
        }
        currentUser = ParseUser.getCurrentUser();
        swPear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (swPear.isChecked()) {
                    Log.d("switch", "on");
                    btnPear.setEnabled(true);
                    btnPear.setClickable(true);
                    switchOnMethod();
                    btnListener();
                } else {
                    Log.d("switch", "off");
                    btnPear.setEnabled(false);
                    btnPear.setClickable(false);
                    switchOffMethod();
                }
            }
        });
    }

    private void switchOnMethod() {
        ArrayList groupPears = group.getPears();
        ArrayList userPearRequests = (ArrayList) currentUser.getList("pearRequests");
        if (!userPearRequests.contains(group)) {
            groupPears.add(currentUser);
            userPearRequests.add(group);
        }
        group.put(Group.KEY_PEARS, groupPears);
        currentUser.put("pearRequests", userPearRequests);
        group.saveInBackground();
        currentUser.saveInBackground();
    }

    private void switchOffMethod() {
        ArrayList groupPears = group.getPears();
        ArrayList userPearRequests = (ArrayList) currentUser.getList("pearRequests");
        groupPears.remove(currentUser);
        userPearRequests.remove(group);
        group.put(Group.KEY_PEARS, groupPears);
        currentUser.put("pearRequests", userPearRequests);
        group.saveInBackground();
        currentUser.saveInBackground();
    }

    private void btnListener() {
        btnPear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<ParseUser> pearUsers = group.getPears();

                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.whereNotEqualTo("username", currentUser.getUsername());
                ArrayList<Group> currentGroup = new ArrayList<Group>();
                currentGroup.add(group);
                query.whereContainedIn("pearRequests", currentGroup);
                query.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {
                        if (objects.size() == 0) {
                            goToWaitingFragment();
                        } else {
                            pearUser = objects.get(0); // TODO: change this so it's not the same pear everytime
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
                    updatePearedUsers();
                    Log.d("XYZ", "Pear created successfully!");
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updatePearedUsers() {
        ArrayList updatedPears = (ArrayList) group.getList("pears");
        ArrayList updatedUserPears = (ArrayList) currentUser.getList("pearRequests");
        ArrayList updatedPearUser = (ArrayList) pearUser.getList("pearRequests");

        updatedPears.remove(currentUser);
        updatedPears.remove(pearUser);
        updatedUserPears.remove(group);
        updatedPearUser.remove(group);

        group.put(Group.KEY_PEARS, updatedPears);
        currentUser.put("pearRequests", updatedUserPears);
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
