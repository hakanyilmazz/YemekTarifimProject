package com.tezodevi.android.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class LifeChain {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "score")
    public int score;

    @ColumnInfo(name = "blockHash")
    public int blockHash;

    @ColumnInfo(name = "previousBlockHash")
    public int previousBlockHash;
}
