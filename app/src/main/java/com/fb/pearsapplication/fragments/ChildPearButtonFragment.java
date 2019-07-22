package com.fb.pearsapplication.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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


        btnPear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<ParseUser> pearUsers = group.getPears();
                Random randomIntGen = new Random();
                int pearPosition = randomIntGen.nextInt(pearUsers.size());
                Integer in = pearPosition;
                Log.d("XYZ", in.toString());

                // used to ensure that the user doesn't get paired up with themselves
                while (pearUser.getObjectId() == currentUser.getObjectId()) {
                    pearPosition = randomIntGen.nextInt(pearUsers.size());
                    pearUser = pearUsers.get(pearPosition);
                }

                Pear newPear = new Pear();
                ArrayList pearedUsers = new ArrayList();
                pearedUsers.add(currentUser);
                pearedUsers.add(pearUser);
                newPear.setUsers(pearedUsers);
                newPear.setUser1(currentUser);
                newPear.setUser2(pearUser);
                newPear.setGroup(group);

                ArrayList updatedPears = (ArrayList) group.getList("pears");
                updatedPears.remove(currentUser);
                updatedPears.remove(pearUser);


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
                pear = newPear;
            }
        });
    }
}
