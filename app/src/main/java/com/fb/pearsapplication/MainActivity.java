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

import com.facebook.login.LoginManager;
import com.fb.pearsapplication.fragments.exploreFragment;
import com.fb.pearsapplication.fragments.groupFragment;
import com.fb.pearsapplication.fragments.profileFragment;
import com.fb.pearsapplication.fragments.searchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

//import com.facebook.AccessToken;

//import com.facebook.AccessToken;


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
        getSupportActionBar().setTitle("p e a r s");


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
                        Log.d("exploreFragment", "searchFragment clicked");
                        fragment = new exploreFragment();
                        break;
                    case R.id.searchFragment:
                        Log.d("searchFragment", "searchFragment clicked");
                        fragment = new searchFragment();
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
        LoginManager.getInstance().logOut();
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

}





