package com.example.beyondmars;


public class Mission {


    private int id;

    private String title;
    private String description;


    private int progress;
    private int goal;


    private int rewardCoins;
    private int rewardGems;
    private int rewardXP;


    private boolean completed;
    private boolean claimed;



    public Mission(int id,
                   String title,
                   String description,
                   int goal,
                   int rewardCoins,
                   int rewardGems,
                   int rewardXP) {


        this.id = id;

        this.title = title;
        this.description = description;


        this.goal = goal;


        this.rewardCoins = rewardCoins;
        this.rewardGems = rewardGems;
        this.rewardXP = rewardXP;



        this.progress = 0;

        this.completed = false;

        this.claimed = false;

    }




    // Mission ID

    public int getId() {

        return id;

    }





    // Mission Title

    public String getTitle() {

        return title;

    }





    // Mission Description

    public String getDescription() {

        return description;

    }





    // Current Progress

    public int getProgress() {

        return progress;

    }





    // Adapter uses this

    public int getTarget() {

        return goal;

    }





    // Original name also kept

    public int getGoal() {

        return goal;

    }





    // Coin Reward

    public int getCoinReward() {

        return rewardCoins;

    }



    public int getRewardCoins() {

        return rewardCoins;

    }





    // Gem Reward

    public int getGemReward() {

        return rewardGems;

    }



    public int getRewardGems() {

        return rewardGems;

    }





    // XP Reward

    public int getRewardXP() {

        return rewardXP;

    }





    // Completed Status

    public boolean isCompleted() {

        return completed;

    }





    // Claimed Status

    public boolean isClaimed() {

        return claimed;

    }





    // Update Progress

    public void setProgress(int progress) {


        this.progress = progress;


        if(this.progress >= goal) {


            this.progress = goal;

            completed = true;

        }

    }






    // Increase Progress

    public void addProgress(int amount) {


        progress += amount;


        if(progress >= goal) {


            progress = goal;

            completed = true;

        }

    }






    // Claim Reward

    public void claimReward() {


        if(completed) {

            claimed = true;

        }

    }






    // Reset Mission

    public void resetProgress() {


        progress = 0;

        completed = false;

        claimed = false;


    }



}