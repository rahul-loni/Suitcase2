package com.example.suitcase2;

import static java.sql.Types.INTEGER;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME="suitcase.db";
    public static final String ITEMS_TABLE_NAME="Items";
    public static final String Col1="id";
    public static final String Col2="name";
    public static final String Col3="description";
    public static final String Col4="price";
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
}
