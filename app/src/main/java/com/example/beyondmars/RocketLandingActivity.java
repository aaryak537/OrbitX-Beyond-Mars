package com.example.beyondmars;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class RocketLandingActivity extends AppCompatActivity {

    ImageView rocket,astronaut,dust;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rocket_landing);

        rocket = findViewById(R.id.rocket);
        astronaut = findViewById(R.id.astronaut);
        dust = findViewById(R.id.dust);

        rocket.post(this::startCutscene);
    }

    private void startCutscene() {

        // STEP 1
        // Rocket falls

        ObjectAnimator rocketDown =
                ObjectAnimator.ofFloat(rocket,
                        "translationY", -900f, 350f);

        rocketDown.setDuration(3000);
        rocketDown.setInterpolator(new DecelerateInterpolator());
        rocketDown.start();

        // STEP 2
        // Dust

        new Handler().postDelayed(() -> {

            dust.setVisibility(View.VISIBLE);

            ObjectAnimator fade =
                    ObjectAnimator.ofFloat(dust,"alpha",0f,1f);
            fade.setDuration(800);

            ObjectAnimator scaleX =
                    ObjectAnimator.ofFloat(dust,"scaleX",0.5f,1.6f);

            ObjectAnimator scaleY =
                    ObjectAnimator.ofFloat(dust,"scaleY",0.5f,1.6f);

            AnimatorSet set = new AnimatorSet();
            set.playTogether(fade,scaleX,scaleY);
            set.start();
        },3000);

        // STEP 3
        // Door opens (fake)

        new Handler().postDelayed(() -> {
            rocket.setRotation(3);
        },3800);

        // STEP 4
        // Astronaut

        new Handler().postDelayed(() -> {

            astronaut.setAlpha(1f);
            ObjectAnimator walk =
                    ObjectAnimator.ofFloat(
                            astronaut,
                            "translationX",
                            0f,
                            180f);
            walk.setDuration(2200);
            walk.start();
        },4600);

        // STEP 5
        // Camera Zoom

        new Handler().postDelayed(() -> {

            View root = findViewById(R.id.root);

            ObjectAnimator zoomX =
                    ObjectAnimator.ofFloat(root,
                            "scaleX",
                            1f,
                            2.2f);

            ObjectAnimator zoomY =
                    ObjectAnimator.ofFloat(root,
                            "scaleY",
                            1f,
                            2.2f);

            AnimatorSet zoom = new AnimatorSet();
            zoom.playTogether(zoomX,zoomY);
            zoom.setDuration(1700);
            zoom.start();

        },6500);

        // STEP 6
        // Open Game

        new Handler().postDelayed(() -> {

            startActivity(new Intent(
                    RocketLandingActivity.this,
                    GameActivity.class));

            overridePendingTransition(android.R.anim.fade_in,
                    android.R.anim.fade_out);

            finish();
        },8000);
    }
}