package com.fb.pearsapplication.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fb.pearsapplication.R;
import com.fb.pearsapplication.adapters.messagesAdapter;
import com.fb.pearsapplication.models.PearMessage;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class messageFragment extends Fragment {

    public static final String TAG = "messageFragment";
    public static final String USER_ID_KEY = "userId";
    public static final String BODY_KEY = "body";
    public static final String FRIEND_ID_KEY = "friendsId";


//    Intent callingIntent = getIntent();
//    FRIEND_ID_KEY =callingIntent.getExtras().getString("friendsObjectId");


    EditText etMessage;
    Button btSend;

    RecyclerView rvMessage;
    messagesAdapter adapter;
    ArrayList<PearMessage> mMessages;
    boolean mFirstLoad;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Find the text field and button
        etMessage = (EditText) view.findViewById(R.id.etMessage);
        btSend = (Button) view.findViewById(R.id.btSend);
        // When send button is clicked, create message object on Parse
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = etMessage.getText().toString();

                PearMessage pearMessage = new PearMessage();
                pearMessage.setBody(data);
                pearMessage.setUserId(ParseUser.getCurrentUser().getObjectId());
                pearMessage.setReceiverId(FRIEND_ID_KEY);
                pearMessage.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        receiveMessage();
                    }
                });
                etMessage.setText(null);
            }
        });
    }

    private void receiveMessage() {

        // Sent Messages Query
        ParseQuery<PearMessage> sentMessagesQuery = ParseQuery.getQuery(PearMessage.class);
        sentMessagesQuery.whereEqualTo("userId", USER_ID_KEY);
        sentMessagesQuery.whereEqualTo("receiverId", FRIEND_ID_KEY);

        // Receiver Messages Query
        ParseQuery<PearMessage> receiveMessagesQuery = ParseQuery.getQuery(PearMessage.class);
        receiveMessagesQuery.whereEqualTo("userId", FRIEND_ID_KEY);
        receiveMessagesQuery.whereEqualTo("receiverId", USER_ID_KEY); //receiver is me (current user)

        // Combine the queries
        List<ParseQuery<PearMessage>> queries = new ArrayList<>();
        queries.add(sentMessagesQuery);
        queries.add(receiveMessagesQuery);

        // Get the messages
        ParseQuery<PearMessage> mainQuery = ParseQuery.or(queries);
        // Configure limit and sort order
        mainQuery.orderByAscending("createdAt");

        mainQuery.findInBackground(new FindCallback<PearMessage>() {
            @Override
            public void done(List<PearMessage> objects, ParseException e) {
            }
        });


    }
}

