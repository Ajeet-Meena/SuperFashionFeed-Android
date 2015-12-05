package com.ajeet_meena.super_app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ajeet_meena.super_app.Fragments.IntroSlide;
import com.ajeet_meena.super_app.R;
import com.github.paolorotolo.appintro.AppIntro;


public class AppIntroActivity extends AppIntro {

    @Override
    public void init(Bundle savedInstanceState) {
        addSlide(IntroSlide.newInstance(R.layout.intro_slide1));
        addSlide(IntroSlide.newInstance(R.layout.intro_slide2));
        addSlide(IntroSlide.newInstance(R.layout.intro_slide3));
    }


    @Override
    public void onSkipPressed() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void onNextPressed() {

    }

    @Override
    public void onDonePressed() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void onSlideChanged() {

    }

    public void getStarted(View v) {

    }
}
