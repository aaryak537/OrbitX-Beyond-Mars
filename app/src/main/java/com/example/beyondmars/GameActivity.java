package com.example.beyondmars;

import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    LinearLayout missionPanel;
    private ImageView imgBackground,imgAstronaut;
    private ImageView imgStars;
    private FrameLayout floatingResourceContainer,particleContainer;
    private TextView txtCoins,txtMinerals,txtEnergy,txtLevel,txtWorkers,txtMission;
    private TextView txtMissionProgress,txtFloatingReward,txtFloatingXP;
    private ProgressBar progressMission;
    private ProgressBar buildProgress;
    private ImageButton btnShop,btnBuild,btnWorkers,btnRocket,btnSettings;

    private long coins = 0,minerals = 0;
    private int energy = 0,maxEnergy = 100,xp = 0,level = 1,workers = 0;

    private int hqLevel = 1,solarLevel = 0,mineLevel = 0,greenhouseLevel = 0,oxygenPlantLevel = 0;

    private static final int TAP_ENERGY_GAIN = 1,TAP_MINERAL_GAIN = 5,TAP_XP_GAIN = 1;
    private static final int ENERGY_REGEN = 1,ENERGY_REGEN_DELAY = 1000,WORKER_INTERVAL = 2000;
    private static final int AUTO_SAVE_INTERVAL = 10000;

    private int combo = 0;
    private long lastTapTime = 0;
    private int tapCount = 0;
    private int criticalTapCount = 0;
    private long coinsSpent = 0;
    private long totalEnergyProduced = 0;
    private float totalPlaySeconds = 0;
    private static final long COMBO_RESET_TIME = 1800;

    private final Random random = new Random();
    private static final int CRITICAL_CHANCE = 10,CRITICAL_MULTIPLIER = 3;
    private ArrayList<Mission> dailyMissions;
    private ArrayList<Mission> storyMissions;

    private Mission currentMission;
    private boolean running = false;
    private static final int FRAME_DELAY = 16;

    private final Handler gameHandler =
            new Handler(Looper.getMainLooper());
    private final Handler energyHandler =
            new Handler(Looper.getMainLooper());
    private final Handler workerHandler =
            new Handler(Looper.getMainLooper());
    private final Handler autoSaveHandler =
            new Handler(Looper.getMainLooper());

    private SharedPreferences preferences;

    private boolean dayMode = true,soundEnabled = true,vibrationEnabled = true,musicEnabled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        preferences = getSharedPreferences(
                "BeyondMarsSave",
                MODE_PRIVATE);

        imgBackground = findViewById(R.id.imgBackground);
        imgAstronaut = findViewById(R.id.imgAstronaut);

        imgStars = findViewById(R.id.imgStars);

        floatingResourceContainer =
                findViewById(R.id.floatingResourceContainer);
        particleContainer =
                findViewById(R.id.particleContainer);

        txtCoins = findViewById(R.id.txtCoins);
        txtMinerals = findViewById(R.id.txtMinerals);
        txtEnergy = findViewById(R.id.txtEnergy);
        txtLevel = findViewById(R.id.txtLevel);
        txtWorkers = findViewById(R.id.txtWorkers);

        txtMission = findViewById(R.id.txtMission);
        txtMissionProgress = findViewById(R.id.txtMissionProgress);
        progressMission = findViewById(R.id.progressMission);
        missionPanel=findViewById(R.id.missionPanel);
       // buildProgress = findViewById(R.id.buildProgress);

        txtFloatingReward = findViewById(R.id.txtFloatingReward);
        txtFloatingXP = findViewById(R.id.txtFloatingXP);

        btnShop = findViewById(R.id.btnShop);
        btnBuild = findViewById(R.id.btnBuild);
        btnWorkers = findViewById(R.id.btnWorkers);
        btnRocket = findViewById(R.id.btnRocket);
        btnSettings = findViewById(R.id.btnSettings);

        loadGame();
        updateHUD();
        MissionManager.initializeMissions();

        dailyMissions = MissionManager.getDailyMissions();
        storyMissions = MissionManager.getStoryMissions();

        if (!dailyMissions.isEmpty()) {
            currentMission = dailyMissions.get(0);
        }

        txtFloatingReward.setVisibility(TextView.GONE);
        txtFloatingXP.setVisibility(TextView.GONE);
        if (currentMission != null) {

            txtMission.setText(currentMission.getTitle());

            progressMission.setMax(currentMission.getGoal());

            progressMission.setProgress(currentMission.getProgress());

            txtMissionProgress.setText(
                    currentMission.getProgress()
                            + " / "
                            + currentMission.getGoal());
        }

        setupClickListeners();
    }
    private void setupClickListeners() {

        imgBackground.setOnClickListener(v -> {
            if (!running)
                return;
            tapMars();
        });

        imgAstronaut.setOnClickListener(v -> {

            if (!running)
                return;
            tapMars();
        });
        missionPanel.setOnClickListener(v -> {

            Intent intent = new Intent(GameActivity.this,
                    MissionActivity.class);
            startActivity(intent);
        });

        btnShop.setOnClickListener(v -> {

            Intent intent = new Intent(GameActivity.this,
                            ShopActivity.class);
            startActivity(intent);
        });

        btnBuild.setOnClickListener(v -> {

            Intent intent = new Intent(GameActivity.this,
                            BuildActivity.class);
            startActivity(intent);
        });

        btnWorkers.setOnClickListener(v -> {
            Intent intent =
                    new Intent(GameActivity.this,
                            WorkersActivity.class);
            startActivity(intent);
        });

        btnRocket.setOnClickListener(v -> {
            Intent intent = new Intent(GameActivity.this,
                            PlanetsActivity.class);
            startActivity(intent);
        });

        btnSettings.setOnClickListener(v -> {

            Intent intent = new Intent(GameActivity.this,
                            SettingsActivity.class);
            startActivity(intent);
        });
    }

    private void initializeGame() {

        updateHUD();

        if (currentMission != null) {

            txtMission.setText(currentMission.getTitle());

            progressMission.setMax(currentMission.getGoal());

            progressMission.setProgress(currentMission.getProgress());

            txtMissionProgress.setText(
                    currentMission.getProgress()
                            + " / "
                            + currentMission.getGoal());
        }

        txtFloatingReward.setVisibility(TextView.GONE);
        txtFloatingXP.setVisibility(TextView.GONE);
    }
    private void tapMars() {

        long currentTime = System.currentTimeMillis();
        tapCount++;
        if (currentTime - lastTapTime <= COMBO_RESET_TIME) {
            combo++;
        } else {
            combo = 1;
        }
        lastTapTime = currentTime;

        int energyGain = TAP_ENERGY_GAIN;
        int mineralGain = TAP_MINERAL_GAIN;
        int xpGain = TAP_XP_GAIN;

        boolean critical = false;

        if (random.nextInt(100) < CRITICAL_CHANCE) {

            critical = true;

            energyGain *= CRITICAL_MULTIPLIER;
            mineralGain *= CRITICAL_MULTIPLIER;
            xpGain *= CRITICAL_MULTIPLIER;
        }

        if (combo >= 10) {
            energyGain += 1;
            mineralGain += 2;
        }

        if (combo >= 25) {
            energyGain += 2;
            mineralGain += 3;
        }

        if (combo >= 50) {
            energyGain += 3;
            mineralGain += 4;
        }
        energy += energyGain;
        totalEnergyProduced += energyGain;
        minerals += mineralGain;
        MissionManager.addProgress(1, mineralGain);
        xp += xpGain;
        MissionManager.addProgress(3,1);

        checkLevelUp();
        animateAstronaut();

        showFloatingReward("+" + energyGain + " Energy");
        showFloatingXP("+" + xpGain + " XP");

        if (critical) {
            criticalTapCount++;
            MissionManager.addProgress(2,1);
            showFloatingReward("CRITICAL x3");
        }
        refreshMissionUI();
        updateHUD();
    }
    private void refreshMissionUI() {

        if(currentMission==null)
            return;

        txtMission.setText(currentMission.getTitle());

        progressMission.setMax(currentMission.getGoal());

        progressMission.setProgress(currentMission.getProgress());

        txtMissionProgress.setText(

                currentMission.getProgress()

                        + " / "

                        + currentMission.getGoal());

        if(currentMission.isCompleted()){

            showFloatingReward("Mission Complete!");

        }

    }
    private void animateAstronaut() {

        imgAstronaut.animate()
                .translationY(-20f)
                .setDuration(80)
                .withEndAction(() ->
                        imgAstronaut.animate()
                                .translationY(0)
                                .setDuration(80))
                .start();
    }

    private void showFloatingReward(String text) {

        txtFloatingReward.setText(text);
        txtFloatingReward.setAlpha(1f);
        txtFloatingReward.setTranslationY(0);
        txtFloatingReward.setVisibility(TextView.VISIBLE);
        txtFloatingReward.animate()
                .translationY(-150)
                .alpha(0f)
                .setDuration(900)
                .withEndAction(() ->
                        txtFloatingReward.setVisibility(TextView.GONE))
                .start();
    }

    private void showFloatingXP(String text) {

        txtFloatingXP.setText(text);
        txtFloatingXP.setAlpha(1f);
        txtFloatingXP.setTranslationY(0);
        txtFloatingXP.setVisibility(TextView.VISIBLE);
        txtFloatingXP.animate()
                .translationY(-110)
                .alpha(0f)
                .setDuration(800)
                .withEndAction(() ->
                        txtFloatingXP.setVisibility(TextView.GONE))
                .start();
    }

    private void startEnergySystem() {

        energyHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (!running)
                    return;

                energy += ENERGY_REGEN;
                updateHUD();
                energyHandler.postDelayed(this, ENERGY_REGEN_DELAY);
            }
        }, ENERGY_REGEN_DELAY);
    }
    private void startWorkerSystem() {

        workerHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (!running)
                    return;

                if (workers <= 0) {
                    workerHandler.postDelayed(
                            this,
                            WORKER_INTERVAL);
                    return;
                }

                int usableWorkers =
                        Math.min(workers, energy);

                if (usableWorkers > 0) {

                    energy -= usableWorkers;

                    int mineralsProduced =
                            usableWorkers * (3 + mineLevel);

                    minerals += mineralsProduced;

                    showFloatingReward("+" + mineralsProduced + " Minerals");

                    checkLevelUp();
                    updateHUD();
                }
                workerHandler.postDelayed(this, WORKER_INTERVAL);
            }
        }, WORKER_INTERVAL);
    }

    private void startAutoSaveSystem() {

        autoSaveHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (!running)
                    return;
                saveGame();

                autoSaveHandler.postDelayed(this, AUTO_SAVE_INTERVAL);
            }
        }, AUTO_SAVE_INTERVAL);
    }

    private void checkLevelUp() {
        int requiredXP = level * 100;

        while (xp >= requiredXP) {

            xp -= requiredXP;
            level++;
            MissionManager.addProgress(102, level);
            maxEnergy += 10;
            energy = maxEnergy;

            if (level % 5 == 0) {
                hqLevel++;
            }

            if (level == 2 && workers == 0) {
                workers = 1;
                showFloatingReward("Worker Unlocked!");
            }

            //------------------------------------
            // EVERY 3 LEVELS
            //------------------------------------

            if (level > 2 && level % 3 == 0) {
                workers++;
                showFloatingReward("+1 Worker");
            }

            //------------------------------------
            // NEXT REQUIREMENT
            //------------------------------------

            requiredXP = level * 100;
            showFloatingReward("LEVEL " + level);

        }
    }

    private void upgradeHQ() {
        int cost = hqLevel * 500;

        if (coins < cost)
            return;

        coins -= cost;
        coinsSpent += cost;
        hqLevel++;
        MissionManager.addProgress(102,1);
        maxEnergy += 20;
        energy = maxEnergy;
        refreshMissionUI();
        updateHUD();
    }

    //====================================================

    private void upgradeSolarPanel() {

        int cost = (solarLevel + 1) * 300;

        if (coins < cost)
            return;

        coins -= cost;
        coinsSpent += cost;
        solarLevel++;
        MissionManager.addProgress(101,1);
        refreshMissionUI();
        updateHUD();
    }

    //====================================================

    private void upgradeMine() {

        int cost = (mineLevel + 1) * 400;
        if (coins < cost)
            return;

        coins -= cost;
        coinsSpent += cost;
        mineLevel++;
        MissionManager.addProgress(101,1);

        refreshMissionUI();
        updateHUD();
    }

    //====================================================

    private void upgradeGreenhouse() {

        int cost = (greenhouseLevel + 1) * 600;
        if (coins < cost)
            return;

        coins -= cost;
        coinsSpent += cost;
        greenhouseLevel++;
        MissionManager.addProgress(101,1);

        refreshMissionUI();
        workers++;

        showFloatingReward("+1 Colonist");

        updateHUD();

    }

    //====================================================

    private void upgradeOxygenPlant() {

        int cost = (oxygenPlantLevel + 1) * 800;
        if (coins < cost)
            return;

        coins -= cost;
        coinsSpent += cost;
        oxygenPlantLevel++;
        MissionManager.addProgress(101,1);

        refreshMissionUI();
        maxEnergy += 25;
        energy = maxEnergy;

        updateHUD();
    }
    //====================================================
    // UPDATE HUD
    //====================================================

    private void updateHUD() {

        txtCoins.setText(
                String.format(Locale.getDefault(),
                        "%,d", coins));

        txtMinerals.setText(
                String.format(Locale.getDefault(),
                        "%,d", minerals));

        txtEnergy.setText(String.valueOf(energy));
        txtLevel.setText("Lv. " + level);
        txtWorkers.setText("Workers : " + workers);
    }

    //====================================================
    // SAVE GAME
    //====================================================

    private void saveGame() {

        SharedPreferences.Editor editor = preferences.edit();

        editor.putLong("coins", coins);
        editor.putLong("minerals", minerals);

        editor.putInt("energy", energy);
        editor.putInt("maxEnergy", maxEnergy);

        editor.putInt("xp", xp);
        editor.putInt("level", level);

        editor.putInt("workers", workers);

        editor.putInt("hqLevel", hqLevel);
        editor.putInt("solarLevel", solarLevel);
        editor.putInt("mineLevel", mineLevel);
        editor.putInt("greenhouseLevel", greenhouseLevel);
        editor.putInt("oxygenPlantLevel", oxygenPlantLevel);

        editor.apply();
    }

    //====================================================
    // LOAD GAME
    //====================================================

    private void loadGame() {

        coins = preferences.getLong("coins", 0);
        minerals = preferences.getLong("minerals", 0);
        energy = preferences.getInt("energy", 0);
        maxEnergy = preferences.getInt("maxEnergy", 100);
        xp = preferences.getInt("xp", 0);
        level = preferences.getInt("level", 1);
        workers = preferences.getInt("workers", 0);
        hqLevel = preferences.getInt("hqLevel", 1);
        solarLevel = preferences.getInt("solarLevel", 0);
        mineLevel = preferences.getInt("mineLevel", 0);
        greenhouseLevel = preferences.getInt("greenhouseLevel", 0);
        oxygenPlantLevel = preferences.getInt("oxygenPlantLevel", 0);
        MissionManager.initializeMissions();

        dailyMissions = MissionManager.getDailyMissions();

        storyMissions = MissionManager.getStoryMissions();

        if (!dailyMissions.isEmpty()) {

            currentMission = dailyMissions.get(0);

        }
    }

    //====================================================
    // GAME LOOP
    //====================================================

    private void startGameLoop() {
        gameHandler.postDelayed(new Runnable() {

            @Override
            public void run() {

                if (!running)
                    return;

                update();
                gameHandler.postDelayed(this, FRAME_DELAY);
            }
        }, FRAME_DELAY);
    }

    private void update() {
        totalPlaySeconds += FRAME_DELAY / 1000.0;
        //----------------------------------------
        // Future Systems
        //----------------------------------------

        // Particle System

        // Day / Night

        // Weather

        // Rockets

        // Achievements

        // Sound
    }

    @Override
    protected void onResume() {

        super.onResume();
        running = true;

        initializeGame();
        refreshMissionUI();
        startGameLoop();
        startEnergySystem();
        startWorkerSystem();
        startAutoSaveSystem();
    }

    @Override
    protected void onPause() {

        super.onPause();
        running = false;
        saveGame();

        gameHandler.removeCallbacksAndMessages(null);
        energyHandler.removeCallbacksAndMessages(null);
        workerHandler.removeCallbacksAndMessages(null);
        autoSaveHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        running = false;
        saveGame();

        gameHandler.removeCallbacksAndMessages(null);
        energyHandler.removeCallbacksAndMessages(null);
        workerHandler.removeCallbacksAndMessages(null);
        autoSaveHandler.removeCallbacksAndMessages(null);
    }
}