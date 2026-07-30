package com.example.beyondmars;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class GameActivity extends AppCompatActivity {

    private ImageView astronaut,marsSurface;
    private TextView tvCoins,tvEnergy,tvLevel;
   // private TextView tvWorkers,tvIncome;
    private ProgressBar energyBar;
  //  private ImageButton btnMission,btnStats;
    private ImageButton btnShop,btnBuild,btnRocket,btnSettings;
    private long coins = 0;

    private int level = 1,workers = 0,energy = 10,tapReward = 5,autoIncome = 0;

    private final int maxEnergy = 100;

    private final Handler handler = new Handler(Looper.getMainLooper());

    private boolean running = true;

    private static final int FRAME_DELAY = 16;
    private final Handler incomeHandler = new Handler(Looper.getMainLooper());

    private final Handler energyHandler = new Handler(Looper.getMainLooper());

    //====================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        astronaut = findViewById(R.id.imgAstronaut);

        marsSurface = findViewById(R.id.imgBackground);

        tvCoins = findViewById(R.id.txtCoins);

        tvEnergy = findViewById(R.id.txtEnergy);

        tvLevel = findViewById(R.id.txtLevel);

       // tvWorkers = findViewById(R.id.txtWorkers);

      //  tvIncome = findViewById(R.id.txtIncome);

       // energyBar = findViewById(R.id.energyBar);

        btnShop = findViewById(R.id.btnShop);

        btnBuild = findViewById(R.id.btnBuild);

       // btnMission = findViewById(R.id.btnMission);

        btnRocket = findViewById(R.id.btnRocket);

     //   btnStats = findViewById(R.id.btnStats);
        btnSettings=findViewById(R.id.btnSettings);
        loadGame();

        updateHUD();

        setupListeners();

        startGameLoop();

        startAutoIncome();

        startEnergyRegen();
    }

    private void setupListeners() {

        marsSurface.setOnClickListener(v -> tapMars());

        astronaut.setOnClickListener(v -> tapMars());

        btnShop.setOnClickListener(v -> {
            Intent intent = new Intent(GameActivity.this, ShopActivity.class);
            startActivity(intent);
        });

        btnBuild.setOnClickListener(v -> {
            Intent intent = new Intent(GameActivity.this, BuildActivity.class);
            startActivity(intent);
        });

       // btnMission.setOnClickListener(v -> {//TODO});

        btnRocket.setOnClickListener(v -> {
            Intent intent = new Intent(GameActivity.this, PlanetsActivity.class);
            startActivity(intent);
        });

       // btnStats.setOnClickListener(v -> {//TODO});
        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(GameActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
    }

    //====================================================

    private void tapMars() {

        if (energy <= 0)
            return;

        energy--;

        coins += tapReward;

        animateTap();

        showFloatingReward();

        updateHUD();

    }

    //====================================================

    private void animateTap() {

        ObjectAnimator animator =
                ObjectAnimator.ofFloat(marsSurface,
                        "scaleX", 1f,
                        0.95f, 1f);
        animator.setDuration(120);
        animator.start();
    }

    //====================================================
    private void showFloatingReward() {

        TextView reward = new TextView(this);
        reward.setText("+1");
        reward.setTextSize(22);
        reward.setTextColor(getColor(R.color.coinYellow));
        reward.setTypeface(null, Typeface.BOLD);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );

        params.gravity = Gravity.CENTER;

        FrameLayout root = findViewById(android.R.id.content);
        root.addView(reward, params);

        reward.animate()
                .translationY(-250f)
                .alpha(0f)
                .setDuration(1000)
                .withEndAction(() -> root.removeView(reward))
                .start();
    }

    //====================================================

    private void updateHUD() {

        tvCoins.setText(String.format(Locale.getDefault(),
                "%,d", coins));

        tvEnergy.setText(energy + "/" + maxEnergy);

        tvLevel.setText("Lv " + level);

      //  tvWorkers.setText(String.valueOf(workers));

       // tvIncome.setText(autoIncome + "/sec");

        energyBar.setMax(maxEnergy);

        energyBar.setProgress(energy);
    }

    //====================================================

    private void startGameLoop() {

        handler.post(new Runnable() {

            @Override
            public void run() {

                if (!running)
                    return;
                update();
                handler.postDelayed(this, FRAME_DELAY);
            }
        });
    }

    //====================================================

    private void update() {

        //Future

        //Day/Night

        //Animations

        //Particles

        //Mission updates

    }

    //====================================================

    private void startAutoIncome() {

        incomeHandler.postDelayed(new Runnable() {

            @Override
            public void run() {

                if (!running)
                    return;

                coins += autoIncome;

                updateHUD();

                incomeHandler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    //====================================================

    private void startEnergyRegen() {

        energyHandler.postDelayed(new Runnable() {

            @Override
            public void run() {

                if (!running)
                    return;

                if (energy < maxEnergy)
                    energy++;

                updateHUD();

                energyHandler.postDelayed(this, 500);
            }
        }, 500);
    }

    //====================================================

    private void loadGame() {

        //Later from SaveManager

    }

    private void saveGame() {

        //Later SharedPreferences

    }

    //====================================================

    @Override
    protected void onPause() {
        super.onPause();
        running = false;
        saveGame();
    }
    @Override
    protected void onResume() {
        super.onResume();

        running = true;

        startGameLoop();

        startAutoIncome();

        startEnergyRegen();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        running = false;

        handler.removeCallbacksAndMessages(null);

        incomeHandler.removeCallbacksAndMessages(null);

        energyHandler.removeCallbacksAndMessages(null);
    }
}