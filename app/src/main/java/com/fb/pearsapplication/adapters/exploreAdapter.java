package com.fb.pearsapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fb.pearsapplication.R;
import com.fb.pearsapplication.models.Group;
import com.parse.ParseFile;

import java.util.List;

// why do u need this? is this necessary?
public class exploreAdapter extends ArrayAdapter<Group> {
    Context context;

    public exploreAdapter(Context context, int resourceId, List<Group> groups){
        super(context, resourceId, groups);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Group group = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_explore_group, parent,false);

        }
        ImageView ivExploreImage = convertView.findViewById(R.id.ivExploreImage);
        TextView tvExploreName = convertView.findViewById(R.id.tvExploreName);
        TextView tvExploreDescription = convertView.findViewById(R.id.tvExploreDescription);

        tvExploreDescription.setText(group.getDescription());
        tvExploreName.setText(group.getGroupName());

        ParseFile image = group.getGroupImage();
        if (image!=null){
            Glide.with(getContext())
                    .load(group.getGroupImage().getUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(ivExploreImage);
        }
        return convertView;

    }
}
