package com.example.ayoub.nicper.MainActivity.Intro;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.example.ayoub.nicper.MainActivity.ActivityMap;
import com.example.ayoub.nicper.R;
import com.github.paolorotolo.appintro.AppIntro2;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MyIntro  extends AppIntro2 {

    private FirebaseAuth.AuthStateListener mAuthListener;
    // Please DO NOT override onCreate. Use init.
    @Override
    public void init(Bundle savedInstanceState) {


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            Intent intent = new Intent(MyIntro.this, ActivityMap.class);
            startActivity(intent);
        } else {
            // No user is signed in

        }

        // Add your slide's fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
        addSlide(SampleSlide.newInstance(R.layout.fragment_intro_1));
        addSlide(SampleSlide.newInstance(R.layout.fragment_intro_2));
        addSlide(SampleSlide.newInstance(R.layout.fragment_intro_3));
        addSlide(SampleSlide.newInstance(R.layout.fragment_intro_4));
        //setBarColor(Color.parseColor("#3F51B5"));
        setIndicatorColor(Color.parseColor("#015D8E"), Color.parseColor("#474747"));


    }



    @Override
    public void onDonePressed() {
        // Do something when users tap on Done button.
        Intent intent = new Intent(MyIntro.this, GoogleLogin.class);
        startActivity(intent);
    }

    @Override
    public void onSlideChanged() {

    }

    @Override
    public void onNextPressed() {
        // Do something when users tap on Next button.
    }

}


