package com.example.beyondmars;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    Switch switchMusic, switchSound, switchVibration;
    TextView resetProgress, privacyPolicy, credits;
    SharedPreferences preferences;
    public static final String PREF_NAME = "BeyondMarsSettings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize Views
        switchMusic = findViewById(R.id.switchMusic);
        switchSound = findViewById(R.id.switchSound);
        switchVibration = findViewById(R.id.switchVibration);

        resetProgress = findViewById(R.id.resetProgress);
        privacyPolicy = findViewById(R.id.privacyPolicy);
        credits = findViewById(R.id.credits);

        // Shared Preferences
        preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        // Load Saved Values
        switchMusic.setChecked(preferences.getBoolean("music", true));
        switchSound.setChecked(preferences.getBoolean("sound", true));
        switchVibration.setChecked(preferences.getBoolean("vibration", true));

        SharedPreferences preferences =
                getSharedPreferences("BeyondMarsSettings", MODE_PRIVATE);

        boolean musicEnabled = preferences.getBoolean("music", true);

        if (musicEnabled) {
            // Play background music
            MusicManager.start(this);
        }
        boolean soundEnabled = preferences.getBoolean("sound", true);

        if (soundEnabled) {
            // Play button click sound
        }
        boolean vibrationEnabled = preferences.getBoolean("vibration", true);

        if (vibrationEnabled) {
            // Vibrate device
        }

        // Save Music
        switchMusic.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("music", isChecked).apply();
            Toast.makeText(this,
                    isChecked ? "Music Enabled" : "Music Disabled",
                    Toast.LENGTH_SHORT).show();
        });

        // Save Sound
        switchSound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("sound", isChecked).apply();
            Toast.makeText(this,
                    isChecked ? "Sound Enabled" : "Sound Disabled",
                    Toast.LENGTH_SHORT).show();
        });

        // Save Vibration
        switchVibration.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("vibration", isChecked).apply();
            Toast.makeText(this,
                    isChecked ? "Vibration Enabled" : "Vibration Disabled",
                    Toast.LENGTH_SHORT).show();
        });

        // Reset Progress
        resetProgress.setOnClickListener(v -> showResetDialog());

        // Privacy Policy
        privacyPolicy.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://docs.google.com/document/d/1YfYAve3_awNVdgtA5rWdlx71--rrXFW5ITHcmBDoaj4/edit?usp=sharing"));
            startActivity(intent);
        });

        // Credits
        credits.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Beyond Mars")
                    .setMessage(
                            "Game: Beyond Mars\n\n" +
                                    "Developed by Team OrbitX\n\n" +
                                    "Powered by Java + Android Studio\n\n" +
                                    "© 2026 Team OrbitX"
                    )
                    .setPositiveButton("OK", null)
                    .show();
        });
    }

    // Reset Game Progress
    private void showResetDialog() {

        new AlertDialog.Builder(this)
                .setTitle("Reset Progress")
                .setMessage("Are you sure you want to reset all game progress?")
                .setPositiveButton("RESET", (dialog, which) -> {

                    SharedPreferences.Editor editor = preferences.edit();

                    editor.clear();
                    editor.apply();

                    Toast.makeText(this,
                            "Progress Reset Successfully!",
                            Toast.LENGTH_LONG).show();
                    recreate();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}