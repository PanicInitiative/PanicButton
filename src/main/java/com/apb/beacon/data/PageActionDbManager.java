package com.apb.beacon.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.apb.beacon.AppConstants;
import com.apb.beacon.model.PageAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aoe on 1/5/14.
 */
public class PageActionDbManager {

    private static final String TAG = PageActionDbManager.class.getSimpleName();

    private static final String TABLE_PAGE_ACTION = "page_action_table";

    private static final String PAGE_ID = "page_id";
    private static final String PAGE_LANGUAGE = "page_language";
    private static final String ACTION_TITLE = "action_title";
    private static final String ACTION_LINK = "action_link";
    private static final String ACTION_STATUS = "action_status";

    private static final String CREATE_TABLE_PAGE_ACTION = "create table " + TABLE_PAGE_ACTION + " ( "
            + AppConstants.TABLE_PRIMARY_KEY + " integer primary key autoincrement, " + PAGE_ID + " text, " + PAGE_LANGUAGE + " text, "
            + ACTION_TITLE + " text, " + ACTION_LINK + " text, " + ACTION_STATUS + " text);";

    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PAGE_ACTION);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAGE_ACTION);
    }

    public static long insert(SQLiteDatabase db, PageAction action, String pageId, String lang) throws SQLException {

        ContentValues cv = new ContentValues();

        cv.put(PAGE_ID, pageId);
        cv.put(PAGE_LANGUAGE, lang);
        cv.put(ACTION_TITLE, action.getTitle());
        cv.put(ACTION_LINK, action.getLink());
        cv.put(ACTION_STATUS, action.getStatus());

        return db.insert(TABLE_PAGE_ACTION, null, cv);
    }


    public static List<PageAction> retrieve(SQLiteDatabase db, String pageId, String lang) throws SQLException {
        List<PageAction> actionList = new ArrayList<PageAction>();

        Cursor c = db.query(TABLE_PAGE_ACTION, null, PAGE_ID + "=? AND " + PAGE_LANGUAGE + "=?", new String[]{pageId, lang}, null, null, null);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                String actionTitle = c.getString(c.getColumnIndex(ACTION_TITLE));
                String actionLink = c.getString(c.getColumnIndex(ACTION_LINK));
                String actionStatus = c.getString(c.getColumnIndex(ACTION_STATUS));
                PageAction action = new PageAction(actionTitle, actionLink, actionStatus);
                actionList.add(action);
                c.moveToNext();
            }
        }
        c.close();
        return actionList;
    }


    public static long update(SQLiteDatabase db, PageAction action, String pageId, String lang) throws SQLException {

        ContentValues cv = new ContentValues();

        cv.put(ACTION_TITLE, action.getTitle());
        cv.put(ACTION_LINK, action.getLink());
        cv.put(ACTION_STATUS, action.getStatus());

        return db.update(TABLE_PAGE_ACTION, cv, PAGE_ID + "=? AND " + PAGE_LANGUAGE + "=?", new String[]{pageId, lang});
    }


    public static boolean isExist(SQLiteDatabase db, String pageId, String lang) throws SQLException {
        boolean itemExist = false;

        Cursor c = db.query(TABLE_PAGE_ACTION, null, PAGE_ID + "=? AND " + PAGE_LANGUAGE + "=?", new String[]{pageId, lang}, null, null, null);

        if ((c != null) && (c.getCount() > 0)) {
            itemExist = true;
        }
        c.close();
        return itemExist;
    }


//    public static void insertOrUpdate(SQLiteDatabase db, PageAction action, String pageId, String lang){
//        if(isExist(db, pageId, lang)){
//            update(db, action, pageId, lang);
//        }
//        else{
//            insert(db, action, pageId, lang);
//        }
//    }


    public static int delete(SQLiteDatabase db, String pageId, String lang){
        return db.delete(TABLE_PAGE_ACTION, PAGE_ID + "=? AND " + PAGE_LANGUAGE + "=?", new String[]{pageId, lang});
    }
}
