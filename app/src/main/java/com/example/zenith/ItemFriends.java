package com.example.zenith;

import android.graphics.drawable.Drawable;

public class ItemFriends {
    String name;
    Drawable image;
    String UID;

    public ItemFriends(String name, Drawable image, String UID) {
        this.name = name;
        this.image = image;
        this.UID = UID;
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
