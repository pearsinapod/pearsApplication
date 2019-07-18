package com.fb.pearsapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fb.pearsapplication.R;
import com.fb.pearsapplication.models.Group;
import com.parse.ParseFile;

import java.util.List;

public class exploreAdapter extends RecyclerView.Adapter<exploreAdapter.ViewHolder> {
    private List<Group> mGroups;
    private Context context;

    public exploreAdapter(List groups){
            mGroups=groups;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View exploreView = LayoutInflater.from(context).inflate(R.layout.item_explore_group,parent,false);
        ViewHolder viewHolder =  new ViewHolder(exploreView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Group group = mGroups.get(position);
        holder.tvExploreName.setText(group.getGroupName());
        ParseFile image = group.getGroupImage();
        if (image!=null){
            Glide.with(context)
                    .load(image.getUrl())
                    .into(holder.ivExploreImage);
        }
    }


    @Override
    public int getItemCount() {
        return mGroups.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvExploreName;
        public ImageView ivExploreImage;
        public ViewHolder(View itemView){
            super(itemView);
            tvExploreName= itemView.findViewById(R.id.tvExploreName);
            ivExploreImage = itemView.findViewById(R.id.ivExploreImage);

        }
    }
}
