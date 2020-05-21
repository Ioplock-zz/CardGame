package com.example.cardgame.auxiliaryClasses;

import androidx.annotation.NonNull;

import java.util.HashMap;

public class Room {

    HashMap<String, String> players;
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%s - ID", this.id);
    }
}
