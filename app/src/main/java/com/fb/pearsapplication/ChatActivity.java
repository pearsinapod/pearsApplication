package com.fb.pearsapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fb.pearsapplication.EndlessRecyclerViewScrollListener;
import com.fb.pearsapplication.adapters.ChatAdapter;
import com.fb.pearsapplication.adapters.conversationsAdapter;
import com.fb.pearsapplication.models.Group;
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

import static com.parse.Parse.getApplicationContext;

public class ChatActivity extends AppCompatActivity {

    private ArrayList<PearMessage> mMessages;
    private ArrayList<ParseUser> userList;
    public static ParseUser user;
    private ChatAdapter mAdapter;
    private EditText etMessage;
    private ImageButton btSend;
    public String receiver;
    private Date lastMessageDate;
    private RecyclerView rvChat;
    private TextView tvReceiver;
    private Boolean isRunning;
    androidx.appcompat.widget.Toolbar toolbar;
    private EndlessRecyclerViewScrollListener scrollListener;

    static final int POLL_INTERVAL = 1000;
    Handler myHandler = new Handler();
    Runnable mRefreshMessagesRunnable = new Runnable() {
        @Override
        public void run() {
            mMessages.clear();
            loadMessages();
            myHandler.postDelayed(this, POLL_INTERVAL);
            int oldCount = mMessages.size();
            if (rvChat.isComputingLayout())
                mAdapter.notifyItemRangeChanged(oldCount - 1, mMessages.size());
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
        btSend = (ImageButton) findViewById(R.id.btSend);

        etMessage = (EditText) findViewById(R.id.etMessage);
        etMessage.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);




        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvChat.setLayoutManager(new WrapContentLinearLayoutManager(getApplicationContext()));
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
            mRefreshMessagesRunnable.run();
            }
        };


            setUpSend();
        receiver = getIntent().getStringExtra(Intent.EXTRA_DATA_REMOVED);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(receiver);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


    }

    public class WrapContentLinearLayoutManager extends LinearLayoutManager {
        public WrapContentLinearLayoutManager(Context context) {
            super(context);
        }
        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
                Log.d("OnLayoutChildren" ,"worked");
            } catch (IndexOutOfBoundsException e) {
                Log.e("Error", "IndexOutOfBoundsException in RecyclerView happens");
            }
        }
    }

//    public class WrapContentLinearLayoutManager extends LinearLayoutManager {
//        public WrapContentLinearLayoutManager(Context context) {
//            super(context);
//        }
//        @Override
//        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
//            try {
//                super.onLayoutChildren(recycler, state);
//            } catch (IndexOutOfBoundsException e) {
//                Log.e("TAG", "meet a IOOBE in RecyclerView");
//            }
//        }
//    }

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
        //mAdapter.notifyDataSetChanged();
    }

    public void contactingParseforBody() {
        ParseQuery<PearMessage> bodyQuery = ParseQuery.getQuery(PearMessage.class);
        bodyQuery.orderByDescending("body");
        bodyQuery.findInBackground(new FindCallback<PearMessage>() {
            @Override
            public void done(List<PearMessage> objects, ParseException e) {
                String[] words = new String[objects.size()];
                if (objects != null && objects.size() > 0) {
                    for (int i = objects.size() - 1; i >= 0 ; i--) {
                        String body = objects.get(i).getBody();
                        words[i] = body;
                    }
                }
            }
        });
    }
    public ParseQuery contactingParse() {
        ParseQuery<ParseObject> messageQuery = ParseQuery.getQuery("Message");
        if (mMessages.size() == 0) {
            ArrayList<String> ArrayList = new ArrayList<String>();
            ArrayList.add(receiver);
            ArrayList.add(user.getUsername());
            messageQuery.whereContainedIn("messageAuthor", ArrayList);
            messageQuery.whereContainedIn("messageReceiver", ArrayList);
        } else {
            if (lastMessageDate != null)
                messageQuery.whereGreaterThan("createdAt", lastMessageDate);
            messageQuery.whereEqualTo("messageAuthor", user.getUsername());
            messageQuery.whereEqualTo("messageReceiver", receiver);
        }
        return messageQuery;
    }

    public void gettingMessagesFromParse(ParseQuery messageQuery) {
       // mAdapter.clear();
        messageQuery.orderByDescending("createdAt");
        messageQuery.setLimit(10);
        if (mMessages.size()>0){
            messageQuery.whereLessThan(Group.KEY_CREATED_AT, mMessages.get(mMessages.size()-1).getCreatedAt());
        }
        messageQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (list != null && list.size() > 0){
                    for (int i = list.size() - 1; i >= 0; i--){
                        ParseObject oldMessage = list.get(i);
                        PearMessage pearMessage = new PearMessage(
                                oldMessage.getString("body"),
                                oldMessage.getCreatedAt(),
                                oldMessage.getString("messageAuthor"));
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
        isRunning = false;

    }


    private void sendMessage() {

        if (etMessage.length() == 0)
            return;

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(etMessage.getWindowToken(), 0);

        addNewMessage();
    }

        private void addNewMessage() {
            String messageToBeSent = etMessage.getText().toString();
            final PearMessage newPearMessage = new PearMessage(messageToBeSent, new Date(), user.getUsername());
            newPearMessage.setStatus(PearMessage.STATUS_SENDING);
            mMessages.add(newPearMessage);
            mAdapter.notifyDataSetChanged();
            etMessage.setText(null);
            addingToParse(messageToBeSent).saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null)
                        newPearMessage.setStatus(PearMessage.STATUS_SENT);
                    else
                        newPearMessage.setStatus(PearMessage.STATUS_FAILED);
                    mAdapter.notifyDataSetChanged();
                }
            });
        }

        public ParseObject addingToParse(String messageToBeSent) {
            ParseObject newMessage = new ParseObject("Message");
            newMessage.put("messageAuthor", user.getUsername());
            newMessage.put("messageReceiver", receiver);
            newMessage.put("body", messageToBeSent);
            return newMessage;
        }

    }
