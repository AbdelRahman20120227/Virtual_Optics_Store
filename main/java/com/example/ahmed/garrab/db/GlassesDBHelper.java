package com.example.ahmed.garrab.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ahmed.garrab.model.Glasses;

import java.util.ArrayList;

/**
 * Created by Shady on 11/03/2017.
 */

public class GlassesDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "VirtualStore";
    private static final int DATABASE_VERSION = 1;

    public GlassesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createSql =
                "CREATE TABLE " + GlassesContract.GlassesEntry.TABLE_NAME + "(\n" +
                        GlassesContract.GlassesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                        GlassesContract.GlassesEntry.BRAND + " TEXT, \n" +
                        GlassesContract.GlassesEntry.MODEL + " TEXT, \n" +
                        GlassesContract.GlassesEntry.URL + " TEXT, \n" +
                        GlassesContract.GlassesEntry.PRICE + " REAL )";

        db.execSQL(createSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String deleteSql = "DROP TABLE IF EXISTS " + GlassesContract.GlassesEntry.TABLE_NAME;
        db.execSQL(deleteSql);
        onCreate(db);
    }

    public void dropTable() {
        String deleteSql = "DROP TABLE IF EXISTS " + GlassesContract.GlassesEntry.TABLE_NAME;
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(deleteSql);
        onCreate(database);
    }

    public void removeGlass(int id) {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(
                GlassesContract.GlassesEntry.TABLE_NAME,
                GlassesContract.GlassesEntry._ID + "=" + id,
                null
        );
    }

    public ArrayList<Glasses> getFavoriteGlasses() {
        ArrayList<Glasses> glasses = new ArrayList<Glasses>();

        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(GlassesContract.GlassesEntry.TABLE_NAME, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(GlassesContract.GlassesEntry._ID));
                String model = cursor.getString(cursor.getColumnIndex(GlassesContract.GlassesEntry.MODEL));
                String brand = cursor.getString(cursor.getColumnIndex(GlassesContract.GlassesEntry.BRAND));
                String url = cursor.getString(cursor.getColumnIndex(GlassesContract.GlassesEntry.URL));
                double price = cursor.getDouble(cursor.getColumnIndex(GlassesContract.GlassesEntry.PRICE));

                Glasses glass = new Glasses(id, url, model, price, brand);
                glasses.add(glass);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return glasses;
    }

    public long addGlass(Glasses glasses) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(GlassesContract.GlassesEntry.BRAND, glasses.getBrand());
        contentValues.put(GlassesContract.GlassesEntry.MODEL, glasses.getModelName());
        contentValues.put(GlassesContract.GlassesEntry.URL, glasses.getUrl());
        contentValues.put(GlassesContract.GlassesEntry.PRICE, glasses.getPrice());

        SQLiteDatabase database = getWritableDatabase();
        return database.insert(GlassesContract.GlassesEntry.TABLE_NAME, null, contentValues);
    }

}
