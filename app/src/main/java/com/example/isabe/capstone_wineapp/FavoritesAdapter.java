package com.example.isabe.capstone_wineapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.isabe.capstone_wineapp.database.FavoriteEntry;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by isabe on 7/22/2018.
 */

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.WineViewHolder> {

    final private ItemClickListener mItemClickListener;
    private List<FavoriteEntry> favoriteWines;
    private Context mContext;

    public FavoritesAdapter(Context context, ItemClickListener listener) {
        mContext = context;
        mItemClickListener = listener;
    }

    @NonNull
    @Override
    public WineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.favorite_item, parent, false);
        return new WineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WineViewHolder holder, int position) {
        FavoriteEntry favoriteWine = favoriteWines.get(position);
        String favWineName = favoriteWine.getName();

        holder.mWineFavName.setText(favWineName);
    }

    @Override
    public int getItemCount() {
        if (favoriteWines == null) {
            return 0;
        }
        return favoriteWines.size();
    }

    public List<FavoriteEntry> getFavWines() {
        return favoriteWines;
    }

    /**
     * When data changes, this method updates the list of taskEntries
     * and notifies the adapter to use the new values on it
     */
    public void setFavWines(List<FavoriteEntry> favWines) {
        favoriteWines = favWines;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

    public class WineViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_wine_favorite_name)
        TextView mWineFavName;

        public WineViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int elementId = favoriteWines.get(getAdapterPosition()).getId();
            mItemClickListener.onItemClickListener(elementId);
        }
    }
}
