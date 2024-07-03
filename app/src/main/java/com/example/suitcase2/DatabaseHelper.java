package com.example.suitcase2;

import static java.sql.Types.INTEGER;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    //Database name
    public static final String DB_NAME = "suitcase.db";
    public static final int DB_VERSION = 1;
    //Database Table Name
    public static final String TABLE_NAME = "Items";
    //Database Columns
    public static final String ITEM_COLUMN_ID = "id";
    public static final String ITEM_NAME = "name";
    public static final String ITEM_PRICE = "price";
    public static final String ITEM_DESCRIPTION = "description";
    public static final String ITEM_IMAGE = "image";
    public static final String ITEM_PURCHASED = "purchased";
    public static final String ITEM_LOCATION = "location";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                ITEM_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ITEM_NAME + " TEXT NOT NULL, " +
                ITEM_PRICE + " REAL NOT NULL, " +
                ITEM_DESCRIPTION + " TEXT NOT NULL, " +
                ITEM_IMAGE + " TEXT NOT NULL, " +
                ITEM_PURCHASED + " INTEGER NOT NULL, " +
                ITEM_LOCATION + " TEXT)";

        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public Cursor queryData(String sqlQuery) {
        SQLiteDatabase database = getWritableDatabase();
        return database.rawQuery(sqlQuery, null);
    }

    //Insert Items Database
    public Boolean insertItems(
            String name,
            double price,
            String description,
            String image,
            boolean purchased,
            String location
    ) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO " + TABLE_NAME + " VALUES (NULL, ?, ?, ?, ?, ?, ?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, name);
        statement.bindDouble(2, price);
        statement.bindString(3, description);
        statement.bindString(4, image);
        statement.bindLong(5, purchased ? 1 : 0);
        statement.bindString(6, location);
        long result = statement.executeInsert();
        database.close();
        return result != 1;
    }

    //Get data from database table and column data
    public Cursor getItemById(int id) {
        SQLiteDatabase database = getWritableDatabase();
        String sqlQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + ITEM_COLUMN_ID + "=?";
        return database.rawQuery(sqlQuery, new String[]{String.valueOf(id)});
    }

    public Cursor getAllItem() {
        SQLiteDatabase database = getReadableDatabase();
        String sqlQuery = "SELECT * FROM " + TABLE_NAME;
        return database.rawQuery(sqlQuery, null);
    }

    public Cursor getAllPurchased() {
        SQLiteDatabase database = getReadableDatabase();
        String sqlQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + ITEM_PURCHASED + "=0";
        return database.rawQuery(sqlQuery, null);
    }

    // Edit Items Method
    public Boolean update(int id, String name, double price, String description, String image, boolean purchased, String location) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ITEM_NAME, name);
        cv.put(ITEM_PRICE, price);
        cv.put(ITEM_DESCRIPTION, description);
        cv.put(ITEM_IMAGE, image);
        cv.put(ITEM_PURCHASED, purchased);
        cv.put(ITEM_LOCATION, location);
        int result = database.update(TABLE_NAME, cv, ITEM_COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        Log.d("Database helper:", "result: " + result);
        database.close();
        return result != -1;
    }

    //Delete Data from Database
    public void deleteItem(long id) {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(TABLE_NAME, ITEM_COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }

}
