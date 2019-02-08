package com.example.isabe.capstone_wineapp;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.isabe.capstone_wineapp.object.Wine;
import com.example.isabe.capstone_wineapp.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.example.isabe.capstone_wineapp.WineListActivity.searchInput;
import static com.example.isabe.capstone_wineapp.WineListFragment.API_KEY_QUERY;
import static com.example.isabe.capstone_wineapp.WineListFragment.API_NO_RESULTS;
import static com.example.isabe.capstone_wineapp.WineListFragment.API_QUERY;
import static com.example.isabe.capstone_wineapp.WineListFragment.WINE_JSON_URI;
import static com.example.isabe.capstone_wineapp.WineListFragment.apiKey;
import static com.example.isabe.capstone_wineapp.utilities.NetworkUtils.isOnline;

/**
 * Created by isabe on 7/30/2018.
 */

public class WineViewModel extends ViewModel {
    private static final String LOG_TAG = WineViewModel.class.getSimpleName();
    public List<Wine> winesList;
    private Context mContext;

    public MutableLiveData<List<Wine>> wineData;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public LiveData<List<Wine>> getWines() {
        if (wineData == null) {
            wineData = new MutableLiveData<List<Wine>>();
            loadWines();
            }
        return wineData;
    }

    public void select(List<Wine> winesList){
        wineData.setValue(winesList);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void loadWines() {
        // Do an asynchronous operation to fetch wines.
            new AsyncTask<Void,Void,List<Wine>>() {
                @Override
                protected List<Wine> doInBackground(Void... voids) {
                    List<Wine> wines = new ArrayList<>();

                    Uri wineUri = Uri.parse(WINE_JSON_URI).buildUpon()
                            .appendQueryParameter(API_KEY_QUERY, apiKey)
                            .appendQueryParameter(API_QUERY, searchInput)
                            .appendQueryParameter(API_NO_RESULTS, "30")
                            .build();

                    URL wineUrl = NetworkUtils.createUrl((wineUri).toString());

                    if ((wineUrl).toString() == null) {
                        return null;
                    }
                    wines = NetworkUtils.fetchWineData((wineUrl).toString());
                    Log.i(LOG_TAG, "Url is " + (wineUrl).toString());

                    return wines;
                }

                @Override
                protected void onPostExecute(List<Wine> wines) {
                    if(wines != null){
                    wineData.setValue(wines);
                }else {
                        return;
                    }}
                }.execute();
        }
    }
