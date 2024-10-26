package com.example.zenith;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SettingsDatabase extends SQLiteOpenHelper {
    public static final int VERSION_DATABASE = 1;
    public static final String SETTINGS_DATABASE = "settings";
    public static final String TABLE_SETTINGS = "settings";
    public static final String ID = "id";
    public static final String LANGUAGE = "language";
    public static final String THEME = "theme";
    public static final String COLOROFTHEME = "coloroftheme";

    public SettingsDatabase(@Nullable Context context) {
        super(context, SETTINGS_DATABASE, null, VERSION_DATABASE);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_SETTINGS + "(" + ID + " integer primary key," + LANGUAGE + " text," + THEME + " text," + COLOROFTHEME + " integer" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_SETTINGS);
        onCreate(sqLiteDatabase);
    }
}
