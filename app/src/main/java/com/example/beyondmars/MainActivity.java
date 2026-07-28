package com.example.beyondmars;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnPlay, btnContinue, btnHow;
    ImageButton btnSettings, btnExit;
    TextView txtCoins;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        hideSystemUI();
        preferences = getSharedPreferences("BeyondMars", MODE_PRIVATE);

        btnPlay = findViewById(R.id.btnPlay);
        btnContinue = findViewById(R.id.btnContinue);
        btnHow = findViewById(R.id.btnHow);
        btnSettings = findViewById(R.id.btnSettings);
        btnExit = findViewById(R.id.btnExit);
        txtCoins = findViewById(R.id.txtCoins);

        loadCoins();
        // PLAY
        btnPlay.setOnClickListener(v -> {
           Intent intent = new Intent(MainActivity.this,
                   PlanetsActivity.class);
            startActivity(intent);
        });
        // CONTINUE
        btnContinue.setOnClickListener(v -> {
            boolean savedGame = preferences.getBoolean("SAVE_GAME", false);

            if(savedGame){
               Intent intent = new Intent(MainActivity.this,
                       GameActivity.class);
                intent.putExtra("CONTINUE", true);
               startActivity(intent);
            }else{
                showMessage("No saved game found.");
            }
        });
        // HOW TO PLAY
       // btnHow.setOnClickListener(v -> {

          //  Intent intent = new Intent(MainActivity.this,
            //        HowToPlayActivity.class);

         //   startActivity(intent);
       // });
        // SETTINGS
        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,
                    SettingsActivity.class);
            startActivity(intent);
        });
        // EXIT
        btnExit.setOnClickListener(v -> showExitDialog());
    }
    private void loadCoins(){

        int coins = preferences.getInt("COINS", 0);

        txtCoins.setText(String.valueOf(coins));
    }
    private void showMessage(String message){

        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
    private void showExitDialog(){

        new AlertDialog.Builder(this)
                .setTitle("Leaving Mars?")
                .setMessage("Mission Control is waiting. Are you sure you want to leave your colony?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, which) -> {
                    finishAffinity();
                })
                .setNegativeButton("No", null)
                .show();
    }
    private void hideSystemUI(){

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            getWindow().setDecorFitsSystemWindows(false);
            WindowInsetsController controller =
                    getWindow().getInsetsController();

            if(controller != null){
                controller.hide(WindowInsets.Type.statusBars()
                        | WindowInsets.Type.navigationBars());

                controller.setSystemBarsBehavior(
                        WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
        }
    }
}