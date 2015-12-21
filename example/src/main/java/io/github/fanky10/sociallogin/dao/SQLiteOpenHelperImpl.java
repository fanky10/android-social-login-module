package io.github.fanky10.sociallogin.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by fanky on 12/18/15.
 */
public class SQLiteOpenHelperImpl extends SQLiteOpenHelper {

    public static final String TAG = "DatabaseDAL";

    public static final String DATABASE_NAME = "main.db";
    public static final int CURRENT_DATABASE_VERSION = 1; // current DB Version

    private static SQLiteOpenHelperImpl instance;

    public static synchronized SQLiteOpenHelperImpl getInstance(Context context)
    {
        if (instance == null) {
            instance = new SQLiteOpenHelperImpl(context);
        }

        return instance;
    }

    public SQLiteOpenHelperImpl(Context context) {
        super(context, DATABASE_NAME, null, CURRENT_DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(UsersDAO.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading the database from version " + oldVersion + " to " + newVersion);

        // clear all data by dropping tables.
        db.execSQL("DROP TABLE IF EXISTS " + UsersDAO.TABLE_NAME);

        // recreate tables.
        db.execSQL(UsersDAO.SQL_CREATE_TABLE);
    }
}