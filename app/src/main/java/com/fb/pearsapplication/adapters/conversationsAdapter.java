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

import com.fb.pearsapplication.ChatActivity;
import com.fb.pearsapplication.R;
import com.fb.pearsapplication.models.Pear;
import com.parse.ParseUser;

import java.util.List;

public class conversationsAdapter extends RecyclerView.Adapter<conversationsAdapter.ViewHolder> {

    private List<Pear> pearList;
    Context context;

    public conversationsAdapter(List<Pear> pearList) {
        this.pearList = pearList;

    }
    public Pear getItem(int adapterView){
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

        Pear pear = pearList.get(position);
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
            Intent convoIntent = new Intent(context, ChatActivity.class);
            convoIntent.putExtra(convoIntent.EXTRA_DATA_REMOVED, pearList.get(position).getOtherUser().getUsername());
            context.startActivity(convoIntent);
        }

        public void bind(Pear pear) {
            tvUsername.setText(pear.getOtherUser().getUsername());
        }
    }
}
