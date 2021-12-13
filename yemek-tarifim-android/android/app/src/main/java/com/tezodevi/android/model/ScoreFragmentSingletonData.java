package com.tezodevi.android.model;

public class ScoreFragmentSingletonData {
    private static ScoreFragmentSingletonData instance;
    public String data = "old";

    private ScoreFragmentSingletonData() {
    }

    public static ScoreFragmentSingletonData getInstance() {
        if (instance == null) {
            instance = new ScoreFragmentSingletonData();
        }

        return instance;
    }
}
