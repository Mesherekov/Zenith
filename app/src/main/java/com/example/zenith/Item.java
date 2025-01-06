package com.example.zenith;

import android.graphics.drawable.Drawable;

public class Item {
    String name;
    Drawable image;
    String UID;
    int colortext;
    String key;
    String privacy;

    public int getColortext() {
        return colortext;
    }

    public void setColortext(int colortext) {
        this.colortext = colortext;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Item(String name, Drawable image, String UID, int colortext, String key, String privacy) {
        this.name = name;
        this.image = image;
        this.UID = UID;
        this.colortext = colortext;
        this.key = key;
        this.privacy = privacy;
    }
    public String getUID(){return UID;}

    public String getName() {
        return name;
    }

    public Drawable getImage() {
        return image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }
}
