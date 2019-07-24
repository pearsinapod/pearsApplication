package com.fb.pearsapplication.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.fb.pearsapplication.LoginActivity;
import com.fb.pearsapplication.MainActivity;
import com.fb.pearsapplication.R;
import com.fb.pearsapplication.models.Group;
import com.fb.pearsapplication.models.GroupUserRelation;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class ChildAddFragment extends Fragment {
    Button btnJoin;
    ParseUser currentUser;
    Group group;
    Switch swPear;
    GroupUserRelation gur;

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
                final GroupUserRelation groupUser = addUserToGroup(currentUser, group);
                groupUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.d("XYZ", "added successfully");
                            gur = groupUser;
                            goToPearBtnFragment();
                        } else {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private GroupUserRelation addUserToGroup(ParseUser user, Group group) {
        final GroupUserRelation groupUser = new GroupUserRelation();
        groupUser.setGroup(group);
        groupUser.setUser(user);
        groupUser.setPearRequest(true);

        return groupUser;
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
        ((ChildPearButtonFragment) fragment).setGUR(gur);
        groupDetailsFragment parentFrag = ((groupDetailsFragment)ChildAddFragment.this.getParentFragment());
        swPear = parentFrag.swPear;
        swPear.setChecked(true);
        fragmentManager.beginTransaction().replace(R.id.child_fragment_container, fragment).addToBackStack(null).commit();
    }

}
