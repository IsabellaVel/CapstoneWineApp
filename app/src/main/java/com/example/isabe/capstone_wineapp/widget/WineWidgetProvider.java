package com.example.isabe.capstone_wineapp.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.example.isabe.capstone_wineapp.MainActivity;
import com.example.isabe.capstone_wineapp.R;
import com.example.isabe.capstone_wineapp.WineDetailsActivity;

import static android.os.Build.VERSION_CODES.N;

/**
 * Implementation of App Widget functionality.
 */
public class WineWidgetProvider extends AppWidgetProvider {

    public static final String SHOW_WINE_DETAILS_FROM_WIDGET = "com.example.isabe.action.show_details";
    public static final String WINE_CODE = "this wine";
    private static Context mContext;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        WineWidgetService.startActionUpdateWineWidgets(context);

        for (int appWidgetId : appWidgetIds) {
            mContext = context;
            SharedPreferences sharedPreferences = mContext.getSharedPreferences(

                    mContext.getString(R.string.prefs_file), Context.MODE_PRIVATE);
            String wineStringName = sharedPreferences.getString(mContext.getString(R.string.wine_name_prefs),
                    mContext.getString(R.string.no_wines_saved));

            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.wine_widget);
            remoteViews.setTextViewText(R.id.widget_name, wineStringName);
            remoteViews.setOnClickPendingIntent(R.id.appwidget_image, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

