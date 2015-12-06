package com.ajeet_meena.super_app.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.ajeet_meena.super_app.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends Activity {

    CallbackManager mCallbackManager;
    AccessTokenTracker tracker;
    ProfileTracker profileTracker;

    String first_name;
    String last_name;
    String user_name;
    String user_id;
    String profile_pic_url;
    String email;
    LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_login);
        mCallbackManager = CallbackManager.Factory.create();
        tracker = intializeTokenTracker();
        profileTracker = intializeProfileTracker();
        tracker.startTracking();
        profileTracker.startTracking();

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        loginButton.registerCallback(mCallbackManager, mCallback);

    }

    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>()
    {
        @Override
        public void onSuccess(LoginResult loginResult)
        {
            Profile profile = Profile.getCurrentProfile();
            getProfileInfo(profile);
        }


        @Override
        public void onCancel()
        {

        }

        @Override
        public void onError(FacebookException e)
        {

        }
    };

    public void getProfileInfo(Profile profile)
    {
        if (profile != null)
        {
            first_name = profile.getFirstName();
            last_name = profile.getLastName();
            user_name = profile.getName();
            user_id = profile.getId();
            profile_pic_url = profile.getProfilePictureUri(128, 128).toString();
        }
    }

    private ProfileTracker intializeProfileTracker()
    {
        return new ProfileTracker()
        {
            @Override
            protected void onCurrentProfileChanged(Profile old, Profile newProfile)
            {

                getProfileInfo(newProfile);

            }
        };
    }

    private AccessTokenTracker intializeTokenTracker()
    {
        return new AccessTokenTracker()
        {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken old, AccessToken newToken)
            {

                final ProgressDialog pDialog = new ProgressDialog(LoginActivity.this);
                GraphRequest request = GraphRequest.newMeRequest(
                        newToken,
                        new GraphRequest.GraphJSONObjectCallback()
                        {

                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response)
                            {
                                pDialog.dismiss();
                                startActivity(new Intent(getApplicationContext(),SuperActivity.class));




                                try
                                {
                                    if(object!=null)
                                        email = object.getString("email");

                                } catch (JSONException e)
                                {
                                    e.printStackTrace();
                                }

                                // onlineHelper = new OnlineHelper(getActivity());
                                //onlineHelper.createUser(user_id, user_name, first_name, last_name, email, profile_pic_url);
                            }
                        });


                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday,location");

                request.setParameters(parameters);

                pDialog.setMessage("Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);

                pDialog.show();
                request.executeAsync();

            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        getProfileInfo(profile);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        tracker.stopTracking();
        profileTracker.stopTracking();
    }


}
