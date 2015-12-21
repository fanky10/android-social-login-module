package io.github.fanky10.sociallogin.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by fanky on 12/21/15.
 */
public abstract class BaseCrudDAO<T> extends BaseDAO<T> {
    protected static final String TABLE_METADATA_NAME = "name";
    protected static final String TABLE_METADATA_KEY = "primaryKeyName";
    private Map<String, String> mTableMetadata;

    public BaseCrudDAO(Context context) {
        super(context);
        validateTable();
    }

    private void validateTable() {
        mTableMetadata = getTableMetadata();
        if (!mTableMetadata.containsKey(TABLE_METADATA_NAME) || mTableMetadata.get(TABLE_METADATA_NAME).isEmpty()) {
            throw new IllegalArgumentException("Table name not provided");
        }
        if (!mTableMetadata.containsKey(TABLE_METADATA_KEY) || mTableMetadata.get(TABLE_METADATA_KEY)
                .isEmpty()) {
            throw new IllegalArgumentException("No primary key provided");
        }
        // TODO: see scope if a class for this purpose is needed
    }

    protected abstract Map<String, String> getTableMetadata();

    protected String getTableName() {
        return mTableMetadata.get(TABLE_METADATA_NAME);
    }

    protected String getTablePrimaryKeyName() {
        return mTableMetadata.get(TABLE_METADATA_KEY);
    }

    public T findOne(String key) {
        Cursor cursor = mDatabase.rawQuery(
                "SELECT * FROM " + getTableName() + " WHERE " + getTablePrimaryKeyName() + "=?",
                new String[]{key});
        T found = null;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            found = create(cursor);
        }

        return found;
    }

    public List<T> findAll() {
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM " + getTableName(), null);
        List<T> found = new ArrayList<T>();

        while (cursor.moveToNext()) {
            T model = create(cursor);
            found.add(model);
        }

        return found;
    }

    public long count() {
        return DatabaseUtils.queryNumEntries(mDatabase, getTableName());
    }

    public void delete(String key) {
        mDatabase.delete(getTableName(), getTablePrimaryKeyName() + " = ? ", new String[]{key});
    }

    public void save(T entity) {
        if (!exists(entity)) {
            mDatabase.insert(getTableName(), null, getTableMap(entity));
        } else {
            update(entity);
        }
    }

    public abstract boolean exists(T entity);
    protected abstract void update(T entity);
}
