package com.example.isabe.capstone_wineapp;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.example.isabe.capstone_wineapp.database.AppDatabase;

/**
 * Created by isabe on 7/27/2018.
 */

public class AddWineViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase mAppDatabase;
    private final int mWineId;

    public AddWineViewModelFactory(AppDatabase appDatabase, int wineId){
        mAppDatabase = appDatabase;
        mWineId = wineId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new AddWineViewModel(mAppDatabase, mWineId);
    }
}
