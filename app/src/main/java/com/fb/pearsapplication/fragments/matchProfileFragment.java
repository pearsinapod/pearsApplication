package com.fb.pearsapplication.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fb.pearsapplication.R;
import com.fb.pearsapplication.adapters.QuestionAdapter;
import com.fb.pearsapplication.conversationsActivity;
import com.fb.pearsapplication.models.Group;
import com.fb.pearsapplication.models.GroupUserRelation;
import com.fb.pearsapplication.models.Pear;
import com.fb.pearsapplication.models.UserQuestion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class matchProfileFragment extends Fragment {

    public Group group;
    public ParseUser pearUser;
    public ImageView ivProfileOther;
    public TextView tvName;
    public TextView tvDescription;
    public TextView tvDistance;
    public Button btnUnpear;
    public Button btnMessage;
    public RecyclerView rvQuestions;
    public List<UserQuestion> uqList;

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
        btnUnpear = view.findViewById(R.id.btnUnpear);
        btnMessage = view.findViewById(R.id.btnMessage);
        rvQuestions = view.findViewById(R.id.rvQuestions);
        bindViews();
        queryUserQuestions();
        btnUnpearClick();
    }

    public void bindViews() {
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

    public void btnUnpearClick() {
        btnUnpear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                queryUnpear();
            }
        });
        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chatIntent = new Intent(getActivity(), conversationsActivity.class);
                startActivity(chatIntent);
            }
        });
    }

    private void queryUnpear() {
        ParseQuery<Pear> pearQuery = new ParseQuery<Pear>(Pear.class);
        pearQuery.whereEqualTo("user", ParseUser.getCurrentUser());
        pearQuery.whereEqualTo("otherUser", pearUser);
        pearQuery.whereEqualTo("group", group);
        pearQuery.findInBackground(new FindCallback<Pear>() {
            @Override
            public void done(List<Pear> objects, ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                }
                ParseObject.deleteAllInBackground(objects, new DeleteCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            e.printStackTrace();
                        } else {
                            Log.d("delete", "pear done");
                        }
                    }
                });
            }
        });

        ParseQuery<Pear> otherPearQuery = new ParseQuery<Pear>(Pear.class);
        otherPearQuery.whereEqualTo("otherUser", ParseUser.getCurrentUser());
        otherPearQuery.whereEqualTo("user", pearUser);
        pearQuery.whereEqualTo("group", group);
        otherPearQuery.findInBackground(new FindCallback<Pear>() {
            @Override
            public void done(List<Pear> objects, ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                }
                ParseObject.deleteAllInBackground(objects, new DeleteCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            e.printStackTrace();
                        } else {
                            Log.d("delete", "pear done");
                        }
                    }
                });
            }
        });

        queryGUR(ParseUser.getCurrentUser());
        queryGUR(pearUser);
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = new groupFragment();
        fragmentManager.beginTransaction().replace(R.id.flContainter, fragment).addToBackStack(null).commit();
    }

    private void queryGUR(ParseUser user) {
        ParseQuery<GroupUserRelation> gurQuery = new ParseQuery<GroupUserRelation>(GroupUserRelation.class);
        gurQuery.whereEqualTo("user", user);
        gurQuery.whereEqualTo("group", group);
        gurQuery.findInBackground(new FindCallback<GroupUserRelation>() {
            @Override
            public void done(List<GroupUserRelation> objects, ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                } else {
                    GroupUserRelation gur = objects.get(0);
                    gur.setPearRequest(true);
                    gur.saveInBackground();
                    Log.d("delete", "gur done");
                }
            }
        });
    }

    public double calculateDistance() {
        double distance = pearUser.getParseGeoPoint("location").distanceInMilesTo(ParseUser.getCurrentUser().getParseGeoPoint("location"));
        return Math.round(distance * 10) / 10.0;
    }

    public void setPearUser(ParseUser pearUser) {
        this.pearUser = pearUser;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void queryUserQuestions() {
        ParseQuery<UserQuestion> uqQuery = new ParseQuery<UserQuestion>(UserQuestion.class);
//        uqQuery.whereEqualTo("user", pearUser);
//        uqQuery.orderByDescending("targetDate");
        uqQuery.findInBackground(new FindCallback<UserQuestion>() {
            @Override
            public void done(List<UserQuestion> objects, ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                } else if (!objects.isEmpty()) {
                    uqList = objects;
                    createAdapter();
                }
            }
        });
    }

    public void createAdapter() {
        QuestionAdapter adapter = new QuestionAdapter(uqList);
        rvQuestions.setAdapter(adapter);
        rvQuestions.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

}
