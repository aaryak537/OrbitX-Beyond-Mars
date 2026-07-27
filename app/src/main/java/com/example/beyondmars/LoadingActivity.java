package com.example.beyondmars;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class LoadingActivity extends AppCompatActivity {

     ProgressBar progressBar;
     TextView txtPercent;
     TextView txtTip;
     ImageView imgRocket;
     int progress = 0;
    private final String[] tips = {
            "Upgrade miners to earn faster.",
            "Tap rapidly during Boost Mode.",
            "Unlock new planets for higher income.",
            "Upgrade your rocket to explore faster.",
            "Collect daily rewards every day.",
            "Auto Miners earn coins even while idle.",
            "Complete missions for huge bonuses."
    };
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        progressBar = findViewById(R.id.loadingBar);
        txtPercent = findViewById(R.id.txtPercent);
        txtTip = findViewById(R.id.txtTip);
        imgRocket = findViewById(R.id.imgRocket);

        // Show random tip
        Random random = new Random();
        txtTip.setText(tips[random.nextInt(tips.length)]);

        // Rocket floating animation
        ObjectAnimator rocketAnimation = ObjectAnimator.ofFloat(
                imgRocket,
                "translationY", 0f, -25f, 0f
        );

        rocketAnimation.setDuration(1200);
        rocketAnimation.setRepeatCount(ValueAnimator.INFINITE);
        rocketAnimation.setInterpolator(new LinearInterpolator());
        rocketAnimation.start();

        startLoading();
    }
    private void startLoading() {

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (progress <= 100) {
                    progressBar.setProgress(progress);
                    txtPercent.setText(progress + "%");

                    progress++;

                    handler.postDelayed(this, 35);
                } else {
                    Intent intent = new Intent(
                            LoadingActivity.this,
                            MainActivity.class
                    );
                    startActivity(intent);
                    finish();
                }
            }
        }, 35);
    }
}