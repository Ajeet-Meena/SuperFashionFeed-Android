package com.ajeet_meena.super_app.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.ajeet_meena.super_app.R;
import com.facebook.FacebookSdk;
import com.facebook.Profile;

public class Splash extends AppCompatActivity {

    TextView textViewCompany;
    TextView textViewTagLine;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initViews();
        setWindow();
        initAnimation();
        facebookLoginCheck();
    }

    private void facebookLoginCheck() {
        new LoginCheckAsync().execute();
    }

    private void initAnimation() {
        logo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.infinite_anim));
    }

    private void setWindow() {
        if( android.os.Build.VERSION.SDK_INT >= 21 ) {
            Window window = getWindow();
            window.setNavigationBarColor(Color.argb(136, 0, 0, 0));
        }

    }

    private void initAnim() {
        textViewCompany.setVisibility(View.VISIBLE);
        textViewTagLine.setVisibility(View.VISIBLE);
        textViewCompany.startAnimation(AnimationUtils.loadAnimation(this, R.anim.abc_fade_in));
        textViewTagLine.startAnimation(AnimationUtils.loadAnimation(this, R.anim.abc_slide_in_bottom));

    }

    private void initViews() {
        textViewCompany = (TextView) findViewById(R.id.company_name);
        textViewTagLine = (TextView) findViewById(R.id.tag_line);
        logo = (ImageView) findViewById(R.id.logo);

    }

    class LoginCheckAsync extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {

            if( Profile.getCurrentProfile().getCurrentProfile() != null )
                return true;
            else
                return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if( result ) {
                startActivity(new Intent(Splash.this, SuperFeedActivity.class));
            } else {
                startActivity(new Intent(Splash.this, LoginActivity.class));
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

    }
}
