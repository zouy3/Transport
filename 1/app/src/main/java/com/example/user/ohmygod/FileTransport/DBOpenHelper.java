package com.example.user.ohmygod.FileTransport;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by zouyun on 15/4/10.
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String DBNAME = "tran.db";
    private static final int VERSION = 1;

    public DBOpenHelper(Context context)
    {
        super(context, DBNAME, null, VERSION);
    }

    @Override

    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE IF NOT EXISTS pathlog(downpath varchar(100) primary key, filepath varchar(100))");
        db.execSQL("CREATE TABLE IF NOT EXISTS filedownlog(id integer primary key autoincrement, downpath varchar(100), threadid INTEGER, downlength INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS filedownlog");
        db.execSQL("DROP TABLE IF EXISTS pathlog");
        onCreate(db);
    }

}
