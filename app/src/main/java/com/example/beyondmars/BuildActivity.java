package com.example.beyondmars;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class BuildActivity extends AppCompatActivity {

    private SharedPreferences preferences;

    private long coins;

    private int hqLevel;
    private int solarLevel;
    private int mineLevel;
    private int greenhouseLevel;
    private int oxygenPlantLevel;

    private int workers;

    private TextView txtCoins;

    private TextView txtHQLevel;
    private TextView txtSolarLevel;
    private TextView txtMineLevel;
    private TextView txtGreenhouseLevel;
    private TextView txtOxygenLevel;

    private TextView txtHQCost;
    private TextView txtSolarCost;
    private TextView txtMineCost;
    private TextView txtGreenhouseCost;
    private TextView txtOxygenCost;

    private Button btnUpgradeHQ;
    private Button btnUpgradeSolar;
    private Button btnUpgradeMine;
    private Button btnUpgradeGreenhouse;
    private Button btnUpgradeOxygen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_build);

        preferences = getSharedPreferences(
                "BeyondMarsSave",
                MODE_PRIVATE
        );

        initializeViews();

        loadGame();

        updateUI();

        setupButtons();
    }


    // =========================================================
    // VIEWS
    // =========================================================

    private void initializeViews() {

        txtCoins =
                findViewById(R.id.txtCoins);

        txtHQLevel =
                findViewById(R.id.txtHQLevel);

        txtSolarLevel =
                findViewById(R.id.txtSolarLevel);

        txtMineLevel =
                findViewById(R.id.txtMineLevel);

        txtGreenhouseLevel =
                findViewById(R.id.txtGreenhouseLevel);

        txtOxygenLevel =
                findViewById(R.id.txtOxygenLevel);


        txtHQCost =
                findViewById(R.id.txtHQCost);

        txtSolarCost =
                findViewById(R.id.txtSolarCost);

        txtMineCost =
                findViewById(R.id.txtMineCost);

        txtGreenhouseCost =
                findViewById(R.id.txtGreenhouseCost);

        txtOxygenCost =
                findViewById(R.id.txtOxygenCost);


        btnUpgradeHQ =
                findViewById(R.id.btnUpgradeHQ);

        btnUpgradeSolar =
                findViewById(R.id.btnUpgradeSolar);

        btnUpgradeMine =
                findViewById(R.id.btnUpgradeMine);

        btnUpgradeGreenhouse =
                findViewById(R.id.btnUpgradeGreenhouse);

        btnUpgradeOxygen =
                findViewById(R.id.btnUpgradeOxygen);
    }


    // =========================================================
    // LOAD
    // =========================================================

    private void loadGame() {

        coins =
                preferences.getLong(
                        "coins",
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

        workers =
                preferences.getInt(
                        "workers",
                        0
                );
    }


    // =========================================================
    // BUTTONS
    // =========================================================

    private void setupButtons() {

        btnUpgradeHQ.setOnClickListener(
                v -> upgradeHQ()
        );

        btnUpgradeSolar.setOnClickListener(
                v -> upgradeSolar()
        );

        btnUpgradeMine.setOnClickListener(
                v -> upgradeMine()
        );

        btnUpgradeGreenhouse.setOnClickListener(
                v -> upgradeGreenhouse()
        );

        btnUpgradeOxygen.setOnClickListener(
                v -> upgradeOxygen()
        );
    }


    // =========================================================
    // HQ
    // =========================================================

    private void upgradeHQ() {

        int cost =
                (hqLevel + 1) * 500;

        if (!spendCoins(cost))
            return;

        hqLevel++;

        saveGame();

        showMessage(
                "🏠 HQ upgraded to Lv." +
                        hqLevel
        );

        updateUI();
    }


    // =========================================================
    // SOLAR
    // =========================================================

    private void upgradeSolar() {

        int cost =
                (solarLevel + 1) * 300;

        if (!spendCoins(cost))
            return;

        solarLevel++;

        saveGame();

        showMessage(
                "☀️ Solar upgraded to Lv." +
                        solarLevel
        );

        updateUI();
    }


    // =========================================================
    // MINE
    // =========================================================

    private void upgradeMine() {

        int cost =
                (mineLevel + 1) * 400;

        if (!spendCoins(cost))
            return;

        mineLevel++;

        saveGame();

        showMessage(
                "⛏ Mine upgraded to Lv." +
                        mineLevel
        );

        updateUI();
    }


    // =========================================================
    // GREENHOUSE
    // =========================================================

    private void upgradeGreenhouse() {

        int cost =
                (greenhouseLevel + 1) * 600;

        if (!spendCoins(cost))
            return;

        greenhouseLevel++;

        workers++;

        saveGame();

        showMessage(
                "🌱 Greenhouse upgraded!\n" +
                        "+1 Worker"
        );

        updateUI();
    }


    // =========================================================
    // OXYGEN
    // =========================================================

    private void upgradeOxygen() {

        int cost =
                (oxygenPlantLevel + 1) * 800;

        if (!spendCoins(cost))
            return;

        oxygenPlantLevel++;

        saveGame();

        showMessage(
                "🫁 Oxygen Plant upgraded!"
        );

        updateUI();
    }


    // =========================================================
    // SPEND COINS
    // =========================================================

    private boolean spendCoins(int amount) {

        if (coins < amount) {

            showMessage(
                    "Not enough Coins"
            );

            return false;
        }

        coins -= amount;

        return true;
    }


    // =========================================================
    // SAVE
    // =========================================================

    private void saveGame() {

        preferences.edit()

                .putLong(
                        "coins",
                        coins
                )

                .putInt(
                        "hqLevel",
                        hqLevel
                )

                .putInt(
                        "solarLevel",
                        solarLevel
                )

                .putInt(
                        "mineLevel",
                        mineLevel
                )

                .putInt(
                        "greenhouseLevel",
                        greenhouseLevel
                )

                .putInt(
                        "oxygenPlantLevel",
                        oxygenPlantLevel
                )

                .putInt(
                        "workers",
                        workers
                ).apply();
    }


    // =========================================================
    // UI
    // =========================================================

    private void updateUI() {

        txtCoins.setText(
                String.format(
                        Locale.getDefault(),
                        "%,d",
                        coins
                )
        );


        txtHQLevel.setText(
                "Lv. " + hqLevel
        );

        txtSolarLevel.setText(
                "Lv. " + solarLevel
        );

        txtMineLevel.setText(
                "Lv. " + mineLevel
        );

        txtGreenhouseLevel.setText(
                "Lv. " + greenhouseLevel
        );

        txtOxygenLevel.setText(
                "Lv. " + oxygenPlantLevel
        );


        txtHQCost.setText(
                "Cost: " +
                        ((hqLevel + 1) * 500) +
                        " Coins"
        );

        txtSolarCost.setText(
                "Cost: " +
                        ((solarLevel + 1) * 300) +
                        " Coins"
        );

        txtMineCost.setText(
                "Cost: " +
                        ((mineLevel + 1) * 400) +
                        " Coins"
        );

        txtGreenhouseCost.setText(
                "Cost: " +
                        ((greenhouseLevel + 1) * 600) +
                        " Coins"
        );

        txtOxygenCost.setText(
                "Cost: " +
                        ((oxygenPlantLevel + 1) * 800) +
                        " Coins"
        );
    }


    private void showMessage(String message) {

        Toast.makeText(
                this,
                message,
                Toast.LENGTH_SHORT
        ).show();
    }
}