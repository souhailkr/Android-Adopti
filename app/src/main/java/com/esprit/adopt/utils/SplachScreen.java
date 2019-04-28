package com.esprit.souhaikr.adopt.utils;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.esprit.souhaikr.adopt.R;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;

public class SplachScreen extends AppCompatActivity {
    ProgressBar progressBar ;
    private final int SPLASH_DISPLAY_LENGTH = 2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splach_screen);
        progressBar = findViewById(R.id.spin_kit);

        Sprite doubleBounce = new ThreeBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplachScreen.this,MainActivity.class);
                SplachScreen.this.startActivity(mainIntent);
                SplachScreen.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    }

