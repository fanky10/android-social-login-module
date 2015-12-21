package io.github.fanky10.sociallogin.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by fanky on 12/18/15.
 */
public abstract class BaseDAO<T> {
    protected static String TAG = "BaseDAO";

    public static final String COLUMN_TYPE_DOUBLE = " REAL "; //DOUBLE
    public static final String COLUMN_TYPE_TEXT = " TEXT ";
    public static final String COLUMN_TYPE_DATE = " DATETIME ";//NUMERIC
    public static final String COLUMN_TYPE_INT = " INTEGER ";
    public static final String COLUMN_TYPE_LONG = " INTEGER ";
    public static final String COLUMN_TYPE_BOOLEAN = " BOOLEAN "; //NUMERIC
    public static final String COLUMN_PRIMARY_KEY = " PRIMARY KEY ";

    // Database fields
    protected SQLiteDatabase mDatabase;
    protected SQLiteOpenHelperImpl mDbHelper;
    protected Context mContext;

    public BaseDAO(Context context) {
        this.mContext = context;
        mDbHelper = SQLiteOpenHelperImpl.getInstance(context);
        // open the database
        try {
            open();
        } catch (SQLException e) {
            Log.e(TAG, "SQLException on opening database " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void open() throws SQLException {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    // abstract methods
    protected abstract ContentValues getTableMap(T model);

    protected abstract T create(Cursor cursor);
}
