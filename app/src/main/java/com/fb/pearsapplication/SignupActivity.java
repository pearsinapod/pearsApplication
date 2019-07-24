package com.fb.pearsapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.ArrayList;

public class SignupActivity extends AppCompatActivity {

    public EditText etName;
    public EditText etEmail;
    public EditText etPassword;
    public EditText etAge;
    public Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSignup = findViewById(R.id.btnSignup);
        etAge = findViewById(R.id.etAge);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String age = etAge.getText().toString();
                createUser(name, email, password, age);
            }
        });
    }

    public void createUser(String name, String email, String password, String age) {
        final ParseUser user = new ParseUser();
        final String age2 = age;
        user.setUsername(name);
        user.setEmail(email);
        user.setPassword(password);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_LONG).show();
                    addAge(user, age2);
                    instantiateGroups(user);
                    Intent mainIntent = new Intent(SignupActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void instantiateGroups(ParseUser user) {
        user.put("groups", new ArrayList<>());
        user.put("pearRequests", new ArrayList<>());
        user.saveInBackground();
    }

    public void addAge(ParseUser user, String age) {
        user.put("age", Integer.valueOf(age));
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("XYZ", "age success!");
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
