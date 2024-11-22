package com.example.zenith;

import android.graphics.drawable.Drawable;

public class ItemFriends {
    String name;
    Drawable image;
    String UID;
    int colortext;
    String Uri;

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

    public ItemFriends(String name, Drawable image, String UID, int colortext, String Uri) {
        this.name = name;
        this.image = image;
        this.UID = UID;
        this.colortext = colortext;
        this.Uri = Uri;
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
