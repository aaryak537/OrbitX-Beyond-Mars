package com.example.beyondmars;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LaunchActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView txtPercent;
    private ImageView imgRocket;
    private int progress = 0;
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        hideSystemUI();

        progressBar = findViewById(R.id.loadingBar);
        txtPercent = findViewById(R.id.txtPercent);
        imgRocket = findViewById(R.id.imgRocket);

        startRocketAnimation();
        startLoading();
    }
    private void startRocketAnimation() {

        ObjectAnimator rocketAnimation = ObjectAnimator.ofFloat(
                imgRocket,
                "translationY",
                0f,
                -20f,
                0f
        );
        rocketAnimation.setDuration(1000);

        rocketAnimation.setRepeatCount(ValueAnimator.INFINITE);

        rocketAnimation.setInterpolator(new LinearInterpolator());
        rocketAnimation.start();
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
                    startActivity(new Intent(
                            LaunchActivity.this,
                            MainActivity.class
                    ));
                    finish();
                }
            }
        }, 20);
    }
    private void hideSystemUI() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().setDecorFitsSystemWindows(false);

            WindowInsetsController controller =
                    getWindow().getInsetsController();

            if (controller != null) {
                controller.hide(
                        WindowInsets.Type.statusBars()
                                | WindowInsets.Type.navigationBars());

                controller.setSystemBarsBehavior(
                        WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
        }
    }
}