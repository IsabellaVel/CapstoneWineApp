package com.example.isabe.capstone_wineapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.example.isabe.capstone_wineapp.database.AppDatabase;
import com.example.isabe.capstone_wineapp.database.FavoriteEntry;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.example.isabe.capstone_wineapp.WineDetailsFragment;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.button_search)
    Button mSearchButton;

    @BindView(R.id.edit_text)
    EditText mEditTextSearch;
    String inputEditText = "merlot";
    public static final String SEARCH_ITEM = "searchItem";

    private InterstitialAd mInterstitialAd;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private AppDatabase mAppDatabase;
    private WineAdapter mWineAdapter;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mAppDatabase = AppDatabase.getInstance(this);

    }



    @OnClick(R.id.button_search)
    public void searchWines(View view) {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d(LOG_TAG, "The interstitial wasn`t loaded yet.");
        }

        Intent showListOfRetrievedWines = new Intent(this, WineListActivity.class);
        inputEditText = mEditTextSearch.getText().toString();
        String adjustedInput = inputEditText.replaceAll("\\s+", "");

        showListOfRetrievedWines.putExtra(SEARCH_ITEM, adjustedInput);
        startActivity(showListOfRetrievedWines);

        Log.i(LOG_TAG, "Button is clicked.");
    }


}
