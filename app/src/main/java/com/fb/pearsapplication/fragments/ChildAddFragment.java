package com.fb.pearsapplication.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.fb.pearsapplication.R;
import com.fb.pearsapplication.models.Group;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;

public class ChildAddFragment extends Fragment {
    Button btnJoin;
    ParseUser currentUser;
    Group group;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_child_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        btnJoin = view.findViewById(R.id.btnJoin);
        currentUser = ParseUser.getCurrentUser();
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUserToGroup();
                checkACL();
                currentUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.d("XYZ", "added group");
                        } else {
                            e.printStackTrace();
                        }
                    }
                });

                group.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.d("XYZ", "added users");
                        } else {
                            e.printStackTrace();
                        }
                    }
                });
                goToPearBtnFragment();
            }
        });
    }

    private void addUserToGroup() {
        ArrayList groupUsers = group.getUsers();
        ArrayList groupPears = group.getPears();
        ArrayList userGroups = (ArrayList) currentUser.getList("groups");
        groupUsers.add(currentUser);
        groupPears.add(currentUser);
        if (userGroups == null) {
            userGroups = new ArrayList();
            userGroups.add(group);
        } else {
            userGroups.add(group);
        }
        group.put("users", groupUsers);
        group.put("pears", groupPears);
        currentUser.put("groups", userGroups);
    }

    private void checkACL() {
        ParseACL acl = new ParseACL(ParseUser.getCurrentUser());
        acl.setPublicReadAccess(true);
        acl.setPublicWriteAccess(true);
        group.setACL(acl);
        currentUser.setACL(acl);
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void goToPearBtnFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = new ChildPearButtonFragment();
        ((ChildPearButtonFragment) fragment).setGroup(group);
        fragmentManager.beginTransaction().replace(R.id.child_fragment_container, fragment).addToBackStack(null).commit();
    }

}
