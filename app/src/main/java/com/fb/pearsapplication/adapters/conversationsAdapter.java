package com.fb.pearsapplication.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fb.pearsapplication.R;
import com.fb.pearsapplication.fragments.messageFragment;
import com.fb.pearsapplication.models.PearMessage;

import java.util.List;

public class conversationsAdapter extends RecyclerView.Adapter<conversationsAdapter.ViewHolder> {


   Context context;
   List<PearMessage> conversations;

    public conversationsAdapter(@NonNull Context context, List<PearMessage> conversations) {
        this.context = context;
        this.conversations = conversations;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_conversation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PearMessage conversation = conversations.get(position);
        holder.bind(conversation);
    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }



    // Clean all elements of the recycler
    public void clear() {
        conversations.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<PearMessage> list) {
        conversations.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //track view objects
        private ImageView ivProfileOther;
        private TextView tvName;
        private TextView tvBody;

        public ViewHolder(View itemView) {
            super(itemView);
            // lookup view objects by id

            ivProfileOther = itemView.findViewById(R.id.ivProfileOther);
            tvName = itemView.findViewById(R.id.tvName);
            tvBody = itemView.findViewById(R.id.tvBody);


            // add this as the itemView's OnClickListener

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    // make sure the position is valid, i.e. actually exists in the view
                    if (position != RecyclerView.NO_POSITION) {
                        // get the movie at the position, this won't work if the class is static
                        PearMessage conversation = conversations.get(position);
                        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("anything", conversation);
                        Fragment fragment = new messageFragment();
                        fragment.setArguments(bundle);

                        fragmentManager.beginTransaction().replace(R.id.flContainter, fragment).addToBackStack(null).commit();
                    }
                }
            });
        }

        public void bind(PearMessage conversation) {
            tvName.setText(conversation.getUserId());
            //ParseFile image = conversation.getImage();
            //if (image != null) {
            //    Glide.with(context).load(image.getUrl()).into(ivProfileOther);
            //}
            tvBody.setText(conversation.getBody());
        }


    }
}
