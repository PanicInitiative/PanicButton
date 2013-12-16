package com.apb.beacon.data;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.apb.beacon.model.LocalCachePage;

/**
 * Created by aoe on 12/12/13.
 */
public class PBDatabase {

    private static final String TAG = PBDatabase.class.getSimpleName();

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Context mContext;

    private static final String DATABASE_NAME = "pb_db";
    private static final int DATABASE_VERSION = 1;


    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context ctx) {
            super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            LocalCacheDbManager.createTable(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            LocalCacheDbManager.dropTable(db);

            onCreate(db);
        }
    }

    /** Constructor */
    public PBDatabase(Context ctx) {
        mContext = ctx;
    }

    public PBDatabase open() throws SQLException {
        dbHelper = new DatabaseHelper(mContext);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }


    public void insertOrUpdateLocalCachePage(LocalCachePage page){
        LocalCacheDbManager.insertOrUpdate(this.db, page);
    }

    public LocalCachePage retrievePage(int pageNumber){
        return LocalCacheDbManager.retrievePage(this.db, pageNumber);
    }
}
