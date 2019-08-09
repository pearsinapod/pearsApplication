package com.fb.pearsapplication.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
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

        TextView tvExploreName = convertView.findViewById(R.id.tvExploreName);
        final TextView tvExploreDescription = convertView.findViewById(R.id.tvExploreDescription);
        final CardView cardViewExplore = convertView.findViewById(R.id.cardViewExplore);
        ImageView ivExploreImage = convertView.findViewById(R.id.ivExploreImage);
        ImageButton btnDetails = convertView.findViewById(R.id.btnDetails);


        tvExploreDescription.setText(group.getDescription());
        tvExploreName.setText(group.getGroupName());
        cardViewExplore.setCardBackgroundColor(Color.parseColor("#F0F0F0"));
        tvExploreDescription.setBackgroundColor(Color.parseColor("#F0F0F0"));
        tvExploreName.setBackgroundColor(Color.parseColor("#F0F0F0"));

        btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater factory = LayoutInflater.from(getContext());
                final View alertView = factory.inflate(R.layout.explore_group_details, null);
                TextView tvGroupName = alertView.findViewById(R.id.tvGroupName);
                TextView tvDescription = alertView.findViewById(R.id.tvDescription);
                tvGroupName.setText(group.getGroupName());
                tvDescription.setText(group.getDescription());
                new AlertDialog.Builder(getContext())
                        .setView(alertView)
                        .show();
            }
        });


        ParseFile image = group.getGroupImage();
/*        if (image!=null){
           // Glide.with(getContext()).load(image.getUrl()).into(ivExploreImage);
            Glide.with(getContext()).load(image.getUrl()).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        cardViewExplore.setBackground(resource);
                    }
                }
            });
        }*/
    if (image!=null){
            Glide.with(getContext()).load(group.getGroupImage().getUrl()).into(ivExploreImage);

        }
        else{
            Glide.with(getContext()) .load(R.drawable.group_search_placeholder) .into(ivExploreImage);
        }
        return convertView;

    }
}
