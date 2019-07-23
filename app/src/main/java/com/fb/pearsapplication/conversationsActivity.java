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
import com.fb.pearsapplication.models.PearMessage;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class conversationsActivity extends AppCompatActivity {

    private List<ParseUser> userList;
    public static ParseUser user;

    static final String TAG = conversationsActivity.class.getSimpleName();

    RecyclerView rvConversations;
    conversationsAdapter cAdapter;
    private String mUserId;

    private EndlessRecyclerViewScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations);

        userList = new ArrayList<ParseUser>();


        rvConversations = (RecyclerView) findViewById(R.id.rvConversations);

        cAdapter = new conversationsAdapter(userList);
        rvConversations.setAdapter(cAdapter);


        rvConversations.setLayoutManager(new LinearLayoutManager(this));

        user = ParseUser.getCurrentUser();





        loadConversationList();
        }



    private void loadConversationList() {

        ParseUser.getQuery().whereNotEqualTo("username", user.getUsername()).findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                if (list != null) {

                    for(int i=0;i<list.size();i++){
                        userList.add(list.get(i));
                    }
//                    if (list.size() == 0)
//                        Toast.makeText(conversationsActivity.this, "no user found", Toast.LENGTH_SHORT).show();
//                    else userList = list;
                    cAdapter.notifyDataSetChanged();

                }
                else {
                    Log.e(TAG, "error getting conversations", e);

                }
            }
        });
    }
}
