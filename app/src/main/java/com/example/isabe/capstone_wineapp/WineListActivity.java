package com.example.isabe.capstone_wineapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.isabe.capstone_wineapp.object.Wine;

import java.nio.BufferUnderflowException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by isabe on 7/15/2018.
 */

public class WineListActivity extends AppCompatActivity {
    private static final String LOG_TAG = WineListActivity.class.getSimpleName();
    private static final java.lang.String INDEX_LIST = "indexList";
    private static final java.lang.String INDEX_TOP = "indexTop";
    private static List<Wine> wineList = new ArrayList<>();
    @BindView(R.id.list_view)
    ListView wineListView;
    public static String searchInput;
    private WineAdapter wineAdapter;
    private Context context;
    public static final String WINE_SELECTION = "thisWine";
    @BindBool(R.bool.isTablet)
    boolean isTabletSize;

    private Wine mWine;
    private Parcelable state;
    private int index;
    private int top;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        context = this;

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        searchInput = intent.getStringExtra(MainActivity.SEARCH_ITEM);

        WineListFragment wineListFragment = (WineListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.frame_list_view);

        if (wineListFragment == null) {
            wineListFragment = WineListFragment.newInstance();

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.frame_list_view, wineListFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ((item.getItemId())) {
            case R.id.favorites:
                Intent showFavs = new Intent(this, WineFavoritesActivity.class);
                startActivity(showFavs);
                return true;
            //case R.id.delete_all:
            // deleteWine();
            //return true;
            default:
                Log.i(LOG_TAG, "Activity: Options reached.");
                return super.onOptionsItemSelected(item);
        }
    }
    }
