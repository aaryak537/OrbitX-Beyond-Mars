package com.example.beyondmars;

import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
     RelativeLayout rootLayout;
     ImageView imgMars,imgRocket,imgBackground,imgSun,imgMoon;
     ImageButton btnTap;
     Button btnUpgrade,btnRocket,btnWorkers, btnMission;
    private TextView txtIncome;
     TextView txtCoins,txtGems,txtLevel,txtRocket,txtWorkers,txtMission,txtDay,txtMultiplier;
     ProgressBar progressMission;
    /*==========================================================
                      GAME DATA
     ==========================================================*/

    private double coins = 0,tapPower = 1,autoIncome = 0,multiplier = 1.0;
    private int gems = 0,level = 1,workers = 0,rockets = 1,currentMission = 1,day = 1;
    private boolean isNight = false;

    /*==========================================================
                     UPGRADE LEVELS
     ==========================================================*/
    private int tapUpgradeLevel = 0,workerUpgradeLevel = 0,rocketUpgradeLevel = 0,incomeUpgradeLevel = 0;

    /*==========================================================
                     GAME COSTS
     ==========================================================*/
    private double tapUpgradeCost = 50,workerCost = 250,rocketCost = 1000,incomeUpgradeCost = 500;

    /*==========================================================
                     MISSION DATA
     ==========================================================*/

    private int missionProgress = 0,missionGoal = 100,missionReward = 50;

    /*==========================================================
                      GAME LOOP
     =========================================================*/
    private final Handler gameHandler =
            new Handler(Looper.getMainLooper());

    private final Handler autoIncomeHandler =
            new Handler(Looper.getMainLooper());

    private final Handler saveHandler =
            new Handler(Looper.getMainLooper());

    private final Handler animationHandler =
            new Handler(Looper.getMainLooper());

    /*==========================================================
                       AUDIO
     ==========================================================*/
     MediaPlayer backgroundMusic;

     SoundPool soundPool;

    private int tapSound,upgradeSound,rewardSound;
    private boolean musicEnabled = true,soundEnabled = true;

    /*==========================================================
                     SAVE SYSTEM
     ==========================================================*/
    private SharedPreferences preferences;
    private static final String PREF_NAME = "BeyondMarsSave";

    /*==========================================================
                      ANIMATION
     ==========================================================*/
    private ObjectAnimator rocketAnimator,marsAnimator;

    /*==========================================================
                     UTILITIES
     ==========================================================*/

    private final Random random = new Random();

    private final DecimalFormat formatter =
            new DecimalFormat("#,###");

    /*==========================================================
                    CONSTANTS
     ==========================================================*/

    private static final long GAME_UPDATE = 16;

    private static final long AUTO_INCOME_DELAY = 1000;

    private static final long AUTO_SAVE_DELAY = 30000;

    private static final double LEVEL_MULTIPLIER = 1.10;

    private static final int MAX_LEVEL = 1000;

    /*==========================================================
                      RUNNABLES
     ==========================================================*/
    private final Runnable gameLoop = new Runnable() {
        @Override
        public void run() {

            // Implement in next section

            gameHandler.postDelayed(this, GAME_UPDATE);
        }
    };

    private final Runnable autoIncomeLoop = new Runnable() {
        @Override
        public void run() {

            // Implement later

            autoIncomeHandler.postDelayed(
                    this,
                    AUTO_INCOME_DELAY
            );
        }
    };

    private final Runnable autoSaveLoop = new Runnable() {
        @Override
        public void run() {

            // Implement later

            saveHandler.postDelayed(
                    this,
                    AUTO_SAVE_DELAY
            );
        }
    };



    /*==========================================================
                    onCreate()
                 (NEXT PART)
     ==========================================================*/

}