package com.example.zenith;

import android.graphics.drawable.Drawable;

public class Item {
    String name;
    Drawable image;
    String UID;
    int colortext;

    public int getColortext() {
        return colortext;
    }

    public void setColortext(int colortext) {
        this.colortext = colortext;
    }

    public Item(String name, Drawable image, String UID, int colortext) {
        this.name = name;
        this.image = image;
        this.UID = UID;
        this.colortext = colortext;
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
