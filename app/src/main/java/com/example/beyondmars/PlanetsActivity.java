package com.example.beyondmars;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlanetsActivity extends AppCompatActivity {

    private RecyclerView recyclerPlanets;

    private ArrayList<PlanetModel> planetList;
    private PlanetAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planets);

        hideSystemUI();

        recyclerPlanets = findViewById(R.id.recyclerPlanets);


        setupRecyclerView();

        loadPlanets();

    }
    private void setupRecyclerView() {

        LinearLayoutManager manager =
                new LinearLayoutManager(
                        this,
                        LinearLayoutManager.HORIZONTAL,
                        false);

        recyclerPlanets.setLayoutManager(manager);

        new LinearSnapHelper().attachToRecyclerView(recyclerPlanets);

    }

    private void loadPlanets() {

        planetList = new ArrayList<>();

        planetList.add(new PlanetModel(
                "Mars",
                "Unlocked",
                R.drawable.mars,
                false));

        planetList.add(new PlanetModel(
                "Moon",
                "Unlock Level 10",
                R.drawable.moon,
                true));

        planetList.add(new PlanetModel(
                "Venus",
                "Unlock Level 20",
                R.drawable.venus,
                true));

        planetList.add(new PlanetModel(
                "Mercury",
                "Unlock Level 30",
                R.drawable.mercury,
                true));

        planetList.add(new PlanetModel(
                "Jupiter",
                "Unlock Level 40",
                R.drawable.jupiter,
                true));

        planetList.add(new PlanetModel(
                "Saturn",
                "Unlock Level 60",
                R.drawable.saturn,
                true));

        planetList.add(new PlanetModel(
                "Uranus",
                "Unlock Level 80",
                R.drawable.uranus,
                true));

        planetList.add(new PlanetModel(
                "Neptune",
                "Unlock Level 100",
                R.drawable.neptune,
                true));

        adapter = new PlanetAdapter(this, planetList);

        recyclerPlanets.setAdapter(adapter);

    }
    private void hideSystemUI() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {

            getWindow().setDecorFitsSystemWindows(false);

            WindowInsetsController controller =
                    getWindow().getInsetsController();

            if (controller != null) {

                controller.hide(WindowInsets.Type.statusBars()
                        | WindowInsets.Type.navigationBars());
                controller.setSystemBarsBehavior(
                        WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI();
    }
}