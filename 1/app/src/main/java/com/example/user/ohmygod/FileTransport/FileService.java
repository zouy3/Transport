package com.example.user.ohmygod.FileTransport;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zouyun on 15/4/10.
 */
public class FileService {
    private DBOpenHelper openHelper;

    public FileService(Context context)
    {
        openHelper = new DBOpenHelper(context);
    }

    public Map<Integer, Integer> getData(String path)
    {
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select threadid, downlength from filedownlog where downpath=?", new String[]{path});
        Map<Integer, Integer> data = new HashMap<Integer, Integer>();
        while(cursor.moveToNext())
        {
            data.put(cursor.getInt(0), cursor.getInt(1));
        }
        cursor.close();
        db.close();
        return data;
    }

    public void save(String path, Map<Integer, Integer> map)
    {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        db.beginTransaction();
        try
        {
            for(Map.Entry<Integer, Integer> entry : map.entrySet())
            {
                db.execSQL("insert into filedownlog(downpath, threadid, downlength) values(?,?,?)",
                        new Object[]{path, entry.getKey(), entry.getValue()});
            }
            db.setTransactionSuccessful();
        }
        finally
        {
            db.endTransaction();
        }
        db.close();
    }

    public void savepath(String key, String path)
    {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        db.beginTransaction();
        try
        {
            db.execSQL("insert into pathlog(downpath, filepath) values(?,?)",
                    new Object[]{key, path});
            db.setTransactionSuccessful();
        }
        finally
        {
            db.endTransaction();
        }
        db.close();
    }

    public String getpath(String key)
    {
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select filepath from pathlog where downpath=?", new String[]{key});
        String data = null;
        while(cursor.moveToNext())
        {
            data = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return data;
    }

    public void update(String path, Map<Integer, Integer> map)
    {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        db.beginTransaction();
        try
        {
            for(Map.Entry<Integer, Integer> entry : map.entrySet())
            {
                db.execSQL("update filedownlog set downlength=? where downpath=? and threadid=?",
                        new Object[]{entry.getValue(), path, entry.getKey()});
            }
            db.setTransactionSuccessful();
        }
        finally
        {
            db.endTransaction();
        }
        db.close();
    }

    public void delete(String path)
    {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        db.execSQL("delete from filedownlog where downpath=?", new Object[]{path});
        db.execSQL("delete from pathlog where downpath=?", new Object[]{path});
        db.close();
    }
}
