package com.example.isabe.capstone_wineapp;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.example.isabe.capstone_wineapp.object.Wine;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.isabe.capstone_wineapp.widget.WineWidgetProvider.WINE_CODE;

/**
 * Created by isabe on 7/15/2018.
 */

public class WineDetailsActivity extends AppCompatActivity {
    private static final String WINE_ID_EXTRA = "wine_id";
    @BindBool(R.bool.isTablet)
    boolean mTwoPane;
    private Wine mWinePOJO;
    Context mContext;
    //@BindView(R.id.details_toolbar)
    android.support.v7.widget.Toolbar mToolbar;

    public void sendWineData(Context context, Parcelable wineObject) {
        context = getBaseContext();
        Bundle bundle = new Bundle();
        bundle.putParcelable(WineListFragment.WINE_SELECTION, mWinePOJO);
        WineDetailsFragment wineDetailsFragment = new WineDetailsFragment();
        wineDetailsFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_details, wineDetailsFragment)
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wine_details);

        android.support.v7.widget.Toolbar mToolbar = findViewById(R.id.details_toolbar);
        setSupportActionBar(mToolbar);
       // ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState != null) {
            mWinePOJO = savedInstanceState.getParcelable(WineListFragment.WINE_SELECTION);
        } else {
            mWinePOJO = getIntent().getParcelableExtra(WineListFragment.WINE_SELECTION);
            String code = mWinePOJO.getmCode();
            String wineName = mWinePOJO.getmWineName();
            String wineRegion = mWinePOJO.getmWineRegion();
            String wineVarietal = mWinePOJO.getmWineVarietal();
            Double winePrice = mWinePOJO.getmWinePrice();
            String wineVIntage = mWinePOJO.getmWineVintage();
            String wineImage = mWinePOJO.getmWineImage();

            sendWineData(getBaseContext(), mWinePOJO);
        }
    }

   }
