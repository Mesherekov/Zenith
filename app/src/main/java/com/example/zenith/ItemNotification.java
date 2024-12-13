package com.example.zenith;

import android.graphics.drawable.Drawable;

public class ItemNotification {
    String name;
    Drawable image;
    String UID;
    int colortext;
    String Uri;
    String key;
    public String getUri() {
        return Uri;
    }

    public void setUri(String uri) {
        Uri = uri;
    }

    public int getColortext() {
        return colortext;
    }

    public void setColortext(int colortext) {
        this.colortext = colortext;
    }

    public ItemNotification(String name, Drawable image, String UID, int colortext, String Uri, String key) {
        this.name = name;
        this.image = image;
        this.UID = UID;
        this.colortext = colortext;
        this.Uri = Uri;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
