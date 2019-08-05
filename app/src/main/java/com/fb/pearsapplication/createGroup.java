package com.fb.pearsapplication;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.fb.pearsapplication.models.Group;

public class createGroup extends AppCompatActivity {

    EditText createGroupName;
    EditText createGroupDescription;
    Switch createGroupPrivate;
    Button submitGroup;
    androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        createGroupName = findViewById(R.id.createGroupName);
        createGroupDescription = findViewById(R.id.createGroupDescription);
        createGroupPrivate = findViewById(R.id.createGroupPrivate);
        submitGroup = findViewById(R.id.createGroupSubmit);

        onClickSubmitGroup();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("p e a r s");
    }

    public void onClickSubmitGroup(){
        submitGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Group group = new Group();
                group.setDescription(createGroupDescription.getText().toString());
                group.setGroupName(createGroupName.getText().toString());
                group.setPrivateStatus(createGroupPrivate.isChecked());
                group.saveInBackground();
                finish();
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_group, menu);
        return true;
    }

    public void onClickBack(MenuItem menu){
        finish();
    }
    // set up editor listener to hide keyboard

}

