package com.example.beyondmars;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BuildActivity extends AppCompatActivity {

    private ImageView btnBack;

    private Button btnBuildSolar;
    private Button btnBuildOxygen;
    private Button btnBuildHabitat;
    private Button btnBuildLab;

    // Building Costs
    private static final int SOLAR_COST = 500;
    private static final int OXYGEN_COST = 1000;
    private static final int HABITAT_COST = 2000;
    private static final int LAB_COST = 5000;

    private int coins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build);

        btnBack = findViewById(R.id.btnBack);

        btnBuildSolar = findViewById(R.id.btnBuildSolar);
        btnBuildOxygen = findViewById(R.id.btnBuildOxygen);
        btnBuildHabitat = findViewById(R.id.btnBuildHabitat);
        btnBuildLab = findViewById(R.id.btnBuildLab);

        // Receive current coins
        coins = getIntent().getIntExtra("coins", 0);

        btnBack.setOnClickListener(v -> finishBuild());

        btnBuildSolar.setOnClickListener(v ->
                buildStructure(SOLAR_COST, "Solar Panel"));

        btnBuildOxygen.setOnClickListener(v ->
                buildStructure(OXYGEN_COST, "Oxygen Plant"));

        btnBuildHabitat.setOnClickListener(v ->
                buildStructure(HABITAT_COST, "Habitat Dome"));

        btnBuildLab.setOnClickListener(v ->
                buildStructure(LAB_COST, "Research Lab"));
    }

    private void buildStructure(int cost, String building) {

        if (coins >= cost) {

            coins -= cost;

            Toast.makeText(
                    this,
                    building + " Built Successfully!",
                    Toast.LENGTH_SHORT
            ).show();

            // TODO:
            // Increase Energy
            // Increase Population
            // Unlock Technologies
            // Save Building Progress
            // Update Main Game

        } else {

            Toast.makeText(
                    this,
                    "Not enough coins!",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void finishBuild() {

        Intent result = new Intent();
        result.putExtra("coins", coins);

        setResult(RESULT_OK, result);
        finish();
    }

    @Override
    public void onBackPressed() {
        finishBuild();
    }
}
