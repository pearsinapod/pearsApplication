package com.fb.pearsapplication.adapters;

import android.content.Context;
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

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    public List<PearMessage> mMessages;
    private Context context;

    public ChatAdapter(ArrayList<PearMessage> mMessages) {

        this.mMessages = mMessages;
    }

    public int getCount() {
        return mMessages.size();
    }

    public PearMessage getItem(int pos) {
        return mMessages.get(pos);
    }


    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_chat, parent, false);

        ChatAdapter.ViewHolder viewHolder = new ChatAdapter.ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {

        PearMessage pearMessage = mMessages.get(position);
        holder.bind(pearMessage);
    }

    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public void updateMessages (List<PearMessage> messages) {
        if (messages != null && messages.size() > 0){
            mMessages.addAll(messages);
            notifyDataSetChanged();
        }
    }

    public View getView(int pos, View view, ViewGroup arg2){
        PearMessage pearMessage = getItem(pos);
        if (pearMessage.isSent())
            view = LayoutInflater.from(context).inflate(R.layout.item_chat, arg2);
        else
            view = LayoutInflater.from(context).inflate(R.layout.item_chat, arg2);

    TextView tvBody = (TextView) view.findViewById(R.id.tvBody);
    tvBody.setText(pearMessage.getBody());
    return view;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvBody;
        TextView tvUsername;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
        }

        public void bind(PearMessage pearMessage) {
            tvBody.setText(pearMessage.getBody());

        }
    }
}
