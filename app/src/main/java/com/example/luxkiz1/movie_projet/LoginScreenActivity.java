package com.example.luxkiz1.movie_projet;


import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginScreenActivity extends ActionBarActivity {

    private CallbackManager callbackManager;
    private ArrayList<String> arr;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.vv));
            getWindow().setStatusBarColor(getResources().getColor(R.color.vv));
        }

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);


        try {
            Parse.enableLocalDatastore(this);
            Parse.initialize(getApplicationContext(), "52bGN3XEs1xxOQlO6YzD2ATFws38BNY45ifSFmeQ",
                    "h2bBNjENFG2IiVg27jgQETluga2bBZYRcIBpdeVV");
            ParseFacebookUtils.initialize(this);
        } catch (Exception e) {
        }

        callbackManager = CallbackManager.Factory.create();
        arr = new ArrayList<>();
        arr.add("public_profile");
        arr.add("email");
        arr.add("user_friends");
        arr.add("user_birthday");

    }


    public void ClickButtonFacebook(View view) {

        progressBar.setVisibility(View.VISIBLE);
        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, arr, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {

                if (parseUser == null) {   //Cancel
                    Log.d("----------------->", "cancel");

                    ParseUser.logOut();
                    //LoginManager.getInstance().logOut();
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Đăng nhập thất bại. Kiểm tra kết nối Internet", Toast.LENGTH_SHORT).show();

                } else if (parseUser.isNew()) {  // New user
                    Log.d("----------------->", "new user");

                    GraphRequest req = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject jsonObject, GraphResponse response) {

                                    try {

                                        ParseObject accountInformation = new ParseObject("AccountInformation");
                                        accountInformation.put("id", response.getJSONObject().getString("id"));
                                        accountInformation.put("name", response.getJSONObject().getString("name"));
                                        accountInformation.put("email", response.getJSONObject().getString("email"));
                                        accountInformation.saveInBackground();


                                    } catch (JSONException e1) {
                                        Log.d("----------------->", "200");
                                    }

                                }


                            });

                    Bundle bundle = new Bundle();
                    bundle.putString("fields", "id, name,email,gender,birthday,picture.height(300).width(300)");
                    req.setParameters(bundle);
                    req.executeAsync();


                    LoginSuccess();


                } else { // Old user
                    Log.d("----------------->", "old user");
                    LoginSuccess();

                }
            }
        });

    }

    private void LoginSuccess() {

        Intent i = new Intent(LoginScreenActivity.this, MainScreenActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        LoginScreenActivity.this.startActivity(i);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }
}
