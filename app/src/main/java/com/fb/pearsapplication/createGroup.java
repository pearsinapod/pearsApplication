package com.fb.pearsapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.fb.pearsapplication.models.Group;

public class createGroup extends AppCompatActivity {

    EditText createGroupName;
    EditText createGroupDescription;
    Switch createGroupPrivate;
    Button submitGroup;
    TextView tvDescriptionCount;
    int descriptionCount;
    TextView tvNameCount;
    int nameCount;
    de.hdodenhof.circleimageview.CircleImageView createPlaceholder;
    ImageButton createGroupAddImage;

    androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        createGroupName = findViewById(R.id.createGroupName);
        createGroupDescription = findViewById(R.id.createGroupDescription);
        createGroupPrivate = findViewById(R.id.createGroupPrivate);
        submitGroup = findViewById(R.id.createGroupSubmit);
        tvDescriptionCount = findViewById(R.id.createDescriptionCount);
        tvNameCount = findViewById(R.id.createNameCount);
        createPlaceholder= findViewById(R.id.createPlaceholder);
        createGroupAddImage = findViewById(R.id.createGroupAddImage);
        nameCount= 30;
        descriptionCount = 200;
        tvDescriptionCount.setText(String.valueOf(descriptionCount));
        tvNameCount.setText(String.valueOf(nameCount));
        textChangeDescription();
        textChangeName();
        setPlaceholder();
        //profileFragment photo= new profileFragment();
       // photo.onPickPhoto(createGroupAddImage);

        onClickSubmitGroup();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("p e a r s");
    }

    public void setPlaceholder(){
        Glide.with(this).load(R.drawable.group_search_placeholder).into(createPlaceholder);
    }

    public boolean  possibleToasts(){
        if (descriptionCount==200 && nameCount==30){
            Toast.makeText(createGroup.this, "You must have a name and description", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (descriptionCount == 200){
            Toast.makeText(createGroup.this, "You must have a description", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (nameCount == 30){
            Toast.makeText(createGroup.this, "You must have a name", Toast.LENGTH_SHORT).show();
            return true;
        }
        String[] arrDescription = tvDescriptionCount.getText().toString().split("\\s+");
        String [] arrName = tvNameCount.getText().toString().split("\\s+");
        if (arrName.length==0 && arrDescription.length==0){
            Toast.makeText(createGroup.this, "Invalid Name and Description", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (arrDescription.length==0){
            Toast.makeText(createGroup.this, "Invalid Description", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (arrName.length==0){
            Toast.makeText(createGroup.this, "Invalid Name", Toast.LENGTH_SHORT).show();
            return true;
        }

        else if (descriptionCount<0 && nameCount<0){
            Toast.makeText(createGroup.this, "Description and Name is too long", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (descriptionCount<0){
            Toast.makeText(createGroup.this, "Description is too long", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (nameCount<0){
            Toast.makeText(createGroup.this, "Name is too long", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public void onClickSubmitGroup(){
        submitGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (!possibleToasts()){
                    Group group = new Group();
                    group.setDescription(createGroupDescription.getText().toString());
                    group.setGroupName(createGroupName.getText().toString());
                    group.setPrivateStatus(createGroupPrivate.isChecked());
                    group.saveInBackground();
                    finish();
                }
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_group, menu);
        return true;
    }

    public void textViewParam (TextView view, int count){
        view.setText(String.valueOf(count));
    }

    public void onClickBack(MenuItem menu){
        finish();
    }
    // TODO : set up editor listener to hide keyboard

    public void countColor(TextView tv , int count ){
        if (count<0){
            tv.setTextColor(Color.parseColor("#FF0000"));
        }
        else{
            tv.setTextColor(Color.parseColor("#000000"));
        }
    }

    public  void textChangeDescription(){
        createGroupDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                descriptionCount = descriptionCount+ (before-count);
                countColor(tvDescriptionCount, descriptionCount);
                textViewParam(tvDescriptionCount, descriptionCount);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    public  void textChangeName(){
        createGroupName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nameCount = nameCount+ (before-count);
                countColor(tvNameCount, nameCount);
                textViewParam(tvNameCount, nameCount);
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

}

