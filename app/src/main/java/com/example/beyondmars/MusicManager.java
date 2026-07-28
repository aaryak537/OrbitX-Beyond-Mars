package com.example.beyondmars;

import android.content.Context;
import android.media.MediaPlayer;

public class MusicManager {

    private static MediaPlayer player;

    public static void start(Context context) {
        if (player == null) {
           // player = MediaPlayer.create(context, R.raw.background_music);
            player.setLooping(true);
            player.start();
        }
    }

    public static void stop() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }
}
