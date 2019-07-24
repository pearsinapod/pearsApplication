package com.fb.pearsapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fb.pearsapplication.ChatActivity;
import com.fb.pearsapplication.R;
import com.fb.pearsapplication.conversationsActivity;
import com.fb.pearsapplication.models.PearMessage;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class conversationsAdapter extends RecyclerView.Adapter<conversationsAdapter.ViewHolder> {

    private List<ParseUser> userList;
    Context context;

    public conversationsAdapter(List<ParseUser> userList) {
        this.userList = userList;

    }
    public ParseUser getItem(int adapterView){
        return userList.get(adapterView);
    }

    @Override
    public long getItemId(int adapterView){
        return adapterView;
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_conversation, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ParseUser parseUser = userList.get(position);
        holder.bind(parseUser);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvBody;
        TextView tvUsername;
        ImageView ivProfileOther;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            ivProfileOther = (ImageView) itemView.findViewById(R.id.ivProfileOther);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Intent convoIntent = new Intent(context, ChatActivity.class);
            convoIntent.putExtra(convoIntent.EXTRA_DATA_REMOVED, userList.get(position).getUsername());
            context.startActivity(convoIntent);
        }

        public void bind(ParseUser parseUser) {
            tvUsername.setText(parseUser.getUsername());
        }
    }
}
