package com.fb.pearsapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fb.pearsapplication.R;
import com.fb.pearsapplication.models.PearMessage;
import com.fb.pearsapplication.models.User;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final int SEND = 0, RECEIVE = 1;

    public List<PearMessage> mMessages;
    private Context context;

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mMessages.get(position).getSender().equals(ParseUser.getCurrentUser().getUsername())) {
            return SEND;
        } else {
            return RECEIVE;
        }
    }


    public ChatAdapter(ArrayList<PearMessage> mMessages) {

        this.mMessages = mMessages;
    }

    public int getCount() {
        return mMessages.size();
    }

    public PearMessage getItem(int pos) {
        return mMessages.get(pos);
    }

    public void clear() {
        mMessages.clear();
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        switch (viewType) {
            case SEND:
                View view1 = inflater.inflate(R.layout.item_chat_sent, parent, false);
                viewHolder = new SendViewHolder(view1);
                break;
            default:
                View view2 = inflater.inflate(R.layout.item_chat_received, parent, false);
                viewHolder = new ReceiveViewHolder(view2);
                break;
        }
        return viewHolder;

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case SEND:
                SendViewHolder sendView = (SendViewHolder) holder;
                sendView.bind(mMessages.get(position));
                break;
            case RECEIVE:
                ReceiveViewHolder receiveView = (ReceiveViewHolder) holder;
                receiveView.bind(mMessages.get(position));
        }
    }

    public long getItemId(int pos) {
        return pos;
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
            view = LayoutInflater.from(context).inflate(R.layout.item_chat_sent, arg2);
        else
            view = LayoutInflater.from(context).inflate(R.layout.item_chat_received, arg2);

    TextView tvBody = (TextView) view.findViewById(R.id.tvBody);
    tvBody.setText(pearMessage.getBody());
    return view;
    }

    public class SendViewHolder extends RecyclerView.ViewHolder {

        TextView tvBody;
        TextView tvUsername;

        public SendViewHolder(@NonNull View itemView) {
            super(itemView);

            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
        }

        public void bind(PearMessage pearMessage) {
            tvBody.setText(pearMessage.getBody());

        }
    }

    public class ReceiveViewHolder extends RecyclerView.ViewHolder {

        TextView tvBody;
        TextView tvUsername;

        public ReceiveViewHolder(@NonNull View itemView) {
            super(itemView);

            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
        }

        public void bind(PearMessage pearMessage) {
            tvBody.setText(pearMessage.getBody());

        }
    }
}
