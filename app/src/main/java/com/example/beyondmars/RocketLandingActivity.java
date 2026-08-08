package com.example.beyondmars;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RocketLandingActivity extends AppCompatActivity {

    private ImageView imgRocket;
    private ImageView imgAstronaut;
    private ImageView imgDust;

    private View engineGlow;
    private View titleContainer;

    private TextView txtStatus;
    private TextView btnSkip;

    private Handler handler;

    private boolean landingFinished = false;
    private boolean gameOpened = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rocket_landing);

        handler = new Handler(Looper.getMainLooper());

        imgRocket = findViewById(R.id.imgRocket);
        imgAstronaut = findViewById(R.id.imgAstronaut);
        imgDust = findViewById(R.id.imgDust);

        engineGlow = findViewById(R.id.engineGlow);

        titleContainer = findViewById(R.id.titleContainer);

        txtStatus = findViewById(R.id.txtStatus);

        btnSkip = findViewById(R.id.btnSkip);
        btnSkip.setOnClickListener(v -> openGame());
        hideSystemUI();
        startLandingSequence();
    }

    private void startLandingSequence() {

        imgRocket.post(() -> {

            float screenHeight =
                    getResources()
                            .getDisplayMetrics()
                            .heightPixels;

            // Rocket starts above the screen
            imgRocket.setTranslationY(
                    -screenHeight * 0.65f
            );

            imgRocket.setRotation(0f);

            // Engine initially hidden
            engineGlow.setAlpha(0f);

            // Astronaut hidden
            imgAstronaut.setAlpha(0f);

            // Dust hidden
            imgDust.setAlpha(0f);

            // Title hidden
            titleContainer.setAlpha(0f);

            txtStatus.setText(
                    "ENTERING MARTIAN ATMOSPHERE..."
            );

            // Show mission title
            titleContainer.animate()
                    .alpha(1f)
                    .setDuration(700)
                    .start();

            // Begin rocket landing
            startRocketDescent();
        });
    }


    // =========================================================
    // ROCKET DESCENT
    // =========================================================

    private void startRocketDescent() {

        handler.postDelayed(() -> {

            txtStatus.setText("DESCENDING...");

            // Engine glow
            engineGlow.animate()
                    .alpha(1f)
                    .setDuration(400)
                    .start();

            float startPosition =
                    -getResources()
                            .getDisplayMetrics()
                            .heightPixels * 0.65f;

            // Rocket moves down
            ObjectAnimator rocketMove =
                    ObjectAnimator.ofFloat(
                            imgRocket,
                            View.TRANSLATION_Y,
                            startPosition,
                            250f
                    );

            rocketMove.setDuration(3500);

            rocketMove.setInterpolator(
                    new AccelerateDecelerateInterpolator()
            );


            // Small rocket movement
            ObjectAnimator rocketSway =
                    ObjectAnimator.ofFloat(
                            imgRocket,
                            View.ROTATION,
                            -1.5f,
                            1.5f,
                            -1f,
                            0f
                    );

            rocketSway.setDuration(3500);


            AnimatorSet rocketAnimation =
                    new AnimatorSet();

            rocketAnimation.playTogether(
                    rocketMove,
                    rocketSway
            );

            rocketAnimation.start();

            // Engine animation
            startEnginePulse();


            rocketAnimation.addListener(
                    new android.animation.AnimatorListenerAdapter() {

                        @Override
                        public void onAnimationEnd(
                                Animator animation) {

                            landingImpact();
                        }
                    }
            );

        }, 1000);
    }


    // =========================================================
    // ENGINE PULSE
    // =========================================================

    private void startEnginePulse() {

        ValueAnimator pulse =
                ValueAnimator.ofFloat(
                        0.65f,
                        1.2f,
                        0.7f
                );

        pulse.setDuration(600);

        pulse.setRepeatCount(5);

        pulse.addUpdateListener(animation -> {

            float value =
                    (float) animation.getAnimatedValue();

            engineGlow.setScaleX(value);
            engineGlow.setScaleY(value);
        });

        pulse.start();
    }


    // =========================================================
    // LANDING IMPACT
    // =========================================================

    private void landingImpact() {

        if (landingFinished) {
            return;
        }

        landingFinished = true;

        txtStatus.setText(
                "LANDING SUCCESSFUL"
        );


        // -----------------------------------------------------
        // ROCKET BOUNCE
        // -----------------------------------------------------

        ObjectAnimator bounce =
                ObjectAnimator.ofFloat(
                        imgRocket,
                        View.TRANSLATION_Y,
                        imgRocket.getTranslationY(),
                        imgRocket.getTranslationY() - 15f,
                        imgRocket.getTranslationY()
                );

        bounce.setDuration(500);

        bounce.start();


        // -----------------------------------------------------
        // ENGINE OFF
        // -----------------------------------------------------

        engineGlow.animate()
                .alpha(0f)
                .setDuration(600)
                .start();


        // -----------------------------------------------------
        // DUST EFFECT
        // -----------------------------------------------------

        imgDust.setAlpha(0f);

        imgDust.setScaleX(0.5f);
        imgDust.setScaleY(0.5f);


        AnimatorSet dustAnimation =
                new AnimatorSet();


        ObjectAnimator dustAlpha =
                ObjectAnimator.ofFloat(
                        imgDust,
                        View.ALPHA,
                        0f,
                        1f,
                        0.7f,
                        0f
                );


        ObjectAnimator dustScaleX =
                ObjectAnimator.ofFloat(
                        imgDust,
                        View.SCALE_X,
                        0.5f,
                        1.2f
                );


        ObjectAnimator dustScaleY =
                ObjectAnimator.ofFloat(
                        imgDust,
                        View.SCALE_Y,
                        0.5f,
                        1.0f
                );


        dustAnimation.playTogether(
                dustAlpha,
                dustScaleX,
                dustScaleY
        );

        dustAnimation.setDuration(1200);

        dustAnimation.start();


        // -----------------------------------------------------
        // ASTRONAUT APPEARS
        // -----------------------------------------------------

        handler.postDelayed(() -> {

            imgAstronaut.setTranslationY(30f);

            imgAstronaut.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(1000)
                    .setInterpolator(
                            new DecelerateInterpolator()
                    )
                    .start();

        }, 700);


        // -----------------------------------------------------
        // FINAL MESSAGE
        // -----------------------------------------------------

        handler.postDelayed(() -> {

            txtStatus.setText(
                    "HUMANITY HAS ARRIVED ON MARS"
            );

        }, 1800);


        // -----------------------------------------------------
        // AUTOMATICALLY OPEN GAME
        // -----------------------------------------------------

        handler.postDelayed(() -> {

            openGame();

        }, 3500);
    }


    // =========================================================
    // OPEN GAME ACTIVITY
    // =========================================================

    private void openGame() {

        if (gameOpened) {
            return;
        }

        gameOpened = true;

        Intent intent =
                new Intent(
                        RocketLandingActivity.this,
                        GameActivity.class
                );

        startActivity(intent);

        // Remove landing screen from back stack
        finish();

        // Nice transition
        overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
        );
    }


    // =========================================================
    // FULLSCREEN IMMERSIVE MODE
    // =========================================================

    private void hideSystemUI() {

        Window window = getWindow();

        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.R) {

            WindowInsetsController controller =
                    window.getInsetsController();

            if (controller != null) {

                controller.hide(
                        WindowInsets.Type.statusBars()
                                | WindowInsets.Type.navigationBars()
                                | WindowInsets.Type.systemBars()
                );

                controller.setSystemBarsBehavior(
                        WindowInsetsController
                                .BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                );
            }

        } else {

            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );
        }
    }


    // =========================================================
    // KEEP FULLSCREEN WHEN USER RETURNS TO ACTIVITY
    // =========================================================

    @Override
    public void onWindowFocusChanged(
            boolean hasFocus) {

        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            hideSystemUI();
        }
    }


    @Override
    protected void onResume() {

        super.onResume();

        hideSystemUI();
    }


    // =========================================================
    // CLEAN HANDLER
    // =========================================================

    @Override
    protected void onDestroy() {

        super.onDestroy();

        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}