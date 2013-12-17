package com.apb.beacon.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.apb.beacon.model.LocalCachePage;

/**
 * Created by aoe on 12/12/13.
 */
public class LocalCacheDbManager {

    private static final String TAG = LocalCacheDbManager.class.getSimpleName();

    private static String TABLE_LOCAL_CACHE = "local_cache_table";

    public static final String TABLE_PRIMARY_KEY = "_id";

    private static String PAGE_NUMBER = "page_number";
    private static String PAGE_NAME = "page_name";
    private static String PAGE_TITLE = "page_title";
    private static String PAGE_ACTION = "page_action";
    private static String PAGE_OPTION = "page_option";
    private static String PAGE_CONTENT = "page_content";

    private static final String CREATE_TABLE_LOCAL_CACHE = "create table " + TABLE_LOCAL_CACHE + " ( "
            + TABLE_PRIMARY_KEY + " integer primary key autoincrement, " + PAGE_NUMBER + " integer, " + PAGE_NAME + " text, "
            + PAGE_ACTION + " text, " + PAGE_TITLE + " text, " + PAGE_OPTION + " text, " + PAGE_CONTENT + " text);";

    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_LOCAL_CACHE);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCAL_CACHE);
    }


    public static long insert(SQLiteDatabase db, LocalCachePage page) throws SQLException {

        ContentValues cv = new ContentValues();

        cv.put(PAGE_NUMBER, page.getPageNumber());
        cv.put(PAGE_NAME, page.getPageName());
        cv.put(PAGE_TITLE, page.getPageTitle());
        cv.put(PAGE_ACTION, page.getPageAction());
        cv.put(PAGE_OPTION, page.getPageOption());
        cv.put(PAGE_CONTENT, page.getPageContent());

        return db.insert(TABLE_LOCAL_CACHE, null, cv);
    }


    public static LocalCachePage retrievePage(SQLiteDatabase db, int pageNumber) throws SQLException {
        LocalCachePage page = new LocalCachePage();

        Cursor c = db.query(TABLE_LOCAL_CACHE, null, PAGE_NUMBER + "=" + pageNumber, null, null, null, null);
        if(c != null && c.getCount() > 0){
            c.moveToFirst();
            String pageName = c.getString(c.getColumnIndex(PAGE_NAME));
            String pageTitle = c.getString(c.getColumnIndex(PAGE_TITLE));
            String pageAction = c.getString(c.getColumnIndex(PAGE_ACTION));
            String pageOption = c.getString(c.getColumnIndex(PAGE_OPTION));
            String pageContent = c.getString(c.getColumnIndex(PAGE_CONTENT));
            page = new LocalCachePage(pageNumber, pageName, pageTitle, pageAction, pageOption, pageContent);
        }
        c.close();
        return page;
    }


    public static long update(SQLiteDatabase db, LocalCachePage page) throws SQLException {

        ContentValues cv = new ContentValues();

        cv.put(PAGE_NUMBER, page.getPageNumber());
        cv.put(PAGE_NAME, page.getPageName());
        cv.put(PAGE_TITLE, page.getPageTitle());
        cv.put(PAGE_ACTION, page.getPageAction());
        cv.put(PAGE_OPTION, page.getPageOption());
        cv.put(PAGE_CONTENT, page.getPageContent());

        return db.update(TABLE_LOCAL_CACHE, cv, PAGE_NUMBER + "=" + page.getPageNumber(), null);
    }


    public static boolean isExist(SQLiteDatabase db, int pageNumber) throws SQLException {
        boolean itemExist = false;

        Cursor c = db.query(TABLE_LOCAL_CACHE, null, PAGE_NUMBER + "=" + pageNumber, null, null, null, null);

        if ((c != null) && (c.getCount() > 0)) {
            itemExist = true;
        }
        c.close();
        return itemExist;
    }


    public static void insertOrUpdate(SQLiteDatabase db, LocalCachePage page){
        if(isExist(db, page.getPageNumber())){
            update(db, page);
        }
        else{
            insert(db, page);
        }
    }
}
