package com.example.isabe.capstone_wineapp;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.isabe.capstone_wineapp.object.Wine;
import com.example.isabe.capstone_wineapp.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.isabe.capstone_wineapp.WineListActivity.searchInput;
import static com.example.isabe.capstone_wineapp.utilities.NetworkUtils.isOnline;

/**
 * Created by isabe on 7/15/2018.
 */

public class WineListFragment extends Fragment {

    private static final String LOG_TAG = WineListFragment.class.getSimpleName();
    public static final String WINE_SELECTION = "thisWine";
    private static final String INDEX_LIST = "indexList";
    private static final String INDEX_TOP = "indexTop";

    @BindView(R.id.list_view)
    ListView wineListView;
    public MutableLiveData<List<Wine>> wineData;

    private WineAdapter wineAdapter;

    private List<Wine> wineList;
    private final String mUrl = "";
    private Unbinder unbinder;
    public static final String WINE_JSON_URI = BuildConfig.API_URI;
    public static final String apiKey = BuildConfig.API_KEY;
    public final static String API_KEY_QUERY = "akey";
    public final static String API_QUERY = "q";
    public final static String API_NO_RESULTS = "n";
    public String resultsNumber = "30";
    private Context mContext;
    private Parcelable state;
    private int index;
    private int top;

    public WineListFragment() {
    }

    public static WineListFragment newInstance() {
        return new WineListFragment();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_list_wines, container, false);
        unbinder = ButterKnife.bind(this, view);

        WineViewModel wineViewModel = ViewModelProviders.of(this).get(WineViewModel.class);
        wineViewModel.getWines().observe(this, wineData -> {

            wineAdapter = new WineAdapter(getContext(), 0, wineData);
            if(wineListView == null){
                Toast.makeText(mContext, "Check your network connection.", Toast.LENGTH_LONG).show();
            }
            wineListView.setAdapter(wineAdapter);
            setWineAdapterItemClickListener();

            //Set the list of wines to the wineData ViewModel, in order to work with its size
            List<Wine> wineList = wineData;
            NetworkUtils.isOnline();

            if (wineList == null && !isOnline) {
                Toast.makeText(getActivity(), "Check your internet connection", Toast.LENGTH_LONG).show();
            } else if (wineList != null && !wineList.isEmpty()) {
                Log.i(LOG_TAG, "Wine adapter is reached.");

                String results = "Items found - " + wineList.size();
                Toast.makeText(getContext(), results, Toast.LENGTH_LONG).show();
            } else if (wineList.isEmpty()) {

                String results = "Items found - " + wineList.size();
                Toast.makeText(getContext(), results, Toast.LENGTH_LONG).show();
            }

        });

        //new GetWinesData().execute();
        if (savedInstanceState != null) {

           // state = savedInstanceState.getParcelable(INDEX_LIST);
            //wineListView.onRestoreInstanceState(state);

            wineListView.post(new Runnable() {
                @Override
                public void run() {
                    wineListView.setSelection(savedInstanceState.getInt("position"));
                    Log.i(LOG_TAG, "Restored position of listView no." + savedInstanceState.getInt("position"));
                }
            });
          }

        return view;
    }

      public class GetWinesData extends AsyncTask<List<Wine>, Void, List<Wine>> {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        @SafeVarargs
        protected final List<Wine> doInBackground(List<Wine>... lists) {

            Uri wineUri = Uri.parse(WINE_JSON_URI).buildUpon()
                    .appendQueryParameter(API_KEY_QUERY, apiKey)
                    .appendQueryParameter(API_QUERY, searchInput)
                    .appendQueryParameter(API_NO_RESULTS, resultsNumber)
                    .build();

            URL wineUrl = NetworkUtils.createUrl((wineUri).toString());

            if ((wineUrl).toString() == null) {
                return null;
            }
            wineList = NetworkUtils.fetchWineData((wineUrl).toString());
            Log.i(LOG_TAG, "Url is " + (wineUrl).toString());
            return wineList;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(List<Wine> wineList) {
            super.onPostExecute(wineList);
            wineAdapter = new WineAdapter(getContext(), 0, new ArrayList<Wine>());
            wineListView.setAdapter(wineAdapter);
            setWineAdapterItemClickListener();

            NetworkUtils.isOnline();

            if (wineList == null && !isOnline) {
                Toast.makeText(getActivity(), "Check your internet connection", Toast.LENGTH_LONG).show();
            } else if (wineList != null && !wineList.isEmpty()) {
                wineAdapter.addAll(wineList);
                Log.i(LOG_TAG, "Wine adapter is reached.");


                String results = "Items found - " + wineList.size();
                Toast.makeText(getContext(), results, Toast.LENGTH_LONG).show();
            } else if (wineList.isEmpty()) {

                String results = "Items found - " + wineList.size();
                Toast.makeText(getContext(), results, Toast.LENGTH_LONG).show();
            }
        }
    }


    public void setWineAdapterItemClickListener() {
        //set onItemClickListener
        wineListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Wine wine = (Wine) wineListView.getItemAtPosition(position);
                assert wine != null;

                String thisWineName = wine.getmWineName();
                String thisWineCode = wine.getmCode();
                String thisWineVintage = wine.getmWineVintage();
                String thisWineRegion = wine.getmWineRegion();
                String thisWineVarietal = wine.getmWineVarietal();
                String thisWineImage = wine.getmWineImage();

                Intent detailsIntent = new Intent(getActivity(), WineDetailsActivity.class);
                detailsIntent.putExtra(WINE_SELECTION, wine);
                startActivity(detailsIntent);
            }
        });

        if (wineList != null && !wineList.isEmpty()) {
            wineAdapter.addAll(wineList);
        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        Log.i(LOG_TAG, "Options reached.");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ((item.getItemId())) {
            case R.id.favorites:
                Intent showFavs = new Intent(getActivity(), WineFavoritesActivity.class);
                startActivity(showFavs);
                return true;
            //case R.id.delete_all:
            // deleteWine();
            //  return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {

        //state = wineListView.onSaveInstanceState();
        index = wineListView.getFirstVisiblePosition();
        Log.i(LOG_TAG, "Saved scroll position of ListView no." + index);

        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        index = wineListView.getFirstVisiblePosition();
       // savedInstanceState.putParcelable(INDEX_LIST, wineListView.onSaveInstanceState());
        savedInstanceState.putInt("position", index);
        Log.i(LOG_TAG, "InstanceState saved index position is " + index);
    }


}
