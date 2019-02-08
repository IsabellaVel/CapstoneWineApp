package com.example.isabe.capstone_wineapp.object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by isabe on 7/14/2018.
 */

public class Wine implements Parcelable {
    public String mWineName;
    public String mCode;
    public String mWineRegion;
    public String mWineVarietal;
    public double mWinePrice;
    public String mWineVintage;
    public String mWineImage;
    public double mWineRank;

    public Wine(String code, String name, String region, String varietal, double price,
                String vintage, String image, double rank) {
        this.mCode = code;
        this.mWineName = name;
        this.mWineRegion = region;
        this.mWineVarietal = varietal;
        this.mWinePrice = price;
        this.mWineVintage = vintage;
        this.mWineImage = image;
        this.mWineRank = rank;
    }

    protected Wine(Parcel in) {
        mCode = in.readString();
        mWineName = in.readString();
        mWineRegion = in.readString();
        mWineVarietal = in.readString();
        mWinePrice = in.readDouble();
        mWineVintage = in.readString();
        mWineImage = in.readString();
        mWineRank = in.readDouble();
    }

    public static final Creator<Wine> CREATOR = new Parcelable.Creator<Wine>() {
        @Override
        public Wine createFromParcel(Parcel in) {
            return new Wine(in);
        }

        @Override
        public Wine[] newArray(int size) {
            return new Wine[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mCode);
        dest.writeString(mWineName);
        dest.writeString(mWineRegion);
        dest.writeString(mWineVarietal);
        dest.writeDouble(mWinePrice);
        dest.writeString(mWineVintage);
        dest.writeString(mWineImage);
        dest.writeDouble(mWineRank);

    }

    public String getmCode(){
        return mCode;
    }

    public String getmWineName(){
        return mWineName;
    }

    public String getmWineRegion(){
        return mWineRegion;
    }

    public String getmWineVarietal(){
        return mWineVarietal;
    }

    public String getmWineVintage(){
        return mWineVintage;
    }

    public String getmWineImage(){
        return mWineImage;
    }

    public double getmWinePrice(){
        return mWinePrice;
    }

    public double getmWineRank(){
        return mWineRank;
    }
}
