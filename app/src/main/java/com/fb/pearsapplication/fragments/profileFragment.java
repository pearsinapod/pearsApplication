package com.fb.pearsapplication.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fb.pearsapplication.LoginActivity;
import com.fb.pearsapplication.MainActivity;
import com.fb.pearsapplication.R;
import com.parse.ParseFile;
import com.parse.ParseUser;

public class profileFragment extends Fragment {

    public ImageView ivImage;
    public TextView tvName;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ParseUser user = ParseUser.getCurrentUser();

        ivImage = view.findViewById(R.id.ivImage);
        tvName = view.findViewById(R.id.tvName);

        tvName.setText(user.getUsername());
        ParseFile profileImage = user.getParseFile("profileImage");
        if (profileImage != null) {
            Glide.with(getContext()).load(profileImage.getUrl()).apply(RequestOptions.circleCropTransform()).into(ivImage);
        }
    }
}
