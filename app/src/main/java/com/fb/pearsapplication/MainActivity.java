package com.fb.pearsapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.fb.pearsapplication.fragments.exploreFragment;
import com.fb.pearsapplication.fragments.groupFragment;
import com.fb.pearsapplication.fragments.profileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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

    }

    public void setUpBottomNavigationView(){
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
                        fragment = new groupFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainter, fragment).commit();
                return true;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.groupFragment);
    }


}



