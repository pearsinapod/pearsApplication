package com.fb.pearsapplication;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.fb.pearsapplication.models.Group;
import com.fb.pearsapplication.models.Hobby;
import com.fb.pearsapplication.models.Question;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

//import com.parse.ParseFacebookUtils;
//import com.parse.ParseFacebookUtils;


public class LoginActivity extends AppCompatActivity {

    private Button FBloginButton;
    private String firstName, lastName, email, birthday, gender, profilePicture;
    private String userId;
    private String TAG = "LoginActivity";

    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FacebookSdk.sdkInitialize(this);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        FBloginButton = findViewById(R.id.login);
        btnSignUp = findViewById(R.id.btnSignup);
        persistenceCheck();
        btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String email = etEmail.getText().toString();
                    String password = etPassword.getText().toString();
                    login(email, password);
                }
            });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupIntent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(signupIntent);
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

        //TO ADD THE HOBBIES
       //aprioriToParse(fileToJSONObj("AprioriResultsTesting.json"));
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

    private void installationUpdate() {
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("device_id", ParseUser.getCurrentUser().getObjectId());
        installation.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                } else {
                    Log.d("XYZ", "successfully updated installation id");
                }
            }
        });
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

    public void populateQuestionDatabase(ArrayList<String> questions) {
        Calendar cal = Calendar.getInstance();
        Date targetDay = cal.getTime();
        for (String q: questions) {
            Question newQuestion = new Question();
            newQuestion.setQuestion(q);
            newQuestion.setTargetDate(targetDay);

            newQuestion.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        e.printStackTrace();
                    } else {
                        Log.d("XYZ", "question save");
                    }
                }
            });
            cal.add(Calendar.DATE, 1);
            targetDay = cal.getTime();
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

    // makes file into JSON Object
    public org.json.simple.JSONObject fileToJSONObj(String filename) {
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
                JSONParser parser = new JSONParser();
                org.json.simple.JSONObject json = null;
                try {
                    json = (org.json.simple.JSONObject) parser.parse(item);
                } catch (org.json.simple.parser.ParseException e) {
                    e.printStackTrace();
                }
                return json;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Connects apriori algorithm to parse

    public void aprioriToParse( org.json.simple.JSONObject jsonObject){
        aprioriAlgorithm apriori = new aprioriAlgorithm();
        JSONArray arr = apriori.convertJSONObjToArr( jsonObject);
        // Log.d("ARRRR", arr.toJSONString()); check size to test once u clear again
        int arrSize = arr.size();
        for (int object = 0; object < arrSize; object++) {
            org.json.simple.JSONObject object_info = apriori.convertObjtoJSONObj(arr.get(object));
            Hobby h = new Hobby();
            ArrayList subset = (ArrayList) object_info.get("subset");
            int occurrences =((Long) object_info.get("occurrences")).intValue();
            double threshold = (double)object_info.get("threshold");
            h.newSubset(subset);
            h.setOccurences(occurrences);
            h.setThreshold(threshold);
            h.saveInBackground();
        }

    }

}
