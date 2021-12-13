package com.tezodevi.android.database.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LifeChainRequest {
    @SerializedName("lifeChainBlock")
    public LifeChainBlock lifeChainBlock;

    @SerializedName("lifeChainBlockList")
    public List<LifeChainBlock> lifeChainBlockList;

    public LifeChainRequest(LifeChainBlock lifeChainBlock, List<LifeChainBlock> lifeChainBlockList) {
        this.lifeChainBlock = lifeChainBlock;
        this.lifeChainBlockList = lifeChainBlockList;
    }
}
