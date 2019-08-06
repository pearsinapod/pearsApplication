package com.fb.pearsapplication.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fb.pearsapplication.R;
import com.fb.pearsapplication.models.Question;
import com.fb.pearsapplication.models.UserQuestion;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

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
    Question dailyQuestion;
    FragmentManager childFragmentManager;

    public final static int PICK_PHOTO_CODE = 1046;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        childFragmentManager = getChildFragmentManager();
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = ParseUser.getCurrentUser();
        findViews(view);
        bindViews();
        etDescription.setVisibility(View.GONE);
        btnDone.setVisibility(View.GONE);
        findDailyQuestion();
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

    private void findViews(View view) {
        ivImage = view.findViewById(R.id.ivProfileOther);
        tvName = view.findViewById(R.id.tvName);
        tvDescription = view.findViewById(R.id.tvDescription);
        etDescription = view.findViewById(R.id.etDescription);
        btnEdit = view.findViewById(R.id.btnEdit);
        btnDone = view.findViewById(R.id.btnDone);
        btnAddPhoto = view.findViewById(R.id.btnAddPhoto);
    }

    private void bindViews() {
        if (user.getNumber("age") != null) {
            tvName.setText(user.getUsername() + ", " + user.getNumber("age"));
        } else {
            tvName.setText(user.getUsername());
        }
        String description = user.getString("description");
        if (!description.equals("")) {
            tvDescription.setText(description);
        } else {
            tvDescription.setText("Add a description!");
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
//                selectedImage = getCorrectlyOrientedImage(getContext(), photoUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Load the selected image into a preview
            Glide.with(getContext()).load(selectedImage).apply(RequestOptions.circleCropTransform()).into(ivImage);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
            byte[] imageByte = byteArrayOutputStream.toByteArray();
            ParseFile parseFile = new ParseFile("image_file" + user.getObjectId() + ".jpeg", imageByte);
            saveUser(parseFile);
        }
    }

    public static int getOrientation(Context context, Uri photoUri) {
        /* it's on the external media. */
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[] { MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);

        if (cursor.getCount() != 1) {
            return -1;
        }

        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public static Bitmap getCorrectlyOrientedImage(Context context, Uri photoUri) throws IOException {
        InputStream is = context.getContentResolver().openInputStream(photoUri);
        BitmapFactory.Options dbo = new BitmapFactory.Options();
        dbo.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, dbo);
        Bitmap srcBitmap = BitmapFactory.decodeStream(is);
        is.close();

        int orientation = getOrientation(context, photoUri);
        if (orientation > 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);

            srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(),
                    srcBitmap.getHeight(), matrix, true);
        }
        return srcBitmap;
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

    public void findDailyQuestion() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date today = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date tomorrow = cal.getTime();

        ParseQuery<Question> questionQuery = new ParseQuery<Question>(Question.class);
        questionQuery.whereGreaterThanOrEqualTo("targetDate", today);
        questionQuery.whereLessThan("targetDate", tomorrow);
        questionQuery.findInBackground(new FindCallback<Question>() {
            @Override
            public void done(List<Question> objects, ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                } else {
                    dailyQuestion = objects.get(0);
                    userQuestionQuery(dailyQuestion);
                }
            }
        });
    }

    public void userQuestionQuery(Question question) {
        ParseQuery<UserQuestion> userQuestionQuery = new ParseQuery<UserQuestion>(UserQuestion.class);
        userQuestionQuery.whereEqualTo("user", user);
        userQuestionQuery.whereEqualTo("question", question);
        userQuestionQuery.findInBackground(new FindCallback<UserQuestion>() {
            @Override
            public void done(List<UserQuestion> objects, ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                } else if (objects.isEmpty()) {
                    insertNestedQuestionFragment();
                } else {
                    insertNestedSubmittedFragment();
                }
            }
        });
    }

    public void insertNestedQuestionFragment() {
        Fragment childFragment = new ChildQuestionFragment();
        ((ChildQuestionFragment) childFragment).setDailyQuestion(dailyQuestion);
        FragmentTransaction transaction = childFragmentManager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.child_fragment_container, childFragment).commitAllowingStateLoss();
    }

    public void insertNestedSubmittedFragment() {
        Fragment childFragment = new ChildSubmittedFragment();
        FragmentTransaction transaction = childFragmentManager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.child_fragment_container, childFragment).commitAllowingStateLoss();
    }

}
