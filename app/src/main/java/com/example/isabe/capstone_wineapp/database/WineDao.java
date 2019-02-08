package com.example.isabe.capstone_wineapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.isabe.capstone_wineapp.object.Wine;

import java.util.List;

/**
 * Created by isabe on 7/22/2018.
 */
@Dao
public interface WineDao {

    @Query("SELECT * FROM wines")
    LiveData<List<FavoriteEntry>> loadAllWines();

    @Insert
    void insertWine(FavoriteEntry favoriteEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateWine(FavoriteEntry favoriteEntry);

    @Delete
    void deleteWine(FavoriteEntry favoriteEntry);

    @Query("SELECT * FROM wines WHERE id = :id")
    LiveData<FavoriteEntry> loadWineById(int id);


}
