package com.example.isabe.capstone_wineapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.example.isabe.capstone_wineapp.database.AppDatabase;
import com.example.isabe.capstone_wineapp.database.FavoriteEntry;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import android.arch.lifecycle.Observer;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by isabe on 7/22/2018.
 */

public class WineFavoritesActivity extends AppCompatActivity implements FavoritesAdapter.ItemClickListener {
    private static final String LOG_TAG = WineFavoritesActivity.class.getSimpleName();
    private WineAdapter mWineAdapter;
    private List<FavoriteEntry> favoriteWines = new ArrayList<>();
    private AppDatabase mAppDatabase;
    @BindView(R.id.recyclerViewWines)
    RecyclerView mRecyclerView;
    private FavoritesAdapter mFavoritesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wine_favorites);
        ButterKnife.bind(this);

        mAppDatabase = AppDatabase.getInstance(this);
        setupRecycler(mRecyclerView);
        setupViewModel();

    }

    public void setupRecycler(RecyclerView recyclerView) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mFavoritesAdapter = new FavoritesAdapter(this, this);
        mRecyclerView.setAdapter(mFavoritesAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(decoration);
        Log.i(LOG_TAG, "Recycler View Adapter is reached.");
        deleteWine();
    }


    public void deleteWine() {

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<FavoriteEntry> wineEntries = mFavoritesAdapter.getFavWines();
                        mAppDatabase.wineDao().deleteWine(wineEntries.get(position));

                    }
                });
            }
        }).attachToRecyclerView(mRecyclerView);
    }

    private void retrieveWines() {
                final LiveData<List<FavoriteEntry>> wines = mAppDatabase.wineDao().loadAllWines();
        Log.i(LOG_TAG, "Wines are retrieved.");
        wines.observe(this, new Observer<List<FavoriteEntry>>(){

            @Override
            public void onChanged(@Nullable List<FavoriteEntry> favoriteEntries) {
                Log.d(LOG_TAG, "Receiving database update from LiveData.");
            mFavoritesAdapter.setFavWines(favoriteEntries);
            }
        });
    }

    @Override
    public void onItemClickListener(int itemId) {
     }

     private void setupViewModel(){
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getFavoriteWines().observe(this, new Observer<List<FavoriteEntry>>() {
            @Override
            public void onChanged(@Nullable List<FavoriteEntry> favoriteEntries) {
                Log.d(LOG_TAG, "Receiving database update from LiveData in ViewModel.");
                mFavoritesAdapter.setFavWines(favoriteEntries);
            }
        });
     }
}
