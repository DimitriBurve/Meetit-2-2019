package com.iutbm.example.iutbm.couchot.meetit_1;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;


/**
 * Created by couchot on 21/11/16.
 */

@Entity(tableName = "characters")
public class Character {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "firstname")
    private String firstname;

    @ColumnInfo(name = "familyname")
    private String familyname ;

    @ColumnInfo(name = "weburl")
    private String weburl;

    @ColumnInfo(name = "bmp_path")
    private String bmppath;


    @ColumnInfo(name = "latitude")
    private float latitude ;

    @ColumnInfo(name = "longitude")
    private float longitude ;

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setFamilyname(String familyname) {
        this.familyname = familyname;
    }

    public void setWeburl(String weburl) {
        this.weburl = weburl;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public void setBmppath(String bmppath) {
        this.bmppath = bmppath;
    }

    @Ignore
    public Character(int uid, String firstname, String familyname, String weburl, float latitude, float longitude, String bmppath) {
        this.uid = uid;
        this.firstname = firstname;
        this.familyname = familyname;
        this.weburl = weburl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.bmppath = bmppath;
    }

    public Character(String firstname, String familyname, String weburl, float latitude, float longitude, String bmppath) {
        this.firstname = firstname;
        this.familyname = familyname;
        this.weburl = weburl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.bmppath = bmppath;
    }

    public int getUid() { return uid; }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getFamilyname() {
        return familyname;
    }

    public String getWeburl() {
        return weburl;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getBmppath() { return bmppath; }


}