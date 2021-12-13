package com.tezodevi.android.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.tezodevi.android.database.model.LifeChain;
import com.tezodevi.android.database.query.LifeChainDao;

@Database(entities = {LifeChain.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract LifeChainDao lifeChainDao();

}
