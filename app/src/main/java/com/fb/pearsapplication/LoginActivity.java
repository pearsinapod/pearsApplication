package com.fb.pearsapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//import com.facebook.AccessToken;
//import com.facebook.CallbackManager;
//import com.facebook.FacebookCallback;
//import com.facebook.FacebookException;
//import com.facebook.login.LoginManager;
//import com.facebook.login.LoginResult;
//import com.facebook.login.widget.LoginButton;
import com.fb.pearsapplication.models.Group;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

//import static com.facebook.appevents.UserDataStore.EMAIL;

public class LoginActivity extends AppCompatActivity {

    public EditText etEmail;
    public EditText etPassword;
    public Button btnLogin;
    public Button btnSignup;
    public ParseUser user;

//    public CallbackManager callbackManager;
//    public LoginButton btnFBLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = ParseUser.getCurrentUser();
        if (user != null) {
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        } else {
            setContentView(R.layout.activity_login);
            //populateUserDatabase(fileToStringArray("userData"));
            etEmail = findViewById(R.id.etEmail);
            etPassword = findViewById(R.id.etPassword);
            btnLogin = findViewById(R.id.btnLogin);
            btnSignup = findViewById(R.id.btnSignup);

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String email = etEmail.getText().toString();
                    String password = etPassword.getText().toString();
                    login(email, password);
                }
            });

            btnSignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent signupIntent = new Intent(LoginActivity.this, SignupActivity.class);
                    startActivity(signupIntent);
                    finish();
                }
            });
        }

    }

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

    public ArrayList<String> fileToStringArray(String filename) {
        ArrayList<String> list = new ArrayList<String>();
        AssetManager assetManager = getResources().getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open(filename);
            if (inputStream != null)
                Log.d("XYZ", "It worked!");
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
        String item;
        try {
            while ((item = bf.readLine()) != null) {
                list.add(item);
                Log.d("XYZ", "added" + item);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
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
}


//        callbackManager = CallbackManager.Factory.create();
//        setContentView(R.layout.activity_login);
//
//        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                setResult(RESULT_OK);
//                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(mainIntent);
//                finish();
//            }
//
//            @Override
//            public void onCancel() {
//                setResult(RESULT_CANCELED);
//                finish();
//            }
//
//            @Override
//            public void onError(FacebookException exception) {
//                exception.printStackTrace();
//            }
//        });
//
