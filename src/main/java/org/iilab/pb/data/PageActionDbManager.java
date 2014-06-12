package org.iilab.pb.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;


import java.util.ArrayList;
import java.util.List;

import org.iilab.pb.common.AppConstants;
import org.iilab.pb.model.PageAction;

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
    private static final String ACTION_LANGUAGE = "action_language";
    private static final String ACTION_CONFIRMATION = "action_confirmation";

    private static final String CREATE_TABLE_PAGE_ACTION = "create table " + TABLE_PAGE_ACTION + " ( "
            + AppConstants.TABLE_PRIMARY_KEY + " integer primary key autoincrement, " + PAGE_ID + " text, " +
            PAGE_LANGUAGE + " text, " + ACTION_TITLE + " text, " + ACTION_LINK + " text, " + ACTION_STATUS + " text, "
            + ACTION_LANGUAGE + " text, " + ACTION_CONFIRMATION + " text);";

    private static final String INSERT_SQL = "insert into  " + TABLE_PAGE_ACTION + " (" + PAGE_ID + ", " + PAGE_LANGUAGE + ", "
            + ACTION_TITLE + ", " + ACTION_LINK + ", " + ACTION_STATUS + ", " + ACTION_LANGUAGE +
            ", " + ACTION_CONFIRMATION + ") values (?,?,?,?,?,?,?)";

    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PAGE_ACTION);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAGE_ACTION);
    }

    public static long insert(SQLiteDatabase db, PageAction action, String pageId, String lang) throws SQLException {

        SQLiteStatement insertStatement = db.compileStatement(INSERT_SQL);

        if(pageId != null)
            insertStatement.bindString(1, pageId);
        if(lang != null)
            insertStatement.bindString(2, lang);
        if(action.getTitle() != null)
            insertStatement.bindString(3, action.getTitle());
        if(action.getLink() != null)
            insertStatement.bindString(4, action.getLink());
        if(action.getStatus() != null)
            insertStatement.bindString(5, action.getStatus());
        if(action.getLanguage() != null)
            insertStatement.bindString(6, action.getLanguage());
        if(action.getConfirmation() != null)
            insertStatement.bindString(7, action.getConfirmation());

        return insertStatement.executeInsert();

//        ContentValues cv = new ContentValues();
//
//        cv.put(PAGE_ID, pageId);
//        cv.put(PAGE_LANGUAGE, lang);
//        cv.put(ACTION_TITLE, action.getTitle());
//        cv.put(ACTION_LINK, action.getLink());
//        cv.put(ACTION_STATUS, action.getStatus());
//        cv.put(ACTION_LANGUAGE, action.getLanguage());
//
//        return db.insert(TABLE_PAGE_ACTION, null, cv);
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
                String actionLang = c.getString(c.getColumnIndex(ACTION_LANGUAGE));
                String confirmation = c.getString(c.getColumnIndex(ACTION_CONFIRMATION));

                PageAction action = new PageAction(actionTitle, actionLink, actionStatus, actionLang, confirmation);
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
        cv.put(ACTION_LANGUAGE, action.getLanguage());
        cv.put(ACTION_CONFIRMATION, action.getConfirmation());


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
