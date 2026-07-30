package com.example.beyondmars;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ShopActivity extends AppCompatActivity {

    private ImageView btnBack;
    private Button btnBuyWorker, btnBuyDrill, btnBuyRover;

    // Prices
    private final int WORKER_COST = 200;
    private final int DRILL_COST = 500;
    private final int ROVER_COST = 1000;

    // Current player coins
    private int coins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        btnBack = findViewById(R.id.btnBack);
        btnBuyWorker = findViewById(R.id.btnBuyWorker);
        btnBuyDrill = findViewById(R.id.btnBuyDrill);
        btnBuyRover = findViewById(R.id.btnBuyRover);

        // Receive coins from GameActivity
        coins = getIntent().getIntExtra("coins", 0);

        btnBack.setOnClickListener(v -> finishShop());

        btnBuyWorker.setOnClickListener(v -> buyItem(WORKER_COST, "Worker"));

        btnBuyDrill.setOnClickListener(v -> buyItem(DRILL_COST, "Mining Drill"));

        btnBuyRover.setOnClickListener(v -> buyItem(ROVER_COST, "Mars Rover"));
    }

    private void buyItem(int cost, String itemName) {

        if (coins >= cost) {

            coins -= cost;

            Toast.makeText(
                    this,
                    itemName + " Purchased!",
                    Toast.LENGTH_SHORT
            ).show();

            // TODO:
            // Increase income/sec
            // Save purchase in SharedPreferences
            // Unlock upgrades

        } else {

            Toast.makeText(
                    this,
                    "Not enough coins!",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void finishShop() {

        Intent result = new Intent();
        result.putExtra("coins", coins);

        setResult(RESULT_OK, result);
        finish();
    }
    @Override
    public void onBackPressed() {
        finishShop();
    }
}
