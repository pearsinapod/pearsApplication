package com.fb.pearsapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fb.pearsapplication.ChatActivity;
import com.fb.pearsapplication.R;
import com.fb.pearsapplication.models.PearMessage;
import com.parse.ParseUser;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class conversationsAdapter extends RecyclerView.Adapter<conversationsAdapter.ViewHolder> {

    private ArrayList<ParseUser> userList;

    public conversationsAdapter() {

    }

    @Override
    public int getCount(){
        return userList.size();
    }

    @Override
    public ParseUser getItem(int adapterView){
        return userList.get(adapterView);
    }

    @Override
    public long getItemId(int adapterView){
        return adapterView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_conversation, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;

    }





    private List<PearMessage> mMessages;
    private Context mContext;
    private String mUserId;

    public conversationsAdapter(Context context, String userId, List<PearMessage> messages) {
        mMessages = messages;
        this.mUserId = userId;
        mContext = context;
    }




    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PearMessage message = mMessages.get(position);
        final boolean isMe = message.getUserId() != null && message.getUserId().equals(mUserId);

        if (isMe) {
            holder.imageMe.setVisibility(View.VISIBLE);
            holder.imageOther.setVisibility(View.GONE);
            holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        } else {
            holder.imageOther.setVisibility(View.VISIBLE);
            holder.imageMe.setVisibility(View.GONE);
            holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        }

        final ImageView profileView = isMe ? holder.imageMe : holder.imageOther;
        Glide.with(mContext).load(getProfileUrl(message.getUserId())).into(profileView);
        holder.body.setText(message.getBody());

    }

    // Create a gravatar image based on the hash value obtained from userId
    private static String getProfileUrl(final String userId) {
        String hex = "";
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            final byte[] hash = digest.digest(userId.getBytes());
            final BigInteger bigInt = new BigInteger(hash);
            hex = bigInt.abs().toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "https://www.gravatar.com/avatar/" + hex + "?d=identicon";
    }


    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageOther;
        ImageView imageMe;
        TextView body;

        public ViewHolder(View itemView) {
            super(itemView);
            imageOther = (ImageView)itemView.findViewById(R.id.ivProfileOther);
            imageMe = (ImageView)itemView.findViewById(R.id.ivProfileMe);
            body = (TextView)itemView.findViewById(R.id.tvBody);
        }

    }

    public void onClick(View view) {
        //gets item position
        int position = getAdapterPosition();
        // make sure the position is valid, i.e. actually exists in the view
        if (position != RecyclerView.NO_POSITION) {
            PearMessage message = mMessages.get(position);
            Intent chatIntent = new Intent (mContext, ChatActivity.class);
            mContext.startActivity(chatIntent);
        }
    }

    private int getAdapterPosition() {
       return getAdapterPosition();
    }

}
