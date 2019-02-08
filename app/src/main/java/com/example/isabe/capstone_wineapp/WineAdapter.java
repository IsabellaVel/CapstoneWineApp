package com.example.isabe.capstone_wineapp;

import android.app.WallpaperInfo;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.isabe.capstone_wineapp.database.FavoriteEntry;
import com.example.isabe.capstone_wineapp.object.Wine;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by isabe on 7/15/2018.
 */

public class WineAdapter extends ArrayAdapter<Wine> {

    @BindView(R.id.tv_wine_item_name)
    TextView wineItemTv;

    @BindView(R.id.tv_wine_vintage)
    TextView wineVintageTv;

    private List<Wine> listOfWines = new ArrayList<>();
    private List<FavoriteEntry> favoriteWines = new ArrayList<>();

    private final Context mContext;

    public WineAdapter(@NonNull Context context, int resource, @NonNull List<Wine> wineItems) {
        super(context, 0, wineItems);
        mContext = context;
        listOfWines = wineItems;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Wine wineItem = listOfWines.get(position);
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.wine_item, parent, false);
        }
        ButterKnife.bind(this, listItemView);
        wineItemTv.setText(wineItem.getmWineName());
        wineVintageTv.setText(wineItem.getmWineVintage());

        return listItemView;
    }

  }
