package com.example.beyondmars;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
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

    private LinearLayout missionPanel;

    private ImageView imgBackground;
    private ImageView imgAstronaut;
    private ImageView imgStars;

    private FrameLayout floatingResourceContainer;
    private FrameLayout particleContainer;

    private TextView txtCoins;
    private TextView txtMinerals;
    private TextView txtEnergy;
    private TextView txtLevel;
    private TextView txtWorkers;
    private TextView txtMission;
    private TextView txtMissionProgress;
    private TextView txtFloatingReward;
    private TextView txtFloatingXP;
    private TextView txtMineralRate;

    private ProgressBar progressMission;

    private ImageButton btnShop;
    private ImageButton btnBuild;
    private ImageButton btnWorkers;
    private ImageButton btnRocket;
    private ImageButton btnSettings;


    // ============================================================
    // GAME DATA
    // ============================================================

    private long coins = 0;
    private long minerals = 0;

    private int energy = 100;
    private int maxEnergy = 100;

    private int xp = 0;
    private int level = 1;
    private int workers = 0;


    // ============================================================
    // BUILDINGS
    // ============================================================

    private int hqLevel = 1;
    private int solarLevel = 0;
    private int mineLevel = 0;
    private int greenhouseLevel = 0;
    private int oxygenPlantLevel = 0;


    // ============================================================
    // GAME CONSTANTS
    // ============================================================

    /*
     * Player tap:
     * Energy -1
     * Minerals +5
     * XP +1
     */
    private static final int TAP_ENERGY_COST = 1;
    private static final int TAP_MINERAL_GAIN = 5;
    private static final int TAP_XP_GAIN = 1;

    /*
     * Energy regeneration.
     */
    private static final int BASE_ENERGY_REGEN = 1;
    private static final int ENERGY_REGEN_DELAY = 1000;

    /*
     * Idle production.
     */
    private static final int WORKER_INTERVAL = 2000;

    /*
     * Auto save.
     */
    private static final int AUTO_SAVE_INTERVAL = 10000;

    /*
     * Main game loop.
     */
    private static final int FRAME_DELAY = 16;

    /*
     * Combo.
     */
    private static final long COMBO_RESET_TIME = 1800;

    /*
     * Critical tap.
     */
    private static final int CRITICAL_CHANCE = 10;
    private static final int CRITICAL_MULTIPLIER = 3;


    // ============================================================
    // STATS
    // ============================================================

    private int combo = 0;
    private long lastTapTime = 0;

    private int tapCount = 0;
    private int criticalTapCount = 0;

    private long coinsSpent = 0;
    private long totalEnergyProduced = 0;

    private float totalPlaySeconds = 0;


    // ============================================================
    // MISSIONS
    // ============================================================

    private ArrayList<Mission> dailyMissions;
    private ArrayList<Mission> storyMissions;

    private Mission currentMission;


    // ============================================================
    // GAME STATE
    // ============================================================

    private boolean running = false;

    private boolean dayMode = true;
    private boolean soundEnabled = true;
    private boolean vibrationEnabled = true;
    private boolean musicEnabled = true;


    // ============================================================
    // RANDOM
    // ============================================================

    private final Random random = new Random();


    // ============================================================
    // HANDLERS
    // ============================================================

    private final Handler gameHandler =
            new Handler(Looper.getMainLooper());

    private final Handler energyHandler =
            new Handler(Looper.getMainLooper());

    private final Handler workerHandler =
            new Handler(Looper.getMainLooper());

    private final Handler autoSaveHandler =
            new Handler(Looper.getMainLooper());


    // ============================================================
    // SAVE
    // ============================================================

    private SharedPreferences preferences;


    // ============================================================
    // ON CREATE
    // ============================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        preferences = getSharedPreferences(
                "BeyondMarsSave",
                MODE_PRIVATE
        );

        initializeViews();

        loadGame();

        MissionManager.initializeMissions();

        dailyMissions =
                MissionManager.getDailyMissions();

        storyMissions =
                MissionManager.getStoryMissions();

        if (dailyMissions != null &&
                !dailyMissions.isEmpty()) {

            currentMission =
                    dailyMissions.get(0);
        }

        setupClickListeners();

        updateMissionUI();

        updateHUD();
    }


    // ============================================================
    // INITIALIZE VIEWS
    // ============================================================

    private void initializeViews() {

        imgBackground =
                findViewById(R.id.imgBackground);

        imgAstronaut =
                findViewById(R.id.imgAstronaut);

        imgStars =
                findViewById(R.id.imgStars);

        floatingResourceContainer =
                findViewById(
                        R.id.floatingResourceContainer
                );

        particleContainer =
                findViewById(
                        R.id.particleContainer
                );


        // HUD

        txtCoins =
                findViewById(R.id.txtCoins);

        txtMinerals =
                findViewById(R.id.txtMinerals);

        txtEnergy =
                findViewById(R.id.txtEnergy);

        txtLevel =
                findViewById(R.id.txtLevel);

        txtWorkers =
                findViewById(R.id.txtWorkers);

        txtMineralRate =
                findViewById(R.id.txtMineralRate);


        // Mission

        missionPanel =
                findViewById(R.id.missionPanel);

        txtMission =
                findViewById(R.id.txtMission);

        txtMissionProgress =
                findViewById(
                        R.id.txtMissionProgress
                );

        progressMission =
                findViewById(
                        R.id.progressMission
                );


        // Floating text

        txtFloatingReward =
                findViewById(
                        R.id.txtFloatingReward
                );

        txtFloatingXP =
                findViewById(
                        R.id.txtFloatingXP
                );


        // Buttons

        btnShop =
                findViewById(R.id.btnShop);

        btnBuild =
                findViewById(R.id.btnBuild);

        btnWorkers =
                findViewById(R.id.btnWorkers);

        btnRocket =
                findViewById(R.id.btnRocket);

        btnSettings =
                findViewById(R.id.btnSettings);
    }


    // ============================================================
    // CLICK LISTENERS
    // ============================================================

    private void setupClickListeners() {

        /*
         * Tapping Mars background.
         */
        imgBackground.setOnClickListener(v -> {

            if (!running)
                return;

            tapMars();
        });


        /*
         * Tapping astronaut.
         */
        imgAstronaut.setOnClickListener(v -> {

            if (!running)
                return;

            tapMars();
        });


        /*
         * Mission.
         */
        missionPanel.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            GameActivity.this,
                            MissionActivity.class
                    );

            startActivity(intent);
        });


        /*
         * Shop.
         */
        btnShop.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            GameActivity.this,
                            ShopActivity.class
                    );

            startActivity(intent);
        });


        /*
         * Build.
         */
        btnBuild.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            GameActivity.this,
                            BuildActivity.class
                    );

            startActivity(intent);
        });


        /*
         * Workers.
         */
        btnWorkers.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            GameActivity.this,
                            WorkersActivity.class
                    );

            startActivity(intent);
        });


        /*
         * Rocket.
         */
        btnRocket.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            GameActivity.this,
                            PlanetsActivity.class
                    );

            startActivity(intent);
        });


        /*
         * Settings.
         */
        btnSettings.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            GameActivity.this,
                            SettingsActivity.class
                    );

            startActivity(intent);
        });
    }


    // ============================================================
    // MAIN GAME INITIALIZATION
    // ============================================================

    private void initializeGame() {

        updateHUD();
        updateMissionUI();

        if (txtFloatingReward != null) {

            txtFloatingReward.setVisibility(
                    View.GONE
            );
        }

        if (txtFloatingXP != null) {

            txtFloatingXP.setVisibility(
                    View.GONE
            );
        }
    }


    // ============================================================
    // PLAYER TAP
    // ============================================================



    private void tapMars() {

        // Cannot mine without energy
        if (energy <= 0) {
            showFloatingReward("⚡ No Energy");
            return;
        }

        long currentTime = System.currentTimeMillis();

        tapCount++;

        if (currentTime - lastTapTime <= COMBO_RESET_TIME) {
            combo++;
        } else {
            combo = 1;
        }

        lastTapTime = currentTime;

        int mineralGain = TAP_MINERAL_GAIN;
        int xpGain = TAP_XP_GAIN;

        boolean critical =
                random.nextInt(100) < CRITICAL_CHANCE;

        if (critical) {

            mineralGain *= CRITICAL_MULTIPLIER;
            xpGain *= CRITICAL_MULTIPLIER;

            criticalTapCount++;

            MissionManager.addProgress(2, 1);
        }

        // Combo bonus
        if (combo >= 10) {
            mineralGain += 2;
        }

        if (combo >= 25) {
            mineralGain += 3;
        }

        if (combo >= 50) {
            mineralGain += 4;
        }

        // Consume energy
        energy = Math.max(
                0,
                energy - TAP_ENERGY_COST
        );

        // Give minerals
        minerals += mineralGain;

        // Give XP
        xp += xpGain;

        // Mission progress
        MissionManager.addProgress(
                1,
                mineralGain
        );

        MissionManager.addProgress(
                3,
                1
        );

        checkLevelUp();

        animateAstronaut();

        if (critical) {

            showFloatingReward(
                    "CRITICAL x3  +" +
                            mineralGain +
                            " Minerals"
            );

        } else {

            showFloatingReward(
                    "+" +
                            mineralGain +
                            " Minerals"
            );
        }

        showFloatingXP(
                "+" +
                        xpGain +
                        " XP"
        );

        refreshMissionUI();

        updateHUD();

        saveGame();
    }
    private void animateAstronaut() {

        imgAstronaut.animate()
                .scaleX(1.08f)
                .scaleY(1.08f)
                .setDuration(80)
                .withEndAction(() -> {

                    imgAstronaut.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                            .start();
                })
                .start();
    }

    private void showFloatingReward(String text) {

        if (txtFloatingReward == null)
            return;

        txtFloatingReward.setText(text);

        txtFloatingReward.setAlpha(1f);

        txtFloatingReward.setTranslationY(0);

        txtFloatingReward.setVisibility(
                View.VISIBLE
        );

        txtFloatingReward.animate()
                .translationY(-150)
                .alpha(0f)
                .setDuration(900)
                .withEndAction(() ->
                        txtFloatingReward
                                .setVisibility(
                                        View.GONE
                                )
                )
                .start();
    }


    // ============================================================
    // FLOATING XP
    // ============================================================

    private void showFloatingXP(String text) {

        if (txtFloatingXP == null)
            return;

        txtFloatingXP.setText(text);

        txtFloatingXP.setAlpha(1f);

        txtFloatingXP.setTranslationY(0);

        txtFloatingXP.setVisibility(
                View.VISIBLE
        );

        txtFloatingXP.animate()
                .translationY(-110)
                .alpha(0f)
                .setDuration(800)
                .withEndAction(() ->
                        txtFloatingXP
                                .setVisibility(
                                        View.GONE
                                )
                )
                .start();
    }
    private int getEnergyRate() {

        return 1 + solarLevel;
    }
    private void startIdleResourceSystem() {

        energyHandler.postDelayed(
                new Runnable() {

                    @Override
                    public void run() {

                        if (!running)
                            return;


                        // Base passive income
                        energy += getEnergyRate();

                       // gems += 1;

                        coins += 1;


                        // Colony mineral production
                        minerals += getMineralRate();


                        // Mission progress
                        MissionManager.addProgress(
                                1,
                                getMineralRate()
                        );


                        updateHUD();

                        refreshMissionUI();


                        energyHandler.postDelayed(
                                this,
                                1000
                        );
                    }

                },
                1000
        );
    }
    private void startWorkerSystem() {

        workerHandler.postDelayed(new Runnable() {

            @Override
            public void run() {

                if (!running)
                    return;

                if (workers > 0) {

                    int mineralsProduced =
                            workers * (3 + mineLevel);

                    int coinsProduced =
                            workers * (2 + mineLevel);

                    minerals += mineralsProduced;

                    coins += coinsProduced;

                    MissionManager.addProgress(
                            1,
                            mineralsProduced
                    );

                    xp += Math.max(
                            1,
                            mineralsProduced / 5
                    );

                    checkLevelUp();

                    updateHUD();

                    refreshMissionUI();
                }

                workerHandler.postDelayed(
                        this,
                        WORKER_INTERVAL
                );
            }

        }, WORKER_INTERVAL);
    }
    private int getMineralRate() {

        int baseRate = 1;

        int workerRate =
                workers * (3 + mineLevel);

        int oxygenBonus =
                oxygenPlantLevel;

        int hqBonus =
                hqLevel - 1;

        return baseRate
                + workerRate
                + oxygenBonus
                + hqBonus;
    }


    // ============================================================
    // AUTO SAVE
    // ============================================================

    private void startAutoSaveSystem() {

        autoSaveHandler.postDelayed(
                new Runnable() {

                    @Override
                    public void run() {

                        if (!running)
                            return;

                        saveGame();

                        autoSaveHandler.postDelayed(
                                this,
                                AUTO_SAVE_INTERVAL
                        );
                    }

                },
                AUTO_SAVE_INTERVAL
        );
    }


    // ============================================================
    // LEVEL SYSTEM
    // ============================================================

    private void checkLevelUp() {

        int requiredXP =
                level * 100;


        while (xp >= requiredXP) {

            xp -= requiredXP;

            level++;


            /*
             * Mission:
             * Reach colony levels.
             */
            MissionManager.addProgress(
                    102,
                    1
            );


            /*
             * Level increases max energy.
             */
            maxEnergy += 10;

            energy = maxEnergy;


            /*
             * Every 5 levels:
             * HQ automatically improves.
             */
            if (level % 5 == 0) {

                hqLevel++;

                showFloatingReward(
                        "HQ LEVEL UP!"
                );
            }


            /*
             * Level 2 unlocks first worker.
             */
            if (level == 2 &&
                    workers == 0) {

                workers = 1;

                showFloatingReward(
                        "Worker Unlocked!"
                );
            }


            /*
             * Every 3 levels:
             * +1 worker.
             */
            if (level > 2 &&
                    level % 3 == 0) {

                workers++;

                showFloatingReward(
                        "+1 Worker"
                );
            }


            requiredXP =
                    level * 100;


            showFloatingXP(
                    "LEVEL " +
                            level +
                            "!"
            );
        }
    }


    // ============================================================
    // BUILDING: HQ
    // ============================================================

    private void upgradeHQ() {

        int cost =
                hqLevel * 500;


        if (coins < cost)
            return;


        coins -= cost;

        coinsSpent += cost;

        hqLevel++;


        MissionManager.addProgress(
                102,
                1
        );


        maxEnergy += 20;

        energy = maxEnergy;


        showFloatingReward(
                "HQ Lv." +
                        hqLevel
        );


        updateHUD();

        updateMissionUI();

        saveGame();
    }


    // ============================================================
    // BUILDING: SOLAR
    // ============================================================

    private void upgradeSolarPanel() {

        int cost =
                (solarLevel + 1) * 300;


        if (coins < cost)
            return;


        coins -= cost;

        coinsSpent += cost;

        solarLevel++;


        MissionManager.addProgress(
                101,
                1
        );


        showFloatingReward(
                "Solar Lv." +
                        solarLevel
        );


        updateHUD();

        updateMissionUI();

        saveGame();
    }


    // ============================================================
    // BUILDING: MINE
    // ============================================================

    private void upgradeMine() {

        int cost =
                (mineLevel + 1) * 400;


        if (coins < cost)
            return;


        coins -= cost;

        coinsSpent += cost;

        mineLevel++;


        MissionManager.addProgress(
                101,
                1
        );


        showFloatingReward(
                "Mine Lv." +
                        mineLevel
        );


        updateHUD();

        updateMissionUI();

        saveGame();
    }


    // ============================================================
    // BUILDING: GREENHOUSE
    // ============================================================

    private void upgradeGreenhouse() {

        int cost =
                (greenhouseLevel + 1) * 600;


        if (coins < cost)
            return;


        coins -= cost;

        coinsSpent += cost;

        greenhouseLevel++;


        MissionManager.addProgress(
                101,
                1
        );


        /*
         * Greenhouse supports
         * colonists/workers.
         */
        workers++;


        showFloatingReward(
                "+1 Colonist"
        );


        updateHUD();

        updateMissionUI();

        saveGame();
    }


    // ============================================================
    // BUILDING: OXYGEN PLANT
    // ============================================================

    private void upgradeOxygenPlant() {

        int cost =
                (oxygenPlantLevel + 1) * 800;


        if (coins < cost)
            return;


        coins -= cost;

        coinsSpent += cost;

        oxygenPlantLevel++;


        MissionManager.addProgress(
                101,
                1
        );


        /*
         * Oxygen Plant expands
         * colony energy capacity.
         */
        maxEnergy += 25;

        energy = maxEnergy;


        showFloatingReward(
                "Oxygen Plant Lv." +
                        oxygenPlantLevel
        );


        updateHUD();

        updateMissionUI();

        saveGame();
    }


    // ============================================================
    // HUD
    // ============================================================

    private void updateHUD() {

        if (txtCoins != null) {

            txtCoins.setText(
                    String.format(
                            Locale.getDefault(),
                            "%,d",
                            coins
                    )
            );
        }


        if (txtMinerals != null) {

            txtMinerals.setText(
                    String.format(
                            Locale.getDefault(),
                            "%,d",
                            minerals
                    )
            );
        }


        if (txtEnergy != null) {

            txtEnergy.setText(
                    String.valueOf(energy)
            );
        }


        if (txtLevel != null) {

            txtLevel.setText(
                    "Lv. " +
                            level
            );
        }


        if (txtWorkers != null) {

            txtWorkers.setText(
                    "Workers : " +
                            workers
            );
        }


        /*
         * New idle production display.
         */
        if (txtMineralRate != null) {

            txtMineralRate.setText(
                    "⛏ +" +
                            getMineralRate() +
                            " Minerals/sec"
            );
        }
    }


    // ============================================================
    // MISSION UI
    // ============================================================
    private void refreshMissionUI() {

        if (currentMission == null)
            return;

        txtMission.setText(
                currentMission.getTitle()
        );

        progressMission.setMax(
                currentMission.getGoal()
        );

        progressMission.setProgress(
                Math.min(
                        currentMission.getProgress(),
                        currentMission.getGoal()
                )
        );

        txtMissionProgress.setText(
                currentMission.getProgress()
                        + " / "
                        + currentMission.getGoal()
        );
    }
    private void updateMissionUI() {

        if (currentMission == null)
            return;


        int progress =
                currentMission.getProgress();

        int goal =
                currentMission.getGoal();


        txtMission.setText(
                currentMission.getTitle()
        );


        progressMission.setMax(goal);

        progressMission.setProgress(
                Math.min(
                        progress,
                        goal
                )
        );


        txtMissionProgress.setText(
                progress +
                        " / " +
                        goal
        );
    }


    // ============================================================
    // SAVE GAME
    // ============================================================

    private void saveGame() {

        if (preferences == null)
            return;


        SharedPreferences.Editor editor =
                preferences.edit();


        editor.putLong(
                "coins",
                coins
        );

        editor.putLong(
                "minerals",
                minerals
        );


        editor.putInt(
                "energy",
                energy
        );

        editor.putInt(
                "maxEnergy",
                maxEnergy
        );


        editor.putInt(
                "xp",
                xp
        );

        editor.putInt(
                "level",
                level
        );

        editor.putInt(
                "workers",
                workers
        );


        editor.putInt(
                "hqLevel",
                hqLevel
        );

        editor.putInt(
                "solarLevel",
                solarLevel
        );

        editor.putInt(
                "mineLevel",
                mineLevel
        );

        editor.putInt(
                "greenhouseLevel",
                greenhouseLevel
        );

        editor.putInt(
                "oxygenPlantLevel",
                oxygenPlantLevel
        );


        editor.apply();
    }


    // ============================================================
    // LOAD GAME
    // ============================================================

    private void loadGame() {

        coins =
                preferences.getLong(
                        "coins",
                        0
                );


        minerals =
                preferences.getLong(
                        "minerals",
                        0
                );


        energy =
                preferences.getInt(
                        "energy",
                        100
                );


        maxEnergy =
                preferences.getInt(
                        "maxEnergy",
                        100
                );


        xp =
                preferences.getInt(
                        "xp",
                        0
                );


        level =
                preferences.getInt(
                        "level",
                        1
                );


        workers =
                preferences.getInt(
                        "workers",
                        0
                );


        hqLevel =
                preferences.getInt(
                        "hqLevel",
                        1
                );


        solarLevel =
                preferences.getInt(
                        "solarLevel",
                        0
                );


        mineLevel =
                preferences.getInt(
                        "mineLevel",
                        0
                );


        greenhouseLevel =
                preferences.getInt(
                        "greenhouseLevel",
                        0
                );


        oxygenPlantLevel =
                preferences.getInt(
                        "oxygenPlantLevel",
                        0
                );
    }


    // ============================================================
    // GAME LOOP
    // ============================================================

    private void startGameLoop() {

        gameHandler.postDelayed(
                new Runnable() {

                    @Override
                    public void run() {

                        if (!running)
                            return;


                        update();


                        gameHandler.postDelayed(
                                this,
                                FRAME_DELAY
                        );
                    }

                },
                FRAME_DELAY
        );
    }


    // ============================================================
    // UPDATE
    // ============================================================

    private void update() {

        totalPlaySeconds +=
                FRAME_DELAY / 1000.0f;


        /*
         * Future systems can be
         * added here:
         *
         * Day/Night
         * Weather
         * Particles
         * Rockets
         * Achievements
         * Audio
         */
    }


    // ============================================================
    // RESUME
    // ============================================================

    @Override
    protected void onResume() {

        super.onResume();

        running = true;


        /*
         * Refresh because another
         * Activity may have changed
         * workers/buildings/resources.
         */
        loadGame();

        MissionManager.initializeMissions();

        dailyMissions =
                MissionManager.getDailyMissions();

        storyMissions =
                MissionManager.getStoryMissions();


        if (dailyMissions != null &&
                !dailyMissions.isEmpty()) {

            currentMission =
                    dailyMissions.get(0);
        }


        initializeGame();


        startGameLoop();

        startIdleResourceSystem();

        startWorkerSystem();

        startAutoSaveSystem();
    }


    // ============================================================
    // PAUSE
    // ============================================================

    @Override
    protected void onPause() {

        super.onPause();

        running = false;


        saveGame();


        gameHandler.removeCallbacksAndMessages(
                null
        );

        energyHandler.removeCallbacksAndMessages(
                null
        );

        workerHandler.removeCallbacksAndMessages(
                null
        );

        autoSaveHandler.removeCallbacksAndMessages(
                null
        );
    }


    // ============================================================
    // DESTROY
    // ============================================================

    @Override
    protected void onDestroy() {

        super.onDestroy();

        running = false;


        saveGame();


        gameHandler.removeCallbacksAndMessages(
                null
        );

        energyHandler.removeCallbacksAndMessages(
                null
        );

        workerHandler.removeCallbacksAndMessages(
                null
        );

        autoSaveHandler.removeCallbacksAndMessages(
                null
        );
    }
}