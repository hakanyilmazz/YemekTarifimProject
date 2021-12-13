package com.yemektarifim.backend.model;

import lombok.Getter;

import java.util.Arrays;

@Getter
public class LifeChainBlock {
    private String username;
    private int score;
    private int blockHash;
    private int previousBlockHash;

    public LifeChainBlock() {
    }

    public LifeChainBlock(String username, int score, int previousBlockHash) {
        this.username = username;
        this.score = score;
        this.previousBlockHash = previousBlockHash;
        this.blockHash = Arrays.hashCode(new int[]{this.username.hashCode(), Integer.hashCode(this.score), this.previousBlockHash});
    }

}
