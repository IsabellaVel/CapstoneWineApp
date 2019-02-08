package com.example.isabe.capstone_wineapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.isabe.capstone_wineapp.database.AppDatabase;
import com.example.isabe.capstone_wineapp.database.FavoriteEntry;

import java.util.List;

/**
 * Created by isabe on 7/27/2018.
 */

public class MainViewModel extends AndroidViewModel {
    private LiveData<List<FavoriteEntry>> favoriteWines;
    private static final String LOG_TAG = MainViewModel.class.getSimpleName();

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase appDatabase = AppDatabase.getInstance(this.getApplication());
        favoriteWines = appDatabase.wineDao().loadAllWines();
    }

    public LiveData<List<FavoriteEntry>> getFavoriteWines(){
        return favoriteWines;
    }
}
