package com.fb.pearsapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fb.pearsapplication.adapters.groupsAdapter;
import com.fb.pearsapplication.adapters.messagesAdapter;
import com.fb.pearsapplication.models.PearMessage;
import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;

import static com.parse.Parse.getApplicationContext;

public class conversationsActivity extends AppCompatActivity {
    static final String TAG = conversationsActivity.class.getSimpleName();



    RecyclerView rvConversations;
    ArrayList<Message> mMessages;
    messagesAdapter mAdapter;
    // Keep track of initial load to scroll to the bottom of the ListView
    boolean mFirstLoad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations);
        // User login
        if (ParseUser.getCurrentUser() != null) { // start with existing user

            rvConversations = findViewById(R.id.rvConversations);

//            // create the data source
//            mConversations = new ArrayList<>();
//            // create the adapter
//            adapter = new groupsAdapter(getContext(), mGroups);
//            // set the adapter on the recycler view
//            rvConversations.setAdapter(adapter);
//            // set the layout manager on the recycler view
//            rvConversations.setLayoutManager(new LinearLayoutManager(this));
//
//            queryConversations();
//
//
//            // Configure the RecyclerView
//            RecyclerView rvConversations = (RecyclerView) findViewById(R.id.rvConversations);
//            GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
//            rvConversations.setLayoutManager(gridLayoutManager);
//            // Retain an instance so that you can call `resetState()` for fresh searches
//            scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
//                @Override
//                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
//                    // Triggered only when new data needs to be appended to the list
//                    // Add whatever code is needed to append new items to the bottom of the list
//                    loadNextDataFromApi(page);
//                }
//            };
//            // Adds the scroll listener to RecyclerView
//            rvConversations.addOnScrollListener(scrollListener);
        }

    }







}
