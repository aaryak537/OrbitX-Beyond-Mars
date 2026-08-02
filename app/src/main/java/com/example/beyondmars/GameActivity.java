package com.example.beyondmars;

import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

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

    private int currentMission = 0;

    private int missionGoal = 100;
    private int missionProgress = 0;

    private int completedMissions = 0;

    private final Random missionRandom = new Random();

    private static final int MISSION_COLLECT_MINERALS = 0;
    private static final int MISSION_EARN_COINS = 1;
    private static final int MISSION_GAIN_ENERGY = 2;
    private static final int MISSION_REACH_LEVEL = 3;
    private static final int MISSION_HIRE_WORKERS = 4;
    private static final int MISSION_UPGRADE_HQ = 5;
    private static final int MISSION_UPGRADE_SOLAR = 6;
    private static final int MISSION_UPGRADE_MINE = 7;
    private static final int MISSION_UPGRADE_GREENHOUSE = 8;
    private static final int MISSION_UPGRADE_OXYGEN = 9;
    private static final int MISSION_TAP_MARS = 10;
    private static final int MISSION_CRITICAL_TAPS = 11;
    private static final int MISSION_SPEND_COINS = 12;
    private static final int MISSION_COLLECT_XP = 13;
    private static final int MISSION_BUILD_BASE = 14;
    private static final int MISSION_COLLECT_500_MINERALS = 15;
    private static final int MISSION_COLLECT_1000_MINERALS = 16;
    private static final int MISSION_EARN_5000_COINS = 17;
    private static final int MISSION_REACH_LEVEL10 = 18;
    private static final int MISSION_PLAY_GAME = 19;
    private static final int MISSION_COMPLETE_5 = 20;

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
        generateRandomMission();

        txtFloatingReward.setVisibility(TextView.GONE);
        txtFloatingXP.setVisibility(TextView.GONE);
        progressMission.setMax(missionGoal);
        progressMission.setProgress(missionProgress);

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
        updateMission();

        txtFloatingReward.setVisibility(TextView.GONE);
        txtFloatingXP.setVisibility(TextView.GONE);
    }
    private void generateRandomMission() {

        currentMission = missionRandom.nextInt(21);

        missionProgress = 0;

        switch (currentMission) {

            case MISSION_COLLECT_MINERALS:
                missionGoal = 100;
                txtMission.setText("Collect 100 Minerals");
                break;

            case MISSION_EARN_COINS:
                missionGoal = 500;
                txtMission.setText("Earn 500 Coins");
                break;

            case MISSION_GAIN_ENERGY:
                missionGoal = 200;
                txtMission.setText("Generate 200 Energy");
                break;

            case MISSION_REACH_LEVEL:
                missionGoal = level + 1;
                txtMission.setText("Reach Level " + missionGoal);
                break;

            case MISSION_HIRE_WORKERS:
                missionGoal = workers + 2;
                txtMission.setText("Hire 2 Workers");
                break;

            case MISSION_UPGRADE_HQ:
                missionGoal = hqLevel + 1;
                txtMission.setText("Upgrade HQ");
                break;

            case MISSION_UPGRADE_SOLAR:
                missionGoal = solarLevel + 1;
                txtMission.setText("Upgrade Solar Panel");
                break;

            case MISSION_UPGRADE_MINE:
                missionGoal = mineLevel + 1;
                txtMission.setText("Upgrade Mine");
                break;

            case MISSION_UPGRADE_GREENHOUSE:
                missionGoal = greenhouseLevel + 1;
                txtMission.setText("Upgrade Greenhouse");
                break;

            case MISSION_UPGRADE_OXYGEN:
                missionGoal = oxygenPlantLevel + 1;
                txtMission.setText("Upgrade Oxygen Plant");
                break;

            case MISSION_TAP_MARS:
                missionGoal = 100;
                txtMission.setText("Tap Mars 100 Times");
                break;

            case MISSION_CRITICAL_TAPS:
                missionGoal = 10;
                txtMission.setText("Get 10 Critical Taps");
                break;

            case MISSION_SPEND_COINS:
                missionGoal = 1000;
                txtMission.setText("Spend 1000 Coins");
                break;

            case MISSION_COLLECT_XP:
                missionGoal = 500;
                txtMission.setText("Collect 500 XP");
                break;

            case MISSION_BUILD_BASE:
                missionGoal = 1;
                txtMission.setText("Build Mars Base");
                break;

            case MISSION_COLLECT_500_MINERALS:
                missionGoal = 500;
                txtMission.setText("Collect 500 Minerals");
                break;

            case MISSION_COLLECT_1000_MINERALS:
                missionGoal = 1000;
                txtMission.setText("Collect 1000 Minerals");
                break;

            case MISSION_EARN_5000_COINS:
                missionGoal = 5000;
                txtMission.setText("Earn 5000 Coins");
                break;

            case MISSION_REACH_LEVEL10:
                missionGoal = 10;
                txtMission.setText("Reach Level 10");
                break;

            case MISSION_PLAY_GAME:
                missionGoal = 300;
                txtMission.setText("Play for 5 Minutes");
                break;

            case MISSION_COMPLETE_5:
                missionGoal = 5;
                txtMission.setText("Complete 5 Missions");
                break;
        }

        progressMission.setMax(missionGoal);
        progressMission.setProgress(0);
        txtMissionProgress.setText("0 / " + missionGoal);
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

        xp += xpGain;

        missionProgress += mineralGain;

        checkLevelUp();
        animateAstronaut();

        showFloatingReward("+" + energyGain + " Energy");
        showFloatingXP("+" + xpGain + " XP");

        if (critical) {
            criticalTapCount++;
            showFloatingReward("CRITICAL x3");
        }

        updateMission();
        updateHUD();
        updateMission();
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
                    missionProgress += mineralsProduced;

                    showFloatingReward("+" + mineralsProduced + " Minerals");

                    checkLevelUp();
                    updateMission();
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

    private void updateMission() {

        switch (currentMission) {

            case MISSION_COLLECT_MINERALS:
                missionProgress = (int) minerals;
                break;

            case MISSION_EARN_COINS:
                missionProgress = (int) coins;
                break;

            case MISSION_GAIN_ENERGY:
                missionProgress = (int) totalEnergyProduced;
                break;

            case MISSION_REACH_LEVEL:
                missionProgress = level;
                break;

            case MISSION_HIRE_WORKERS:
                missionProgress = workers;
                break;

            case MISSION_UPGRADE_HQ:
                missionProgress = hqLevel;
                break;

            case MISSION_UPGRADE_SOLAR:
                missionProgress = solarLevel;
                break;

            case MISSION_UPGRADE_MINE:
                missionProgress = mineLevel;
                break;

            case MISSION_UPGRADE_GREENHOUSE:
                missionProgress = greenhouseLevel;
                break;

            case MISSION_UPGRADE_OXYGEN:
                missionProgress = oxygenPlantLevel;
                break;

            case MISSION_TAP_MARS:
                missionProgress = tapCount;
                break;

            case MISSION_CRITICAL_TAPS:
                missionProgress = criticalTapCount;
                break;

            case MISSION_SPEND_COINS:
                missionProgress = (int) coinsSpent;
                break;

            case MISSION_COLLECT_XP:
                missionProgress = xp;
                break;

            case MISSION_REACH_LEVEL10:
                missionProgress = level;
                break;

            case MISSION_PLAY_GAME:
                missionProgress = (int) totalPlaySeconds;
                break;

            case MISSION_COMPLETE_5:
                missionProgress = completedMissions;
                break;
        }

        progressMission.setMax(missionGoal);
        progressMission.setProgress(Math.min(missionProgress, missionGoal));

        txtMissionProgress.setText(
                missionProgress + " / " + missionGoal
                        + "   (" + completedMissions + " Done)");

        if (missionProgress >= missionGoal) {
            completeMission();
        }
    }

    private void completeMission() {

        completedMissions++;

        int coinReward = 300 + (completedMissions * 100);
        int xpReward = 50 + (completedMissions * 20);

        coins += coinReward;
        xp += xpReward;

        showFloatingReward("+" + coinReward + " Coins");
        showFloatingXP("+" + xpReward + " XP");

        checkLevelUp();

        generateRandomMission();

        updateHUD();
    }

    //====================================================
    // BUILDING UPGRADES
    //====================================================

    private void upgradeHQ() {
        int cost = hqLevel * 500;

        if (coins < cost)
            return;

        coins -= cost;
        coinsSpent += cost;
        hqLevel++;
        maxEnergy += 20;
        energy = maxEnergy;
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

        editor.putInt("missionGoal", missionGoal);
        editor.putInt("missionProgress", missionProgress);

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
        missionGoal = preferences.getInt("missionGoal", 100);
        missionProgress = preferences.getInt("missionProgress", 0);
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