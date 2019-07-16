package com.fb.pearsapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;
    final FragmentManager fragmentManager = getSupportFragmentManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        setUpBottomNavigationView();



    }

    public void setUpBottomNavigationView(){
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.groupFragment:
                        Log.d("groupFragment", "clicked set up");
                        // TODO fragment = new groupFragment;
                        break;
                    case R.id.profileFragment:
                        Log.d("profileFragment", "clicked set up");
                        //TODO fragment = new profileFragment;
                        break;
                    case R.id.exploreFragment:
                        Log.d("exploreFragment", "clicked set up");
                        //TODO fragment = new exploreFragment;
                        break;
                }
                //TODO fragmentManager.beginTransaction().replace(R.id.flContainter, fragment).commit();
                return true;
            }
        });
    }


}



