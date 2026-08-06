package com.example.beyondmars;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.ImageView;
import android.view.View;

public class RocketLandingActivity extends AppCompatActivity {

    private ImageView rocket, dust, astronaut, glow;
    private View fadeOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rocket_landing);

        rocket = findViewById(R.id.rocket);
        dust = findViewById(R.id.dust);
        astronaut = findViewById(R.id.astronaut);
        glow = findViewById(R.id.glow);
        fadeOverlay = findViewById(R.id.fadeOverlay);

        startLandingAnimation();
    }

    private void startLandingAnimation() {

        // Fade in from black
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(
                fadeOverlay,
                "alpha",
                1f,
                0f
        );
        fadeIn.setDuration(1200);
        fadeIn.start();

        // Rocket landing
        ObjectAnimator rocketLanding = ObjectAnimator.ofFloat(
                rocket,
                "translationY",
                -900f,
                700f
        );
        rocketLanding.setDuration(3500);
        rocketLanding.setInterpolator(new BounceInterpolator());

        rocketLanding.start();

        rocketLanding.addListener(new android.animation.AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(android.animation.Animator animation) {

                showLandingEffects();

            }
        });

    }

    private void showLandingEffects() {

        // Glow
        ObjectAnimator glowFade = ObjectAnimator.ofFloat(
                glow,
                "alpha",
                0f,
                1f
        );
        glowFade.setDuration(500);

        // Dust
        dust.setScaleX(0.5f);
        dust.setScaleY(0.5f);

        ObjectAnimator dustAlpha =
                ObjectAnimator.ofFloat(dust, "alpha", 0f, 1f);

        ObjectAnimator dustScaleX =
                ObjectAnimator.ofFloat(dust, "scaleX", 0.5f, 1.3f);

        ObjectAnimator dustScaleY =
                ObjectAnimator.ofFloat(dust, "scaleY", 0.5f, 1.3f);

        AnimatorSet dustSet = new AnimatorSet();
        dustSet.playTogether(dustAlpha, dustScaleX, dustScaleY);
        dustSet.setDuration(800);

        glowFade.start();
        dustSet.start();

        shakeRocket();
    }

    private void shakeRocket() {

        ObjectAnimator shake = ObjectAnimator.ofFloat(
                rocket,
                "translationX",
                0f,
                -12f,
                12f,
                -8f,
                8f,
                0f
        );

        shake.setDuration(700);
        shake.start();

        shake.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {

                astronautExit();

            }
        });

    }

    private void astronautExit() {

        astronaut.setAlpha(0f);

        ObjectAnimator fadeAstronaut =
                ObjectAnimator.ofFloat(astronaut,
                        "alpha",
                        0f,
                        1f);

        ObjectAnimator walk =
                ObjectAnimator.ofFloat(astronaut,
                        "translationX",
                        0f,
                        120f);

        fadeAstronaut.setDuration(700);
        walk.setDuration(1800);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(fadeAstronaut, walk);
        set.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(
                        RocketLandingActivity.this,
                        GameActivity.class));

                finish();

            }
        }, 2500);

    }

}