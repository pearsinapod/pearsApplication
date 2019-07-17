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

import com.bumptech.glide.Glide;
import com.fb.pearsapplication.R;
import com.fb.pearsapplication.fragments.groupDetailsFragment;
import com.fb.pearsapplication.models.Group;
import com.parse.ParseFile;

import java.util.List;

public class groupsAdapter extends RecyclerView.Adapter<groupsAdapter.ViewHolder>{

    private Context context;
    private List<Group> groups;

    public groupsAdapter(Context context, List<Group> groups) {
        this.context = context;
        this.groups = groups;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_group, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Group group = groups.get(position);
        holder.bind(group);
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }



    // Clean all elements of the recycler
    public void clear() {
        groups.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Group> list) {
        groups.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //track view objects
        private TextView handle_tv;
        private ImageView image_iv;
        private TextView description_tv;
        private TextView timestamp_tv;

        public ViewHolder(View itemView) {
            super(itemView);
            // lookup view objects by id
            // TODO handle_tv = itemView.findViewById(R.id.handle_tv);
            // TODO image_iv = itemView.findViewById(R.id.image_iv);
            // TODO description_tv = itemView.findViewById(R.id.description_tv);
            // TODO timestamp_tv = itemView.findViewById(R.id.timestamp_tv);
            // add this as the itemView's OnClickListener

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    // make sure the position is valid, i.e. actually exists in the view
                    if (position != RecyclerView.NO_POSITION) {
                        // get the group at the position, this won't work if the class is static
                        Group post = groups.get(position);
                        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("anything", post);
                        Fragment fragment = new groupDetailsFragment();
                        fragment.setArguments(bundle);

                        fragmentManager.beginTransaction().replace(R.id.flContainter, fragment).addToBackStack(null).commit();
                    }
                }
            });
        }

        public void bind(Group group) {
            handle_tv.setText(group.getUser().getUsername());
            ParseFile image = group.getImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(image_iv);
            }
            description_tv.setText(group.getDescription());
            String timeAgo = group.getRelativeTimeAgo();
            timestamp_tv.setText(timeAgo);
        }


    }
}
