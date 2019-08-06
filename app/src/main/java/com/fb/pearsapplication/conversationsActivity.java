package com.fb.pearsapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.fb.pearsapplication.adapters.ChatAdapter;
import com.fb.pearsapplication.adapters.conversationsAdapter;
import com.fb.pearsapplication.models.Pear;
import com.fb.pearsapplication.models.PearMessage;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class conversationsActivity extends AppCompatActivity {

    private List<ParseUser> pearList;
    public static ParseUser user;
    androidx.appcompat.widget.Toolbar toolbar;

    static final String TAG = conversationsActivity.class.getSimpleName();

    RecyclerView rvConversations;
    conversationsAdapter cAdapter;
    private String mUserId;

    private EndlessRecyclerViewScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Conversations with Pears");


        pearList = new ArrayList<ParseUser>();
        final ArrayList<String> pearID = new ArrayList<>();


        rvConversations = (RecyclerView) findViewById(R.id.rvConversations);

        cAdapter = new conversationsAdapter(pearList);
        rvConversations.setAdapter(cAdapter);


        rvConversations.setLayoutManager(new LinearLayoutManager(this));

        user = ParseUser.getCurrentUser();

        ParseQuery<Pear> pearQuery = ParseQuery.getQuery("Pear");
        pearQuery.whereEqualTo("user", user);
        pearQuery.findInBackground(new FindCallback<Pear>() {

            @Override
            public void done(List<Pear> objects, ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                } else if (!objects.isEmpty()) {
                    for (Pear pear : objects) {
                        if (!pearID.contains(pear.getOtherUser().getObjectId())) {
                            pearList.add(pear.getOtherUser());
                            pearID.add(pear.getOtherUser().getObjectId());
                        }
                    }
                    cAdapter.notifyDataSetChanged();
                }

            }
        });

    }

}
