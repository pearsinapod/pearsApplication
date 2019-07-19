package com.fb.pearsapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

//import com.facebook.AccessToken;
import com.fb.pearsapplication.fragments.exploreFragment;
import com.fb.pearsapplication.fragments.groupFragment;
import com.fb.pearsapplication.fragments.profileFragment;
import com.fb.pearsapplication.models.Group;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    androidx.appcompat.widget.Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        setUpBottomNavigationView();


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("temp");

        String fbUserEmail = getIntent().getStringExtra("email");

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("email", fbUserEmail);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                    return;
                }
                if (objects.isEmpty()) {
                    Log.d("XYZ", "need to sign-up user");
                }
            }
        });
    }

    public void setUpBottomNavigationView() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = new groupFragment();
                switch (menuItem.getItemId()) {
                    case R.id.groupFragment:
                        Log.d("groupFragment", "groupFragment clicked");
                        fragment = new groupFragment();
                        break;
                    case R.id.profileFragment:
                        Log.d("profileFragment", "profileFragment clicked");
                        fragment = new profileFragment();
                        break;
                    case R.id.exploreFragment:
                        Log.d("exploreFragment", "exploreFragment clicked");
                        fragment = new exploreFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainter, fragment).commit();
                return true;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.groupFragment);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    public void onClickLogout(MenuItem item) {
        Log.d("Main Activity", "Logged out");
        ParseUser.logOut();
        Intent logoutIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(logoutIntent);
        finish();
    }

    public void onClickMessages(MenuItem item) {
        Log.d("Main Activity", "went to messages");
        Intent messageIntent = new Intent(MainActivity.this, conversationsActivity.class);
        startActivity(messageIntent);
    }

    public void populateGroupDatabase(ArrayList<String> groupNames) {
        boolean priv = true;
        for (String h : groupNames) {
            Group newGroup = new Group();
            newGroup.setGroupName(h);
            newGroup.setPrivateStatus(priv);
            newGroup.setDescription("This group is about " + h);
            newGroup.addUser(new ArrayList());
            priv = !priv;

            newGroup.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Log.d("XYZ", "success");
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}





