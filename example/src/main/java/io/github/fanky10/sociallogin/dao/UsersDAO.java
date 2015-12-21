package io.github.fanky10.sociallogin.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.github.fanky10.sociallogin.model.BasicUserModel;

/**
 * Created by fanky on 12/18/15.
 */
public class UsersDAO extends BaseCrudDAO<BasicUserModel> {

    public static final String TABLE_NAME = "users";

    public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + " username" + BaseDAO.COLUMN_TYPE_TEXT + BaseDAO.COLUMN_PRIMARY_KEY
            + ", password" + BaseDAO.COLUMN_TYPE_TEXT
            + ", scope" + BaseDAO.COLUMN_TYPE_TEXT
            + ")";

    public UsersDAO(Context context) {
        super(context);
    }

    @Override
    protected Map<String, String> getTableMetadata() {
        Map<String,String> tableMetadata = new HashMap<>();
        tableMetadata.put(TABLE_METADATA_NAME, TABLE_NAME);
        tableMetadata.put(TABLE_METADATA_KEY, "username");

        return tableMetadata;
    }

    @Override
    protected ContentValues getTableMap(BasicUserModel model) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", model.getUsername());
        contentValues.put("password", model.getPassword());
        contentValues.put("scope", model.getScope());

        return contentValues;
    }

    @Override
    protected BasicUserModel create(Cursor cursor) {
        BasicUserModel model = new BasicUserModel();
        model.setUsername(cursor.getString(cursor.getColumnIndex("username")));
        model.setPassword(cursor.getString(cursor.getColumnIndex("password")));
        model.setScope(cursor.getString(cursor.getColumnIndex("scope")));

        return model;
    }


}
