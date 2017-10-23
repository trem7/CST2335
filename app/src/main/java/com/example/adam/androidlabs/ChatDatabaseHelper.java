package com.example.adam.androidlabs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Adam on 10/17/2017.
 */

public class ChatDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Messages.db";
    private static final int VERSION_NUM = 1;
    public static final String KEY_ID = "_id";
    public static final String KEY_MESSAGE = "MESSAGE";
    public static final String TABLE_NAME = "MyTable";


    public ChatDatabaseHelper(Context ctx){
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ( "+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+ KEY_MESSAGE + " text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

}
