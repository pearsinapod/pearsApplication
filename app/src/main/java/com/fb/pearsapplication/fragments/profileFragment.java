package com.fb.pearsapplication.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class profileFragment extends Fragment {

    public ImageView ivImage;
    public TextView tvName;
    public TextView tvDescription;
    public ImageButton btnEdit;
    public EditText etDescription;
    public ImageButton btnDone;
    public ImageButton btnAddPhoto;

    ParseUser user;
    Uri photoUri;

    public final static int PICK_PHOTO_CODE = 1046;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = ParseUser.getCurrentUser();

        ivImage = view.findViewById(R.id.ivProfileOther);
        tvName = view.findViewById(R.id.tvName);
        tvDescription = view.findViewById(R.id.tvDescription);
        etDescription = view.findViewById(R.id.etDescription);
        btnEdit = view.findViewById(R.id.btnEdit);
        btnDone = view.findViewById(R.id.btnDone);
        btnAddPhoto = view.findViewById(R.id.btnAddPhoto);

        etDescription.setVisibility(View.GONE);
        btnDone.setVisibility(View.GONE);

        bindViews();

        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPickPhoto(view);
            }
        });

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

    private void bindViews() {
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
            Glide.with(getContext()).load(profileImage.getUrl()).apply(RequestOptions.circleCropTransform()).into(ivImage);
        } else if (profileImageString != null) {
            Glide.with(getContext()).load(profileImageString).apply(RequestOptions.circleCropTransform()).into(ivImage);
        } else {
            Glide.with(getContext()).load(R.drawable.user).apply(RequestOptions.circleCropTransform()).into(ivImage);
        }
    }

    public void onPickPhoto(View view) {
        // Create intent for picking a photo from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Bring up gallery to select a photo
            startActivityForResult(intent, PICK_PHOTO_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            photoUri = data.getData();
            // Do something with the photo based on Uri
            Bitmap selectedImage = null;
            try {
                selectedImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Load the selected image into a preview
            ivImage.setImageBitmap(selectedImage);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
            byte[] imageByte = byteArrayOutputStream.toByteArray();
            ParseFile parseFile = new ParseFile("image_file" + user.getObjectId() + ".jpeg", imageByte);
            saveUser(parseFile);
        }
    }

    private void saveUser(ParseFile file) {
        user.put("profileImage", file);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.d("XYZ", "error");
                    e.printStackTrace();
                } else {
                    Log.d("XYZ", "success!");
                }
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
