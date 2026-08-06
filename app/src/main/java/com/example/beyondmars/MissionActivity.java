package com.example.beyondmars;


import android.os.Bundle;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;



public class MissionActivity extends AppCompatActivity {


    private RecyclerView rvDaily;
    private RecyclerView rvStory;


    private MissionAdapter dailyAdapter;
    private MissionAdapter storyAdapter;


    private ArrayList<Mission> dailyMissions;
    private ArrayList<Mission> storyMissions;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mission);



        rvDaily = findViewById(
                R.id.rvDailyMissions
        );


        rvStory = findViewById(
                R.id.rvStoryMissions
        );



        // Load missions

        MissionManager.initializeMissions();


        dailyMissions =
                MissionManager.getDailyMissions();


        storyMissions =
                MissionManager.getStoryMissions();




        // RecyclerView setup

        rvDaily.setLayoutManager(
                new LinearLayoutManager(this)
        );


        rvStory.setLayoutManager(
                new LinearLayoutManager(this)
        );






        // Daily Mission Adapter

        dailyAdapter = new MissionAdapter(
                this,
                dailyMissions,
                new MissionAdapter.OnMissionClickListener() {


                    @Override
                    public void onClaimClick(Mission mission, int position) {

                        claimMission(
                                mission
                        );


                        dailyAdapter.notifyItemChanged(
                                position
                        );

                    }

                }
        );






        // Story Mission Adapter

        storyAdapter = new MissionAdapter(
                this,
                storyMissions,
                new MissionAdapter.OnMissionClickListener() {


                    @Override
                    public void onClaimClick(Mission mission, int position) {


                        claimMission(
                                mission
                        );


                        storyAdapter.notifyItemChanged(
                                position
                        );


                    }

                }
        );






        rvDaily.setAdapter(
                dailyAdapter
        );


        rvStory.setAdapter(
                storyAdapter
        );



    }








    private void claimMission(Mission mission) {



        if(!mission.isCompleted()) {


            Toast.makeText(
                    this,
                    "Mission not completed yet!",
                    Toast.LENGTH_SHORT
            ).show();


            return;

        }




        if(mission.isClaimed()) {


            Toast.makeText(
                    this,
                    "Reward already claimed!",
                    Toast.LENGTH_SHORT
            ).show();


            return;

        }





        // Claim reward

        mission.claimReward();




        Toast.makeText(
                this,

                "+" + mission.getCoinReward()
                        + " Coins\n"
                        +
                        "+" + mission.getGemReward()
                        + " Gems",

                Toast.LENGTH_LONG

        ).show();



    }



}