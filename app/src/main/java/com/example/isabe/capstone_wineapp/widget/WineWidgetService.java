package com.example.isabe.capstone_wineapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.isabe.capstone_wineapp.R;

/**
 * Created by isabe on 7/28/2018.
 */

public class WineWidgetService extends IntentService {

    private static final String LOG_TAG = WineWidgetService.class.getSimpleName();
    public static final String SHOW_WINE_DETAILS_FROM_WIDGET = "com.example.isabe.action.show_details";

    private Context mContext;
    private int mAppWidgetId;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public WineWidgetService() {
        super("WineWidgetService");
    }


    public static void startActionUpdateWineWidgets(Context context) {
        Intent intent = new Intent(context, WineWidgetService.class);
        intent.setAction(SHOW_WINE_DETAILS_FROM_WIDGET);
        context.startService(intent);
    }

    public void handleActionUpdateWineWidget() {
        mContext = this;
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                mContext.getString(R.string.prefs_file), Context.MODE_PRIVATE);
        String wineStringName = sharedPreferences.getString(mContext.getString(R.string.wine_name_prefs),
                getString(R.string.no_wines_saved));
        Log.i(LOG_TAG, getString(R.string.wine_in_widget));

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, WineWidgetService.class));
        //WineWidgetProvider.updateWineWidgets(this, appWidgetManager, wineStringName, appWidgetIds);


    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            handleActionUpdateWineWidget();
        }
    }
}
