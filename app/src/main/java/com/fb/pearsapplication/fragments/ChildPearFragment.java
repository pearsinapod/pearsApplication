package com.fb.pearsapplication.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fb.pearsapplication.ChatActivity;
import com.fb.pearsapplication.R;
import com.fb.pearsapplication.conversationsActivity;
import com.fb.pearsapplication.models.Pear;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

public class ChildPearFragment extends Fragment {
    TextView tvCurrentPear;
    ImageView ivPearPic;
    TextView tvPearName;
    Button btnMessage;
    Button btnViewProfile;
    Switch swPear;

    Pear pear;
    ParseUser pearUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Need to define the child fragment layout
        return inflater.inflate(R.layout.fragment_child_pear, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvCurrentPear = (TextView) view.findViewById(R.id.tvCurrentPear);
        ivPearPic = (ImageView) view.findViewById(R.id.ivPearPic);
        tvPearName = (TextView) view.findViewById(R.id.tvPearName);
        btnMessage = (Button) view.findViewById(R.id.btnMessage);
        btnViewProfile = (Button) view.findViewById(R.id.btnViewProfile);
        bindViews();
        setOnClickListeners();
    }

    private void bindViews() {
        groupDetailsFragment parentFrag = ((groupDetailsFragment)ChildPearFragment.this.getParentFragment());
        swPear = parentFrag.swPear;
        swPear.setChecked(false);
        swPear.setEnabled(false);
        swPear.setClickable(false);
        String name = "";
        try {
            name = pearUser.fetchIfNeeded().getString("username");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tvPearName.setText(name);
        ParseFile profileImage = pearUser.getParseFile("profileImage");
        String profileImageString = pearUser.getString("profilePicString");
        if (profileImage != null) {
            Glide.with(getContext()).load(profileImage.getUrl()).apply(RequestOptions.circleCropTransform()).into(ivPearPic);
        } else if (profileImageString != null) {
            Glide.with(getContext()).load(profileImageString).apply(RequestOptions.circleCropTransform()).into(ivPearPic);
        } else {
            Glide.with(getContext()).load(R.drawable.user).apply(RequestOptions.circleCropTransform()).into(ivPearPic);
        }
    }

    public void setOnClickListeners() {
        btnViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                groupDetailsFragment parentFrag = ((groupDetailsFragment)ChildPearFragment.this.getParentFragment());
                FragmentManager fragmentManager = parentFrag.getFragmentManager();
                Fragment fragment = new matchProfileFragment();
                ((matchProfileFragment) fragment).setPearUser(pear.getOtherUser());
                ((matchProfileFragment) fragment).setGroup(pear.getGroup());
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left )
                        .replace(R.id.flContainter, fragment).addToBackStack(null).commit();
            }
        });

        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chatIntent = new Intent(getActivity(), conversationsActivity.class);
                startActivity(chatIntent);
            }
        });
    }

    public void setPear(Pear pear) {
        this.pear = pear;
        pearUser = pear.getOtherUser();
    }

}
