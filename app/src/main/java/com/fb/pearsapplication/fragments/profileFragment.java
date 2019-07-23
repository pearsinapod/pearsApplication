package com.fb.pearsapplication.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fb.pearsapplication.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class profileFragment extends Fragment {

    public ImageView ivImage;
    public TextView tvName;
    public TextView tvDescription;
    public ImageButton btnEdit;
    public EditText etDescription;
    public ImageButton btnDone;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ParseUser user = ParseUser.getCurrentUser();

        ivImage = view.findViewById(R.id.ivProfileOther);
        tvName = view.findViewById(R.id.tvName);
        tvDescription = view.findViewById(R.id.tvDescription);
        etDescription = view.findViewById(R.id.etDescription);
        btnEdit = view.findViewById(R.id.btnEdit);
        btnDone = view.findViewById(R.id.btnDone);

        etDescription.setVisibility(View.GONE);
        btnDone.setVisibility(View.GONE);

        if (user.getNumber("age") != null) {
            tvName.setText(user.getUsername() + ", " + user.getNumber("age"));
        } else {
            tvName.setText(user.getUsername());
        }
        String description = user.getString("description");
        if (description != null) {
            tvDescription.setText(description);
        } else {
            tvDescription.setText("How would your best friend describe you?");
        }

        ParseFile profileImage = user.getParseFile("profileImage");
        String profileImageString = user.getString("profilePicString");
        if (profileImage != null) {
            Glide.with(getContext()).load(profileImage).apply(RequestOptions.circleCropTransform()).into(ivImage);
        } else if (profileImageString != null) {
            Glide.with(getContext()).load(profileImageString).apply(RequestOptions.circleCropTransform()).into(ivImage);
        } else {
            Glide.with(getContext()).load(R.drawable.user).apply(RequestOptions.circleCropTransform()).into(ivImage);
        }

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvDescription.setVisibility(View.GONE);
                btnEdit.setVisibility(View.GONE);
                etDescription.setVisibility(View.VISIBLE);
                btnDone.setVisibility(View.VISIBLE);
                etDescription.setText(ParseUser.getCurrentUser().getString("description"));
                btnDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        changeDescription();
                    }
                });
            }
        });
    }

    private void changeDescription() {
        final String description = etDescription.getText().toString();
        ParseUser user = ParseUser.getCurrentUser();
        user.put("description", description);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    etDescription.setVisibility(View.GONE);
                    btnDone.setVisibility(View.GONE);
                    tvDescription.setVisibility(View.VISIBLE);
                    btnEdit.setVisibility(View.VISIBLE);
                    tvDescription.setText(description);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
