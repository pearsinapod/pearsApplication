package com.fb.pearsapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fb.pearsapplication.ChatActivity;
import com.fb.pearsapplication.R;
import com.fb.pearsapplication.models.Pear;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

public class conversationsAdapter extends RecyclerView.Adapter<conversationsAdapter.ViewHolder> {

    private List<ParseUser> pearList;
    Context context;

    public conversationsAdapter(List<ParseUser> pearList) {
        this.pearList = pearList;

    }
    public ParseUser getItem(int adapterView){
        return pearList.get(adapterView);
    }

    @Override
    public long getItemId(int adapterView){
        return adapterView;
    }

    @Override
    public int getItemCount() {
        return pearList.size();
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

        ParseUser pear = pearList.get(position);
        holder.bind(pear);
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
            Intent chatIntent = new Intent(context, ChatActivity.class);
            chatIntent.putExtra(chatIntent.EXTRA_DATA_REMOVED, pearList.get(position).getUsername());
            context.startActivity(chatIntent);
        }


        public void bind(ParseUser otherUser) {
            String name = "";
            try {
                name = otherUser.fetchIfNeeded().getString("username");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            tvUsername.setText(otherUser.getUsername());
            ParseFile profileImage = otherUser.getParseFile("profileImage");
            String profileImageString = otherUser.getString("profilePicString");
            if (profileImage != null) {
                Glide.with(context).load(profileImage.getUrl()).apply(RequestOptions.circleCropTransform()).into(ivProfileOther);
            } else if (profileImageString != null) {
                Glide.with(context).load(profileImageString).apply(RequestOptions.circleCropTransform()).into(ivProfileOther);
            } else {
                Glide.with(context).load(R.drawable.user).apply(RequestOptions.circleCropTransform()).into(ivProfileOther);
            }
        }
    }
}
