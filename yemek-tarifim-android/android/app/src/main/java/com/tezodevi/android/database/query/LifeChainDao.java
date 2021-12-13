package com.tezodevi.android.database.query;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.tezodevi.android.database.model.LifeChain;

import java.util.List;

@Dao
public interface LifeChainDao {
    @Query("SELECT * FROM lifechain")
    List<LifeChain> getAll();

    @Insert
    void insertAll(List<LifeChain> lifeChains);

    @Delete
    void delete(List<LifeChain> lifeChains);
}
