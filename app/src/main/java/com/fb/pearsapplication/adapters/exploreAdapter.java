package com.fb.pearsapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
        TextView tvExploreDescription = convertView.findViewById(R.id.tvExploreDescription);
        final CardView cardViewExplore = convertView.findViewById(R.id.cardViewExplore);
        ImageView ivExploreImage = convertView.findViewById(R.id.ivExploreImage);
        //cardViewExplore.setBackground


        tvExploreDescription.setText(group.getDescription());
        tvExploreDescription.setBackgroundColor(0000);
        tvExploreName.setText(group.getGroupName());
        tvExploreName.setBackgroundColor(0000);


       // ivExploreImage.setVisibility(View.GONE);


        ParseFile image = group.getGroupImage();
        if (image!=null){
            Glide.with(getContext()).load(image.getUrl()).into(ivExploreImage);
/*            Glide.with(getContext()).load(image.getUrl()).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        cardViewExplore.setBackground(resource);
                    }
                }
            });*/
        }

/*        ParseFile image = group.getGroupImage();
        if (image!=null){
            Glide.with(getContext())
                    .load(group.getGroupImage().getUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(ivExploreImage);
        }*/
        return convertView;

    }
}
