package org.iilab.pb.data;

import org.iilab.pb.common.AppConstants;
import org.iilab.pb.model.PageTimer;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;


/**
 * Created by aoe on 1/16/14.
 */
public class PageTimerDbManager {

    private static final String TAG = PageTimerDbManager.class.getSimpleName();

    private static final String TABLE_PAGE_TIMER = "page_timer_table";

    private static final String PAGE_ID = "page_id";
    private static final String PAGE_LANGUAGE = "page_language";
    private static final String TIMER_INFO = "timer_info";
    private static final String TIMER_INACTIVE = "timer_inactive";
    private static final String TIMER_FAIL = "timer_fail";

    private static final String CREATE_TABLE_PAGE_TIMER = "create table " + TABLE_PAGE_TIMER + " ( "
            + AppConstants.TABLE_PRIMARY_KEY + " integer primary key autoincrement, " + PAGE_ID + " text, " + PAGE_LANGUAGE + " text, " + TIMER_INFO + " text, "
            + TIMER_INACTIVE + " text, " + TIMER_FAIL + " text);";

    private static final String INSERT_SQL = "insert into  " + TABLE_PAGE_TIMER + " (" + PAGE_ID + ", " + PAGE_LANGUAGE + ", "
            + TIMER_INFO + ", " + TIMER_INACTIVE + ", " + TIMER_FAIL + ") values (?,?,?,?,?)";


    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PAGE_TIMER);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAGE_TIMER);
    }

    public static long insert(SQLiteDatabase db, PageTimer timer, String pageId, String lang) throws SQLException {

        SQLiteStatement insertStatement = db.compileStatement(INSERT_SQL);

        if(pageId != null)
            insertStatement.bindString(1, pageId);
        if(lang != null)
            insertStatement.bindString(2, lang);
        if(timer.getInfo() != null)
            insertStatement.bindString(3, timer.getInfo());
        if(timer.getInactive() != null)
            insertStatement.bindString(4, timer.getInactive());
        if(timer.getFail() != null)
            insertStatement.bindString(5, timer.getFail());

        return insertStatement.executeInsert();

//        ContentValues cv = new ContentValues();
//
//        cv.put(PAGE_ID, pageId);
//        cv.put(PAGE_LANGUAGE, lang);
//        cv.put(TIMER_INFO, timer.getInfo());
//        cv.put(TIMER_INACTIVE, timer.getInactive());
//        cv.put(TIMER_FAIL, timer.getFail());
//
//        return db.insert(TABLE_PAGE_TIMER, null, cv);
    }


    public static PageTimer retrieve(SQLiteDatabase db, String pageId, String lang) throws SQLException {
        PageTimer timer = null;

        Cursor c = db.query(TABLE_PAGE_TIMER, null, PAGE_ID + "=? AND " + PAGE_LANGUAGE + "=?", new String[]{pageId, lang}, null, null, null);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            String info = c.getString(c.getColumnIndex(TIMER_INFO));
            String inactive = c.getString(c.getColumnIndex(TIMER_INACTIVE));
            String fail = c.getString(c.getColumnIndex(TIMER_FAIL));
            timer = new PageTimer(info, inactive, fail);
        }
        c.close();
        return timer;
    }


    public static long update(SQLiteDatabase db, PageTimer timer, String pageId, String lang) throws SQLException {

        ContentValues cv = new ContentValues();

        cv.put(TIMER_INFO, timer.getInfo());
        cv.put(TIMER_INACTIVE, timer.getInactive());
        cv.put(TIMER_FAIL, timer.getFail());

        return db.update(TABLE_PAGE_TIMER, cv, PAGE_ID + "=? AND " + PAGE_LANGUAGE + "=?", new String[]{pageId, lang});
    }


    public static boolean isExist(SQLiteDatabase db, String pageId, String lang) throws SQLException {
        boolean itemExist = false;

        Cursor c = db.query(TABLE_PAGE_TIMER, null, PAGE_ID + "=? AND " + PAGE_LANGUAGE + "=?", new String[]{pageId, lang}, null, null, null);

        if ((c != null) && (c.getCount() > 0)) {
            itemExist = true;
        }
        c.close();
        return itemExist;
    }

    public static int delete(SQLiteDatabase db, String pageId, String lang){
        return db.delete(TABLE_PAGE_TIMER, PAGE_ID + "=? AND " + PAGE_LANGUAGE + "=?", new String[]{pageId, lang});
    }
}
