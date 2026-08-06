package com.example.beyondmars;

import java.util.ArrayList;
import java.util.Random;

public class MissionManager {

    private static final ArrayList<Mission> dailyMissions = new ArrayList<>();
    private static final ArrayList<Mission> storyMissions = new ArrayList<>();

    private static final Random random = new Random();

    public static void initializeMissions() {

        if (!dailyMissions.isEmpty())
            return;

        generateDailyMissions();
        generateStoryMissions();
    }

    private static void generateDailyMissions() {

        dailyMissions.clear();

        dailyMissions.add(new Mission(
                1,
                "Collect Minerals",
                "Collect 500 Minerals",
                500,
                300,
                20,
                50));

        dailyMissions.add(new Mission(
                2,
                "Earn Coins",
                "Earn 1000 Coins",
                1000,
                500,
                30,
                80));

        dailyMissions.add(new Mission(
                3,
                "Tap Mars",
                "Tap Mars 100 Times",
                100,
                250,
                15,
                40));
    }

    private static void generateStoryMissions() {

        storyMissions.clear();

        storyMissions.add(new Mission(
                101,
                "First Colony",
                "Build Solar Panel",
                1,
                1000,
                50,
                150));

        storyMissions.add(new Mission(
                102,
                "Expand Base",
                "Upgrade HQ",
                1,
                1500,
                80,
                200));

        storyMissions.add(new Mission(
                103,
                "Reach Level 10",
                "Become Level 10",
                10,
                3000,
                150,
                500));
    }

    public static ArrayList<Mission> getDailyMissions() {
        return dailyMissions;
    }

    public static ArrayList<Mission> getStoryMissions() {
        return storyMissions;
    }

    public static Mission getMissionById(int id) {

        for (Mission mission : dailyMissions) {
            if (mission.getId() == id)
                return mission;
        }

        for (Mission mission : storyMissions) {
            if (mission.getId() == id)
                return mission;
        }

        return null;
    }

    public static void addProgress(int missionId, int amount) {

        Mission mission = getMissionById(missionId);

        if (mission != null && !mission.isClaimed()) {

            mission.addProgress(amount);

        }
    }

    public static void claimMission(int missionId) {

        Mission mission = getMissionById(missionId);

        if (mission != null && mission.isCompleted()) {

            mission.claimReward();

        }
    }

    public static int getCompletedMissionCount() {

        int count = 0;

        for (Mission mission : dailyMissions)
            if (mission.isClaimed())
                count++;

        for (Mission mission : storyMissions)
            if (mission.isClaimed())
                count++;

        return count;
    }

    public static void resetDailyMissions() {

        generateDailyMissions();

    }

    public static Mission getRandomDailyMission() {

        if (dailyMissions.isEmpty())
            return null;

        return dailyMissions.get(random.nextInt(dailyMissions.size()));

    }
}