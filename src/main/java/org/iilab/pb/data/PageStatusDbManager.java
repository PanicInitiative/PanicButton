package org.iilab.pb.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;


import java.util.ArrayList;
import java.util.List;

import org.iilab.pb.common.AppConstants;
import org.iilab.pb.model.PageStatus;

/**
 * Created by aoe on 1/5/14.
 */
public class PageStatusDbManager {

    private static final String TAG = PageStatusDbManager.class.getSimpleName();

    private static final String TABLE_PAGE_STATUS = "page_status_table";

    private static final String PAGE_ID = "page_id";
    private static final String PAGE_LANGUAGE = "page_language";
    private static final String STATUS_TITLE = "status_title";
    private static final String STATUS_COLOR = "status_color";
    private static final String STATUS_LINK = "status_link";

    private static final String CREATE_TABLE_PAGE_STATUS = "create table " + TABLE_PAGE_STATUS + " ( "
            + AppConstants.TABLE_PRIMARY_KEY + " integer primary key autoincrement, " + PAGE_ID + " text, " + PAGE_LANGUAGE + " text, "
            + STATUS_TITLE + " text, " + STATUS_COLOR + " text, " + STATUS_LINK + " text);";

    private static final String INSERT_SQL = "insert into  " + TABLE_PAGE_STATUS + " (" + PAGE_ID + ", " + PAGE_LANGUAGE + ", "
            + STATUS_TITLE + ", " + STATUS_COLOR + ", " + STATUS_LINK + ") values (?,?,?,?,?)";

    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PAGE_STATUS);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAGE_STATUS);
    }


    public static long insert(SQLiteDatabase db, PageStatus status, String pageId, String lang) throws SQLException {

        SQLiteStatement insertStatement = db.compileStatement(INSERT_SQL);

        if(pageId != null)
            insertStatement.bindString(1, pageId);
        if(lang != null)
            insertStatement.bindString(2, lang);
        if(status.getTitle() != null)
            insertStatement.bindString(3, status.getTitle());
        if(status.getColor() != null)
            insertStatement.bindString(4, status.getColor());
        if(status.getLink() != null)
            insertStatement.bindString(5, status.getLink());

        return insertStatement.executeInsert();

//        ContentValues cv = new ContentValues();
//
//        cv.put(PAGE_ID, pageId);
//        cv.put(PAGE_LANGUAGE, lang);
//        cv.put(STATUS_TITLE, status.getTitle());
//        cv.put(STATUS_COLOR, status.getColor());
//        cv.put(STATUS_LINK, status.getLink());
//
//        return db.insert(TABLE_PAGE_STATUS, null, cv);
    }


    public static List<PageStatus> retrieve(SQLiteDatabase db, String pageId, String lang) throws SQLException {
        List<PageStatus> statusList = new ArrayList<PageStatus>();

        Cursor c = db.query(TABLE_PAGE_STATUS, null, PAGE_ID + "=? AND " + PAGE_LANGUAGE + "=?", new String[]{pageId, lang}, null, null, null);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                String statusTitle = c.getString(c.getColumnIndex(STATUS_TITLE));
                String statusColor = c.getString(c.getColumnIndex(STATUS_COLOR));
                String statusLink = c.getString(c.getColumnIndex(STATUS_LINK));
                PageStatus status = new PageStatus(statusTitle, statusColor, statusLink);
                statusList.add(status);
                c.moveToNext();
            }
        }
        c.close();
        return statusList;
    }


    public static long update(SQLiteDatabase db, PageStatus status, String pageId, String lang) throws SQLException {

        ContentValues cv = new ContentValues();

        cv.put(STATUS_TITLE, status.getTitle());
        cv.put(STATUS_COLOR, status.getColor());
        cv.put(STATUS_LINK, status.getLink());

        return db.update(TABLE_PAGE_STATUS, cv, PAGE_ID + "=? AND " + PAGE_LANGUAGE + "=?", new String[]{pageId, lang});
    }


    public static boolean isExist(SQLiteDatabase db, String pageId, String lang) throws SQLException {
        boolean itemExist = false;

        Cursor c = db.query(TABLE_PAGE_STATUS, null, PAGE_ID + "=? AND " + PAGE_LANGUAGE + "=?", new String[]{pageId, lang}, null, null, null);

        if ((c != null) && (c.getCount() > 0)) {
            itemExist = true;
        }
        c.close();
        return itemExist;
    }


//    public static void insertOrUpdate(SQLiteDatabase db, PageStatus status, String pageId, String lang){
//        if(isExist(db, pageId, lang)){
//            update(db, status, pageId, lang);
//        }
//        else{
//            insert(db, status, pageId, lang);
//        }
//    }


    public static int delete(SQLiteDatabase db, String pageId, String lang){
        return db.delete(TABLE_PAGE_STATUS, PAGE_ID + "=? AND " + PAGE_LANGUAGE + "=?", new String[]{pageId, lang});
    }
}
