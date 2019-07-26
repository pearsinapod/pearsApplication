package com.fb.pearsapplication.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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

public class ChildPearButtonFragment extends Fragment {

    View view;
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
        this.view = view;
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
//                query.whereNear("userLocation", currentUser.getParseGeoPoint("location"));
                query.whereEqualTo("group", group);
                query.whereEqualTo("pearRequest", true);

                query.findInBackground(new FindCallback<GroupUserRelation>() {
                    @Override
                    public void done(List<GroupUserRelation> objects, ParseException e) {
                        Log.d("pear request", objects.toString());
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
        final Pear newPearMe = new Pear();
        final Pear newPearOther = new Pear();
        newPearMe.setUser(currentUser);
        newPearMe.setOtherUser(pearUser);
        newPearMe.setGroup(group);

        newPearOther.setUser(pearUser);
        newPearOther.setOtherUser(currentUser);
        newPearOther.setGroup(group);

        newPearMe.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    pear = newPearMe;
                    updateRelations();
                    popupPear(view);
                    Log.d("XYZ", "Pear created successfully!");
                } else {
                    e.printStackTrace();
                }
            }
        });

        newPearOther.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void popupPear(View view) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_pear, null);
        int width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        int height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        bindPopupViews(popupView);
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                goToPearFragment();
            }
        });
    }

    public void bindPopupViews(View view) {
        TextView tvCurrentPear = view.findViewById(R.id.tvCurrentPear);
        ImageView ivPearPic = view.findViewById(R.id.ivPearPic);
        TextView tvPearName = view.findViewById(R.id.tvPearName);
        Button btnMessage = view.findViewById(R.id.btnMessage); // TODO set onclick listeners here
        Button btnViewProfile = view.findViewById(R.id.btnViewProfile);

        String name = "";
        try {
            name = pearUser.fetchIfNeeded().getString("username");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tvPearName.setText(name);
        ParseFile profileImage = pearUser.getParseFile("profileImage");
        String profileImageString = pearUser.getString("profilePicString");
        if (profileImage != null) {
            Glide.with(getContext()).load(profileImage.getUrl()).apply(RequestOptions.circleCropTransform()).into(ivPearPic);
        } else if (profileImageString != null) {
            Glide.with(getContext()).load(profileImageString).apply(RequestOptions.circleCropTransform()).into(ivPearPic);
        } else {
            Glide.with(getContext()).load(R.drawable.user).apply(RequestOptions.circleCropTransform()).into(ivPearPic);
        }
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
