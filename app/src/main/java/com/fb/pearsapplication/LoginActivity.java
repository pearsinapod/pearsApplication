package com.fb.pearsapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.fb.pearsapplication.models.Group;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
//import com.parse.ParseFacebookUtils;

//import com.parse.ParseFacebookUtils;
import com.parse.ParseFacebookUtils;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class LoginActivity extends AppCompatActivity {

    private Button FBloginButton;
    private String firstName, lastName, email, birthday, gender, profilePicture;
    private String userId;
    private String TAG = "LoginActivity";

    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FacebookSdk.sdkInitialize(this);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        // fb-specific button
        FBloginButton = findViewById(R.id.login);

        persistenceCheck();

        btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String email = etEmail.getText().toString();
                    String password = etPassword.getText().toString();
                    login(email, password);
                }
            });

        final List<String> permissions = Arrays.asList("public_profile", "email");
        FBloginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MyApp", "clicked!");
                ParseFacebookUtils.logInWithReadPermissionsInBackground(LoginActivity.this, permissions, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException err) {
                        Log.d("MyApp", "inside done!");
                        if (user == null) {
                            Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                        } else if (user.isNew()) {
                            SignupActivity.instantiateGroups(user);
                            getFBInfo();
                        } else {
                            Log.d("MyApp", "User logged in through Facebook!");
                            getFBInfo();
                        }
                    }
                });
            }
        });
    }

    private void persistenceCheck() {
        if (ParseUser.getCurrentUser() != null) {
            Intent homeIntent = new Intent(this, MainActivity.class);
            startActivity(homeIntent);
            finish();
        }

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if (isLoggedIn) {
            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    public void getFBInfo() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("XYZ", "inside onCompleted");
                        ParseUser user = ParseUser.getCurrentUser();
                        try {
                            userId = object.getString("id");
                            profilePicture = "https://graph.facebook.com/" + userId + "/picture?width=500&height=500";
                            user.put("profilePicString", profilePicture);
                            if (object.has("first_name")) {
                                firstName = object.getString("first_name");
                                user.setUsername(firstName);
                            }
                            if (object.has("email")) {
                                email = object.getString("email");
                                user.setEmail(email);
                            }
                            if (object.has("birthday")) {
                                birthday = object.getString("birthday");
                            }
                            user.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    Log.d("MyApp", "New user created successfully!");
                                }
                            });
                            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(mainIntent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, first_name, last_name, email, birthday, gender");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void login(String email, String password) {
        ParseUser.logInInBackground(email, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    // used to populate the database
    public void populateUserDatabase(ArrayList<String> userNames) {
        Random randomInt = new Random();
        for (String userName : userNames) {
            ParseUser newUser = new ParseUser();
            newUser.setUsername(userName);
            newUser.setEmail(userName.toLowerCase() + "@test.com");
            newUser.setPassword("1234");
            newUser.put("groups", new ArrayList<Group>());
            newUser.put("age", randomInt.nextInt(60));
            newUser.put("description", "Hi! My name is " + userName + "!");

            newUser.signUpInBackground(new SignUpCallback() {
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

    // used to populate the database
    public void populateGroupDatabase(ArrayList<String> groupNames) {
        boolean priv = true;
        for (String h : groupNames) {
            Group newGroup = new Group();
            newGroup.setGroupName(h);
            newGroup.setPrivateStatus(priv);
            newGroup.setDescription("This group is about " + h);
            newGroup.setUsers(new ArrayList());
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


    // used to populate the database
    public ArrayList<String> fileToStringArray(String filename) {
        ArrayList<String> list = new ArrayList<String>();
        AssetManager assetManager = getResources().getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
        String item;
        try {
            while ((item = bf.readLine()) != null) {
                list.add(item);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

}
