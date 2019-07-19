package com.fb.pearsapplication.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fb.pearsapplication.R;
import com.fb.pearsapplication.models.Group;
import com.parse.ParseFile;

public class groupDetailsFragment extends Fragment {

    // the post to display
    Group group;
    Context context;

    // the view objects
    ImageView ivGroupImage;
    TextView tvGroupName;
    TextView tvGroupNumber;
    TextView tvDescription;
    Button btnMatch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        group = (Group) getArguments().getSerializable("anything");
        return inflater.inflate(R.layout.fragment_group_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivGroupImage = (ImageView) view.findViewById(R.id.ivGroupImage);
        tvGroupName = (TextView) view.findViewById(R.id.tvGroupName);
        tvDescription = (TextView) view.findViewById(R.id.tvDescription);
        tvGroupNumber = (TextView) view.findViewById(R.id.tvGroupNumber);

        tvGroupName.setText(group.getGroupName());
        ParseFile image = group.getGroupImage();
        if (image != null) {
            Glide.with(getContext()).load(image.getUrl()).apply(RequestOptions.circleCropTransform()).into(ivGroupImage);
        }
        if (group.getUsers() != null) {
            Integer size = group.getUsers().size();
            String sizeString = size.toString();
            tvGroupNumber.setText(sizeString);
        }
        tvDescription.setText(group.getDescription());
        String timeAgo = group.getRelativeTimeAgo();

//        btnMatch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                insertNestedFragment();
//
//            }
//        });
    }
    // Embeds the child fragment dynamically
    private void insertNestedFragment() {
        Fragment childFragment = new ChildFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.child_fragment_container, childFragment).commit();
    }

}

class ChildFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Need to define the child fragment layout
        return inflater.inflate(R.layout.fragment_child, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

}


