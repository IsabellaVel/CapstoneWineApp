package com.example.isabe.capstone_wineapp.utilities;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

import com.example.isabe.capstone_wineapp.object.Wine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by isabe on 7/14/2018.
 */

public class WineJSONUtils {
    private static final String WINE_CODE = "code";
    private static final String WINE_NAME = "name";
    private static final String WINE_REGION = "region";
    private static final String WINE_VINTAGE = "vintage";
    private static final String WINE_VARIETAL = "varietal";
    private static final String WINE_PRICE = "price";
    private static final String WINE_RANK = "snoothrank";
    private static final String WINE_IMAGE = "image";
    private static final String WINE_RESULTS = "results";
    private static final String META_DATA = "meta";
    private static final String WINES_OBJECT = "wines";

    private static final String LOG_TAG = WineJSONUtils.class.getSimpleName();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static List<Wine> getWineDataFromJSON(String wineJSONString) {
        if (TextUtils.isEmpty(wineJSONString)) {
            Log.i(LOG_TAG, "String wineJSONString is empty.");
            return null;
        }

        ArrayList<Wine> wineArrayList = new ArrayList<>();
        try {

            JSONObject wineJSONReader = new JSONObject(wineJSONString);

            JSONObject metaData = wineJSONReader.getJSONObject(META_DATA);
            int resultsNumber = metaData.optInt(WINE_RESULTS);

           // JSONObject allWineJSON = wineJSONReader.getJSONObject(WINES_OBJECT);
            JSONArray wineJSONArray = wineJSONReader.getJSONArray(WINES_OBJECT);

            for (int i = 0; i < wineJSONArray.length(); i++) {
                JSONObject currentWine = wineJSONArray.getJSONObject(i);
                String wineCode = currentWine.optString(WINE_CODE);
                String wineName = currentWine.optString(WINE_NAME);
                String wineRegion = currentWine.optString(WINE_REGION);
                String wineVintage = currentWine.optString(WINE_VINTAGE);
                String wineVarietal = currentWine.optString(WINE_VARIETAL);
                double winePrice = currentWine.optDouble(WINE_PRICE);
                double wineRank = currentWine.optDouble(WINE_RANK);
                String wineImage = currentWine.optString(WINE_IMAGE);

                Wine wineItem = new Wine(wineCode, wineName, wineRegion,
                        wineVarietal, winePrice, wineVintage, wineImage, wineRank);
                wineArrayList.add(wineItem);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Problem parsing Wine JSON results.", e);
        }
        return wineArrayList;
    }
}
