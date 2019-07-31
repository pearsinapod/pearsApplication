package com.fb.pearsapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fb.pearsapplication.adapters.ChatAdapter;
import com.fb.pearsapplication.models.PearMessage;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

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

    static final int POLL_INTERVAL = 1000;
    Handler myHandler = new Handler();
    Runnable mRefreshMessagesRunnable = new Runnable() {
        @Override
        public void run() {
            loadMessages();
            myHandler.postDelayed(this, POLL_INTERVAL);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        user = ParseUser.getCurrentUser();

        myHandler.postDelayed(mRefreshMessagesRunnable, POLL_INTERVAL);

        mMessages = new ArrayList<PearMessage>();
        rvChat = (RecyclerView) findViewById(R.id.rvChat);
        mAdapter = new ChatAdapter(mMessages);
        rvChat.setAdapter(mAdapter);
        btSend = (Button) findViewById(R.id.btSend);

        etMessage = (EditText) findViewById(R.id.etMessage);
        etMessage.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        rvChat.setLayoutManager(new LinearLayoutManager(this));

        loadMessages();
        setUpSend();
        receiver = getIntent().getStringExtra(Intent.EXTRA_DATA_REMOVED);
//        getActionBar().setTitle(receiver);

    }

    @Override
    protected void onStart() {
        super.onStart();
//        loadMessages();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isRunning = false;
    }

    public void setUpSend(){
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.btSend){
                    sendMessage();
                }
            }
        });
    }

    public void loadMessages() {
        gettingMessagesFromParse(contactingParse());
    }


    public ParseQuery contactingParse() {
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Message");
        if (mMessages.size() == 0) {
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
        return parseQuery;
    }

    public void gettingMessagesFromParse(ParseQuery parseQuery) {
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
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        isRunning = true;

    }


    private void sendMessage() {

        if (etMessage.length() == 0)
            return;

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(etMessage.getWindowToken(), 0);

        addNewMessageToParse();
    }

        private void addNewMessageToParse() { //changename
            String messageToBeSent = etMessage.getText().toString();
            final PearMessage pearMessage = new PearMessage(messageToBeSent, new Date(), user.getUsername());
            pearMessage.setStatus(PearMessage.STATUS_SENDING);
            mMessages.add(pearMessage);
            mAdapter.notifyDataSetChanged();
            etMessage.setText(null);
            addingToParse(messageToBeSent).saveInBackground(new SaveCallback() {
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

        public ParseObject addingToParse(String messageToBeSent) {
            ParseObject parseObject = new ParseObject("Message");
            parseObject.put("messageAuthor", user.getUsername());
            parseObject.put("messageReceiver", receiver);
            parseObject.put("body", messageToBeSent);
            return parseObject;
        }

    }
