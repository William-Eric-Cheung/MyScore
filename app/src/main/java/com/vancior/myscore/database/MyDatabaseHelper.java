package com.vancior.myscore.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by H on 2017/3/8.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String CREATE_TABLE = "create table Favorite ("
            + "bookmark text, "
            + "author text, "
            + "linkurl text, "
            + "imgurl text)";

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("drop table if exists Favorite");
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists Favorite");
        onCreate(sqLiteDatabase);
    }
}
