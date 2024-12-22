package com.example.zenith;

import android.graphics.drawable.Drawable;

public class ItemNotification {
    String name;
    Drawable image;
    String UID;
    int colortext;
    String Uri;
    String key;
    String userkey;

    public String getTypeMassage() {
        return TypeMassage;
    }

    public void setTypeMassage(String typeMassage) {
        TypeMassage = typeMassage;
    }

    String TypeMassage;
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

    public ItemNotification(String name, Drawable image, String UID, int colortext, String Uri, String key, String userkey, String TypeMassage) {
        this.name = name;
        this.image = image;
        this.UID = UID;
        this.colortext = colortext;
        this.Uri = Uri;
        this.key = key;
        this.userkey = userkey;
        this.TypeMassage = TypeMassage;
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
