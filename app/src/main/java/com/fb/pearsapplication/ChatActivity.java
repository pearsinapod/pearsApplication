package com.fb.pearsapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fb.pearsapplication.R;
import com.fb.pearsapplication.adapters.ChatAdapter;
import com.fb.pearsapplication.models.PearMessage;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private ArrayList<PearMessage> mMessages;
    private ArrayList<ParseUser> userList;
    public static ParseUser user;
    private ChatAdapter mAdapter;
    private EditText etMessage;
    private Button btSend;
    private String receiver;
    private Date lastMessageDate;
    private RecyclerView rvChat;
    private Boolean isRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        user = ParseUser.getCurrentUser();

        mMessages = new ArrayList<PearMessage>();
        RecyclerView rvChat = (RecyclerView) findViewById(R.id.rvChat);
        mAdapter = new ChatAdapter(mMessages);
        rvChat.setAdapter(mAdapter);
        Button btSend = (Button) findViewById(R.id.btSend);

        etMessage = (EditText) findViewById(R.id.etMessage);
        etMessage.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        rvChat.setLayoutManager(new LinearLayoutManager(this));

        loadMessages();

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.btSend){
                    sendMessage();
                }
            }
        });
        receiver = getIntent().getStringExtra(Intent.EXTRA_DATA_REMOVED);
//        getActionBar().setTitle(receiver);

        Handler handler = new Handler();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isRunning = true;
        loadMessages();
    }

    private void loadMessages() {

        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Messages");
        if (mMessages.size() == 0){
            ArrayList<String> ArrayList = new ArrayList<String>();
            ArrayList.add(receiver);
            ArrayList.add(user.getUsername());
            parseQuery.whereContainedIn("messageAuthor", ArrayList);
            parseQuery.whereContainedIn("messageReceiver", ArrayList);
        } else {
            if (lastMessageDate != null)
                parseQuery.whereGreaterThan("createdAt", lastMessageDate);
            parseQuery.whereEqualTo("messageAuthor", user.getUsername());
            parseQuery.whereEqualTo("messageReceiver", receiver);
        }
        parseQuery.orderByDescending("createdAt");
        parseQuery.setLimit(30);
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (list != null && list.size() > 0){
                    for (int i = list.size() - 1; i >= 0; i--){
                        ParseObject parseObject = list.get(i);
                        PearMessage pearMessage = new PearMessage(
                                parseObject.getString("body"),
                                parseObject.getCreatedAt(),
                                parseObject.getString("messageAuthor"));
                        mMessages.add(pearMessage);
                        if (lastMessageDate == null
                                || lastMessageDate.before(pearMessage.getCreatedAt()))
                            lastMessageDate = pearMessage.getCreatedAt();
                        mAdapter.notifyDataSetChanged();
                    }
                }
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isRunning)
                            loadMessages();

                    }
                }, 1000);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false;
    }


    private void sendMessage() {

        if(etMessage.length() == 0)
            return;

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(etMessage.getWindowToken(), 0);

        String string = etMessage.getText().toString();
        final PearMessage pearMessage = new PearMessage(string, new Date(), user.getUsername());
        pearMessage.setStatus(PearMessage.STATUS_SENDING);
        mMessages.add(pearMessage);
        mAdapter.notifyDataSetChanged();
        etMessage.setText(null);

        ParseObject parseObject = new ParseObject("Message");
        parseObject.put("messageAuthor", user.getUsername());
        parseObject.put("messageReceiver", receiver);
        parseObject.put("body", string);
        parseObject.saveEventually(new SaveCallback() {
            @Override
            public void done(ParseException e) {
               if (e == null)
                   pearMessage.setStatus(PearMessage.STATUS_SENT);
               else
                   pearMessage.setStatus(PearMessage.STATUS_FAILED);
               mAdapter.notifyDataSetChanged();
            }
        });
    }
}
//    static final String TAG = ChatActivity.class.getSimpleName();
//    static final String USER_ID_KEY = "messageAuthor";
//    static final String BODY_KEY = "body";
//
//    EditText etMessage;
//    Button btSend;
//
//    RecyclerView rvChat;
//    ArrayList<PearMessage> mMessages;
//    ChatAdapter mAdapter;
//    // Keep track of initial load to scroll to the bottom of the ListView
//    boolean mFirstLoad;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_chat);
//        // User login
//        if (ParseUser.getCurrentUser() != null) { // start with existing user
//            startWithCurrentUser();
//        } else { // If not logged in, login as a new anonymous user
//            login();
//        }
//
//    }
//
//    // Get the userId from the cached currentUser object
//    void startWithCurrentUser() {
//        setupMessagePosting();
//
//    }
//
//    // Create an anonymous user using ParseAnonymousUtils and set sUserId
//    void login() {
//        ParseAnonymousUtils.logIn(new LogInCallback() {
//            @Override
//            public void done(ParseUser user, ParseException e) {
//                if (e != null) {
//                    Log.e(TAG, "Anonymous login failed: ", e);
//                } else {
//                    startWithCurrentUser();
//                }
//            }
//        });
//    }
//
//
//
//    // Setup button event handler which posts the entered message to Parse
//    void setupMessagePosting() {
//        // Find the text field and button
//        etMessage = (EditText) findViewById(R.id.etMessage);
//        btSend = (Button) findViewById(R.id.btSend);
//        // When send button is clicked, create message object on Parse
//        btSend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String data = etMessage.getText().toString();
//
//                PearMessage message = new PearMessage();
//                message.setBody(data);
//                message.setUserId(ParseUser.getCurrentUser().getObjectId());
//
//                message.saveInBackground(new SaveCallback() {
//                    @Override
//                    public void done(ParseException e) {
//                        if(e == null) {
//                            Toast.makeText(ChatActivity.this, "Successfully created message on Parse",
//                                    Toast.LENGTH_SHORT).show();
//                        } else {
//                            Log.e(TAG, "Failed to save message", e);
//                        }
//                    }
//                });
//                etMessage.setText(null);
//            }
//        });
//    }
//
//
//}
