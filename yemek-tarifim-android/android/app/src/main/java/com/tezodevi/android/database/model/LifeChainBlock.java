package com.tezodevi.android.database.model;

import com.google.gson.annotations.SerializedName;

public class LifeChainBlock {
    @SerializedName("username")
    public String username;

    @SerializedName("score")
    public int score;

    @SerializedName("blockHash")
    public int blockHash;

    @SerializedName("previousBlockHash")
    public int previousBlockHash;

    public LifeChainBlock(String username, int score) {
        this.username = username;
        this.score = score;
    }
}
