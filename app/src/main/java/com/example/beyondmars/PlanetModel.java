package com.example.beyondmars;

public class PlanetModel {

    private final String name,status;
    private final int imageResId;
    private final boolean locked;

    public PlanetModel(String name, String status, int imageResId, boolean locked) {
        this.name = name;
        this.status = status;
        this.imageResId = imageResId;
        this.locked = locked;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public int getImageResId() {
        return imageResId;
    }

    public boolean isLocked() {
        return locked;
    }
}