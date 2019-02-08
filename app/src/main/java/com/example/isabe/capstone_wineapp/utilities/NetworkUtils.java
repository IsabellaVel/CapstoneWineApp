package com.example.isabe.capstone_wineapp.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.isabe.capstone_wineapp.object.Wine;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

/**
 * Created by isabe on 7/15/2018.
 */

public class NetworkUtils {
    private static Context mContext;
    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();
    public static boolean isOnline;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)


    public static boolean isOnline() {

        if (mContext != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager)
                    mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            isOnline = (networkInfo != null && networkInfo.isConnected());
        }
        return isOnline;

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static List<Wine> fetchWineData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;

        try {
            jsonResponse = getResponseFromHttp(url);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<Wine> wineList = WineJSONUtils.getWineDataFromJSON(jsonResponse);
        return wineList;
    }

    public static String getResponseFromHttp(URL url) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        try {
            httpURLConnection.getResponseCode();
        } catch (IOException e) {
            int responseCode = httpURLConnection.getResponseCode();
        }

        try {
            InputStream inputStream = httpURLConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            httpURLConnection.disconnect();
        }
    }

    public static URL createUrl(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);

        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Error with creating URL., e");
        }
        Log.v(LOG_TAG, "Built URI " + url);
        return url;
    }
}
