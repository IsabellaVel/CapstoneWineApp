package com.example.isabe.capstone_wineapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.isabe.capstone_wineapp.database.AppDatabase;
import com.example.isabe.capstone_wineapp.database.FavoriteEntry;

import java.util.List;

/**
 * Created by isabe on 7/27/2018.
 */

public class AddWineViewModel extends ViewModel {

    private LiveData<FavoriteEntry> favoriteWine;

    public AddWineViewModel(AppDatabase appDatabase, int wineId){
        favoriteWine = appDatabase.wineDao().loadWineById(wineId);
    }

    public LiveData<FavoriteEntry> getFavoriteWine(){
        return favoriteWine;
    }
}
