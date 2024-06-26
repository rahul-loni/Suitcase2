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
    public static final String DB_NAME="suitcase.db";
    public static final String ITEMS_TABLE_NAME="Items";
    public static final String Col1="id";
    public static final String Col2="name";
    public static final String Col3="price";
    public static final String Col4="description";
    public static final String Col5="image";
    public static final String Col6="purchased";
    public static final int DB_VERSION=1;



    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String itemstableQuery=" CREATE TABLE " +ITEMS_TABLE_NAME + "(" +
                Col1 + "INTEGER PRIMARY KEY AUTOINCREMENT ," +
                Col2 +" TEXT NOT NULL , "+
                Col3 +" TEXT NOT NULL ,"+
                Col4 +" TEXT NOT NULL ,"+
                Col5 +" TEXT NOT NULL ," +
                Col6 +" TEXT NOT NULL ," + "INTEGER)";

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
    //Insert Items Database
   public Boolean insertItems(String name,
                              String description,
                              double price,
                              boolean purchased ,
                              String image
                              ) {
        SQLiteDatabase database=getWritableDatabase();
        String sql=" INSERT INTO " + ITEMS_TABLE_NAME + " VALUES (NULL,?,?,?,?)";
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
    public Cursor getItemById(int id){
        SQLiteDatabase database=getWritableDatabase();
        String sqlQuery="SELECT * FROM "+ITEMS_TABLE_NAME + " WHERE "+ Col1 + "=?";
        return database.rawQuery(sqlQuery, new String[]{String.valueOf(id)});
    }
    // get all data from database
    public Cursor getAllData(){
        SQLiteDatabase database=getReadableDatabase();
        String sqlQuery="SELECT * FROM " +ITEMS_TABLE_NAME;
        return database.rawQuery(sqlQuery,null);
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
        cv.put(Col2,name);
        cv.put(Col3,price);
        cv.put(Col4,description);
        cv.put(Col5,image);
        cv.put(Col6,purchased);
        int result = database.update(ITEMS_TABLE_NAME,cv,Col1 +"=?",
                new String[]{String.valueOf(id)});
        Log.d("databaseHelper:","result"+result);
        database.close();
        return result !=1;
    }
    //Delete Data from Database
    public void deleteItem(long id){
        SQLiteDatabase database=getWritableDatabase();
        database.delete(ITEMS_TABLE_NAME,Col1+"=?",
                new String[]{String.valueOf(id)});
    }

}
