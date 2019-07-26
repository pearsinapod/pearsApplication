package com.fb.pearsapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.facebook.login.LoginManager;
import com.fb.pearsapplication.fragments.exploreFragment;
import com.fb.pearsapplication.fragments.groupFragment;
import com.fb.pearsapplication.fragments.profileFragment;
import com.fb.pearsapplication.fragments.searchFragment;
import com.fb.pearsapplication.models.GroupUserRelation;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

//import com.facebook.AccessToken;

//import com.facebook.AccessToken;


public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    androidx.appcompat.widget.Toolbar toolbar;
    LocationManager locationManager;
    Location location;
    LocationListener locationListener;
    String provider;
    ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = ParseUser.getCurrentUser();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        setUpBottomNavigationView();


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("temp");
        locationFinder();
    }

    public void locationFinder() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, true);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("XYZ", "asked for permissions");
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 102);
            location = locationManager.getLastKnownLocation(provider);
        } else {
            location = locationManager.getLastKnownLocation(provider);
            setUserLocation(location);
        }
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                setUserLocation(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
            }
        };
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 0, locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 102) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setUserLocation(location);
            } else {
                Log.d("XYZ", "sad");
            }
        }
    }

    private void setUserLocation(Location location) {
        if (location != null) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();

            ParseGeoPoint geopoint = new ParseGeoPoint(lat, lng);
            user.put("location", geopoint);
            user.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        e.printStackTrace();
                    } else {
                        updateGURs();
                    }
                }
            });
        }
    }

    private void updateGURs() {
        ParseQuery<GroupUserRelation> gurQuery = new ParseQuery<GroupUserRelation>(GroupUserRelation.class);
        gurQuery.whereEqualTo("user", user);
        gurQuery.findInBackground(new FindCallback<GroupUserRelation>() {
            @Override
            public void done(List<GroupUserRelation> objects, ParseException e) {
                for (GroupUserRelation gur: objects) {
                    gur.setUserLocation(user.getParseGeoPoint("location"));
                    gur.saveInBackground();
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





