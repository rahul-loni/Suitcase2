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
    public static final String DB_NAME="suitcase.db";
    public static final int DB_VERSION=1;
    //Database Table Name
    public static final String ITEMS_TABLE_NAME="Items";
    //Database Columns
    public static final String ITEM_COLUMN_ID="id";
    public static final String ITEM_NAME="name";
    public static final String ITEM_PRICE="price";
    public static final String ITEM_DESCRIPTION="description";
    public static final String ITEM_IMAGE="image";
    public static final String ITEM_PURCHASED="purchased";




    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String itemstableQuery=" CREATE TABLE " +ITEMS_TABLE_NAME + "(" +
                ITEM_COLUMN_ID + "INTEGER PRIMARY KEY AUTOINCREMENT ," +
                ITEM_NAME +" TEXT NOT NULL , "+
                ITEM_PRICE +" TEXT NOT NULL ,"+
                ITEM_DESCRIPTION +" TEXT NOT NULL ,"+
                ITEM_IMAGE +" TEXT NOT NULL ," +
                ITEM_PURCHASED +" TEXT NOT NULL ," + "INTEGER)";

        try {
            sqLiteDatabase.execSQL(itemstableQuery);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " +ITEMS_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
    public Cursor queryData(String sqlQuery) {
        SQLiteDatabase database = getWritableDatabase();
        return database.rawQuery(sqlQuery, null);
    }
    //Insert Items Database
   public Boolean insertItems(String name,
                              double price,
                              String description,
                              String image,
                              boolean purchased

                              ) {
        SQLiteDatabase database=getWritableDatabase();
       String sql = "INSERT INTO " + ITEMS_TABLE_NAME + " VALUES (NULL, ?, ?, ?, ?, ?)";
       SQLiteStatement statement=database.compileStatement(sql);
       statement.clearBindings();
       statement.bindString(1,name);
       statement.bindDouble(2,price);
       statement.bindString(3,description);
       statement.bindString(4,image);
       statement.bindLong(5,purchased ?1:0);
       long result=statement.executeInsert();
       database.close();
       return result !=1;
   }
   //Get data from database table and column data
   public Cursor getItemById(int id) {
       SQLiteDatabase database = getWritableDatabase();
       String sqlQuery = "SELECT * FROM " + ITEMS_TABLE_NAME + " WHERE " + ITEM_COLUMN_ID + "=?";
       return database.rawQuery(
               sqlQuery, new String[]{String.valueOf(id)}
       );
   }

    public Cursor getAllItem() {
        SQLiteDatabase database = getReadableDatabase();
        String sqlQuery = "SELECT * FROM " + ITEMS_TABLE_NAME;
        return database.rawQuery(sqlQuery, null);
    }
    // Edit Items Method
    public Boolean update(int id,
                          String name,
                          double price,
                          String description,
                          String image,
                          boolean purchased){
        SQLiteDatabase database=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(ITEM_NAME,name);
        cv.put(ITEM_PRICE,price);
        cv.put(ITEM_DESCRIPTION,description);
        cv.put(ITEM_IMAGE,image);
        cv.put(ITEM_PURCHASED,purchased);
        int result = database.update(ITEMS_TABLE_NAME, cv, ITEM_COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        Log.d("Database helper:", "result: " + result);
        database.close();
        return result != -1;
    }
    //Delete Data from Database
    public void deleteItem(long id) {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(
                ITEMS_TABLE_NAME, ITEM_COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }

}
