package com.example.cookbook.LocalDB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FavDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "FavDB";
    public static final String TABLE_NAME = "FavTable";
    public static final String COL_1 = "meal_id";
    String CREATE_FAV_TABLE = "CREATE TABLE " + TABLE_NAME + "(id INTEGER PRIMARY KEY, " + COL_1 + " INT)";

    public FavDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(@NotNull SQLiteDatabase db) {
        db.execSQL(CREATE_FAV_TABLE);
    }

    @Override
    public void onUpgrade(@NotNull SQLiteDatabase db, int oldi, int newi) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addFavFood(int meal_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, meal_id);
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    } //Add a food product

    /*public void deleteFavourites() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.close();
    }*/ //Delete Favourites (Not Used)

    public boolean removeFavFood(int meal_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query_1 = "DELETE FROM " + TABLE_NAME + " WHERE " + COL_1 + "= " + meal_id;
        Log.d(TAG, "deleteName: query: " + query_1);
        db.execSQL(query_1);
        return true;
    } //Remove one item

    public long checkSpecficFood(int meal_id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        return DatabaseUtils.longForQuery(sqLiteDatabase, "SELECT COUNT (*) FROM " + TABLE_NAME + " WHERE " + COL_1 + "=?",
                new String[]{String.valueOf(meal_id)});

    } //CHeck if a specific food exists in the favourties or not

    public ArrayList<Integer> getAllFavFoods() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<Integer> arrayList = new ArrayList<>();
        @SuppressLint("Recycle") Cursor cursor_1 = sqLiteDatabase.rawQuery("select * from " + TABLE_NAME, null);
        cursor_1.moveToFirst();
        while (!cursor_1.isAfterLast()) {
            arrayList.add(cursor_1.getInt(cursor_1.getColumnIndex(COL_1)));
            cursor_1.moveToNext();
        }
        return arrayList;

    } //Returns all the values of the favourites
}
