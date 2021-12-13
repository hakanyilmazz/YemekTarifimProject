package com.tezodevi.android.database.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LifeChainWeb {
    @SerializedName("lifechain")
    public List<LifeChainBlock> lifeChainBlockList;
}
