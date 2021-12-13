package com.tezodevi.android.model;

public class ScoreSingleton {
    private static ScoreSingleton instance;
    public int score = 0;

    private ScoreSingleton() {
    }

    public static ScoreSingleton getInstance() {
        if (instance == null) {
            instance = new ScoreSingleton();
        }

        return instance;
    }
}
