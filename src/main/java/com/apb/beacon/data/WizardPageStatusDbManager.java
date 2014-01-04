package com.apb.beacon.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.apb.beacon.AppConstants;
import com.apb.beacon.model.WizardPageStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aoe on 1/5/14.
 */
public class WizardPageStatusDbManager {

    private static final String TAG = WizardPageStatusDbManager.class.getSimpleName();

    private static final String TABLE_WIZARD_PAGE_STATUS = "wizard_page_status_table";

    private static final String PAGE_ID = "page_id";
    private static final String PAGE_LANGUAGE = "page_language";
    private static final String STATUS_TITLE = "status_title";
    private static final String STATUS_COLOR = "status_color";
    private static final String STATUS_LINK = "status_link";

    private static final String CREATE_TABLE_WIZARD_PAGE_STATUS = "create table " + TABLE_WIZARD_PAGE_STATUS + " ( "
            + AppConstants.TABLE_PRIMARY_KEY + " integer primary key autoincrement, " + PAGE_ID + " text, " + PAGE_LANGUAGE + " text, "
            + STATUS_TITLE + " text, " + STATUS_COLOR + " text, " + STATUS_LINK + " text);";

    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_WIZARD_PAGE_STATUS);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WIZARD_PAGE_STATUS);
    }


    public static long insert(SQLiteDatabase db, WizardPageStatus status, String pageId, String lang) throws SQLException {

        ContentValues cv = new ContentValues();

        cv.put(PAGE_ID, pageId);
        cv.put(PAGE_LANGUAGE, lang);
        cv.put(STATUS_TITLE, status.getTitle());
        cv.put(STATUS_COLOR, status.getColor());
        cv.put(STATUS_LINK, status.getLink());

        return db.insert(TABLE_WIZARD_PAGE_STATUS, null, cv);
    }


    public static List<WizardPageStatus> retrieve(SQLiteDatabase db, String pageId, String lang) throws SQLException {
        List<WizardPageStatus> statusList = new ArrayList<WizardPageStatus>();

        Cursor c = db.query(TABLE_WIZARD_PAGE_STATUS, null, PAGE_ID + "=? AND " + PAGE_LANGUAGE + "=?", new String[]{pageId, lang}, null, null, null);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                String statusTitle = c.getString(c.getColumnIndex(STATUS_TITLE));
                String statusColor = c.getString(c.getColumnIndex(STATUS_COLOR));
                String statusLink = c.getString(c.getColumnIndex(STATUS_LINK));
                WizardPageStatus status = new WizardPageStatus(statusTitle, statusColor, statusLink);
                statusList.add(status);
            }
        }
        c.close();
        return statusList;
    }


    public static long update(SQLiteDatabase db, WizardPageStatus status, String pageId, String lang) throws SQLException {

        ContentValues cv = new ContentValues();

        cv.put(STATUS_TITLE, status.getTitle());
        cv.put(STATUS_COLOR, status.getColor());
        cv.put(STATUS_LINK, status.getLink());

        return db.update(TABLE_WIZARD_PAGE_STATUS, cv, PAGE_ID + "=? AND " + PAGE_LANGUAGE + "=?", new String[]{pageId, lang});
    }


    public static boolean isExist(SQLiteDatabase db, String pageId, String lang) throws SQLException {
        boolean itemExist = false;

        Cursor c = db.query(TABLE_WIZARD_PAGE_STATUS, null, PAGE_ID + "=? AND " + PAGE_LANGUAGE + "=?", new String[]{pageId, lang}, null, null, null);

        if ((c != null) && (c.getCount() > 0)) {
            itemExist = true;
        }
        c.close();
        return itemExist;
    }


    public static void insertOrUpdate(SQLiteDatabase db, WizardPageStatus status, String pageId, String lang){
        if(isExist(db, pageId, lang)){
            update(db, status, pageId, lang);
        }
        else{
            insert(db, status, pageId, lang);
        }
    }


    public static int delete(SQLiteDatabase db, String pageId, String lang){
        return db.delete(TABLE_WIZARD_PAGE_STATUS, PAGE_ID + "=? AND " + PAGE_LANGUAGE + "=?", new String[]{pageId, lang});
    }
}
