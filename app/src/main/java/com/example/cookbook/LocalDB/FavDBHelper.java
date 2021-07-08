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

import com.example.cookbook.DataModels.MainPageModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FavDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "FavDB";
    public static final String TABLE_NAME = "FavTable";
    public static final String COL_1 = "meal_id";
    public static final String COL_2 = "strMeal";
    public static final String COL_3 = "strCategory";
    public static final String COL_4 = "strArea";
    public static final String COL_5 = "strMealThumb";
    public static final String COL_6 = "strInstructions";
    public static final String COL_7 = "strTags";
    public static final String COL_8 = "strYoutube";
    String CREATE_FAV_TABLE = "CREATE TABLE " + TABLE_NAME + "(id INTEGER PRIMARY KEY, " + COL_1 + " INT, " + COL_2 + " TEXT, " + COL_3 + " TEXT, " + COL_4 + " TEXT, " + COL_5 + " TEXT, " + COL_6 + " TEXT, " + COL_7 + " TEXT, " + COL_8 + " TEXT)";

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

    public boolean addFavFood(int meal_id, String strMeal, String strCategory, String strArea, String strMealThumb, String strInstructions, String strTags, String strYoutube) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, meal_id);
        contentValues.put(COL_2, strMeal);
        contentValues.put(COL_3, strCategory);
        contentValues.put(COL_4, strArea);
        contentValues.put(COL_5, strMealThumb);
        contentValues.put(COL_6, strInstructions);
        contentValues.put(COL_7, strTags);
        contentValues.put(COL_8, strYoutube);
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

    public ArrayList<MainPageModel> getAllFavFoods() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<MainPageModel> arrayList = new ArrayList<>();
        @SuppressLint("Recycle") Cursor cursor_1 = sqLiteDatabase.rawQuery("select * from " + TABLE_NAME, null);
        cursor_1.moveToFirst();
        while (!cursor_1.isAfterLast()) {
            arrayList.add(new MainPageModel(
                    cursor_1.getInt(cursor_1.getColumnIndex(COL_1)),
                    cursor_1.getString(cursor_1.getColumnIndex(COL_2)),
                    cursor_1.getString(cursor_1.getColumnIndex(COL_3)),
                    cursor_1.getString(cursor_1.getColumnIndex(COL_4)),
                    cursor_1.getString(cursor_1.getColumnIndex(COL_5)),
                    cursor_1.getString(cursor_1.getColumnIndex(COL_6)),
                    cursor_1.getString(cursor_1.getColumnIndex(COL_7)),
                    cursor_1.getString(cursor_1.getColumnIndex(COL_8))));
            cursor_1.moveToNext();
        }
        return arrayList;

    } //Returns all the values of the favourites
}
