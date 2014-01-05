package com.apb.beacon.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.apb.beacon.AppConstants;
import com.apb.beacon.model.WizardPageItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aoe on 1/5/14.
 */
public class WizardPageItemDbManager {

    private static final String TAG = WizardPageItemDbManager.class.getSimpleName();

    private static final String TABLE_WIZARD_PAGE_ITEM = "wizard_page_item_table";

    private static final String PAGE_ID = "page_id";
    private static final String PAGE_LANGUAGE = "page_language";
    private static final String ITEM_TITLE = "item_title";
    private static final String ITEM_LINK = "item_link";

    private static final String CREATE_TABLE_WIZARD_PAGE_ITEM = "create table " + TABLE_WIZARD_PAGE_ITEM + " ( "
            + AppConstants.TABLE_PRIMARY_KEY + " integer primary key autoincrement, " + PAGE_ID + " text, " + PAGE_LANGUAGE + " text, "
            + ITEM_TITLE + " text, " + ITEM_LINK + " text);";

    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_WIZARD_PAGE_ITEM);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WIZARD_PAGE_ITEM);
    }

    public static long insert(SQLiteDatabase db, WizardPageItem item, String pageId, String lang) throws SQLException {

        ContentValues cv = new ContentValues();

        cv.put(PAGE_ID, pageId);
        cv.put(PAGE_LANGUAGE, lang);
        cv.put(ITEM_TITLE, item.getTitle());
        cv.put(ITEM_LINK, item.getLink());

        return db.insert(TABLE_WIZARD_PAGE_ITEM, null, cv);
    }


    public static List<WizardPageItem> retrieve(SQLiteDatabase db, String pageId, String lang) throws SQLException {
        List<WizardPageItem> statusList = new ArrayList<WizardPageItem>();

        Cursor c = db.query(TABLE_WIZARD_PAGE_ITEM, null, PAGE_ID + "=? AND " + PAGE_LANGUAGE + "=?", new String[]{pageId, lang}, null, null, null);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                String itemTitle = c.getString(c.getColumnIndex(ITEM_TITLE));
                String itemLink = c.getString(c.getColumnIndex(ITEM_LINK));
                WizardPageItem item = new WizardPageItem(itemTitle, itemLink);
                statusList.add(item);
                c.moveToNext();
            }
        }
        c.close();
        return statusList;
    }


    public static long update(SQLiteDatabase db, WizardPageItem item, String pageId, String lang) throws SQLException {

        ContentValues cv = new ContentValues();

        cv.put(ITEM_TITLE, item.getTitle());
        cv.put(ITEM_LINK, item.getLink());

        return db.update(TABLE_WIZARD_PAGE_ITEM, cv, PAGE_ID + "=? AND " + PAGE_LANGUAGE + "=?", new String[]{pageId, lang});
    }


    public static boolean isExist(SQLiteDatabase db, String pageId, String lang) throws SQLException {
        boolean itemExist = false;

        Cursor c = db.query(TABLE_WIZARD_PAGE_ITEM, null, PAGE_ID + "=? AND " + PAGE_LANGUAGE + "=?", new String[]{pageId, lang}, null, null, null);

        if ((c != null) && (c.getCount() > 0)) {
            itemExist = true;
        }
        c.close();
        return itemExist;
    }


    public static void insertOrUpdate(SQLiteDatabase db, WizardPageItem item, String pageId, String lang){
        if(isExist(db, pageId, lang)){
            update(db, item, pageId, lang);
        }
        else{
            insert(db, item, pageId, lang);
        }
    }


    public static int delete(SQLiteDatabase db, String pageId, String lang){
        return db.delete(TABLE_WIZARD_PAGE_ITEM, PAGE_ID + "=? AND " + PAGE_LANGUAGE + "=?", new String[]{pageId, lang});
    }
}
