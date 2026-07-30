package com.example.beyondmars;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class WorkersActivity extends AppCompatActivity {

    private TextView txtWorkersOwned,txtWorkerIncome,txtWorkerPrice;

    private Button btnHireWorker;
    private ImageView btnBack;

    private SharedPreferences prefs;

    private int energy,workers,workerCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workers);

        txtWorkersOwned = findViewById(R.id.txtWorkersOwned);
        txtWorkerIncome = findViewById(R.id.txtWorkerIncome);
        txtWorkerPrice = findViewById(R.id.txtWorkerPrice);

        btnHireWorker = findViewById(R.id.btnHireWorker);
        btnBack = findViewById(R.id.btnBack);

        prefs = getSharedPreferences("BeyondMars", MODE_PRIVATE);

        energy = prefs.getInt("energy", 0);
        workers = prefs.getInt("workers", 0);
        workerCost = prefs.getInt("workerCost", 100);

        updateUI();

        btnBack.setOnClickListener(v -> finish());

        btnHireWorker.setOnClickListener(v -> {

            if (energy >= workerCost) {

                energy -= workerCost;
                workers++;

                // Increase price by 50 every purchase
                workerCost += 50;

                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("energy", energy);
                editor.putInt("workers", workers);
                editor.putInt("workerCost", workerCost);
                editor.apply();

                updateUI();

                Toast.makeText(this, "Worker Hired!",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Not enough Energy!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateUI() {
        txtWorkersOwned.setText(String.valueOf(workers));
        txtWorkerIncome.setText("+" + workers + " Energy / sec");
        txtWorkerPrice.setText("Cost: " + workerCost + " Energy");
    }
}