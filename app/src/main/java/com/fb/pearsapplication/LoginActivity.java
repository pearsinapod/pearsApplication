package com.fb.pearsapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.Arrays;

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



        user = ParseUser.getCurrentUser();
        if (user != null) {
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        } else {
            setContentView(R.layout.activity_login);
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

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//        super.onActivityResult(requestCode, resultCode, data);
//    }


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
