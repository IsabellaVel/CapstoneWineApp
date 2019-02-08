package com.example.isabe.capstone_wineapp.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.SimpleTimeZone;

/**
 * Created by isabe on 7/22/2018.
 */
@Entity(tableName = "wines")
public class FavoriteEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String code;
    private String name;
    private String image;
    private String region;

    public FavoriteEntry(String name, String image, String region){
        this.id = id;
       // this.code = code;
        this.name = name;
        this.image = image;
        this.region = region;
    }

    public int getId(){return id;}
    public void setId(int id) {this.id = id;}

    public String getCode(){return code;}
    public void setCode(String code){this.code = code;}

    public String getName(){return name;}
    public void setName(String name){this.name = name;}

    public String getImage(){return image;}
    public void setImage(){this.image = image;}

    public String getRegion(){return region;}
    public void setRegion(String region){this.region = region;}
}
