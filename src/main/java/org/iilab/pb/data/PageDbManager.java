package org.iilab.pb.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;


import java.util.ArrayList;
import java.util.List;

import org.iilab.pb.common.AppConstants;
import org.iilab.pb.model.Page;
import org.iilab.pb.model.PageAction;
import org.iilab.pb.model.PageChecklist;
import org.iilab.pb.model.PageItem;
import org.iilab.pb.model.PageStatus;
import org.iilab.pb.model.PageTimer;

/**
 * Created by aoe on 1/5/14.
 */
public class PageDbManager {

    private static final String TAG = PageDbManager.class.getSimpleName();

    private static final String TABLE_PAGE = "page_table";

    private static final String PAGE_ID = "page_id";
    private static final String PAGE_LANGUAGE = "page_language";
    private static final String PAGE_TYPE = "page_type";
    private static final String PAGE_TITLE = "page_title";
    private static final String PAGE_INTRODUCTION = "page_introduction";
    private static final String PAGE_WARNING = "page_warning";
    private static final String PAGE_COMPONENT = "page_component";
    private static final String PAGE_CONTENT = "page_content";
    private static final String PAGE_SUCCESS_ID = "page_success_id";
    private static final String PAGE_FAILED_ID = "page_failed_id";
    private static final String PAGE_HEADING = "page_heading";
    private static final String PAGE_SECTION_ORDER = "page_section_order";

    private static final String CREATE_TABLE_WIZARD_PAGE = "create table " + TABLE_PAGE + " ( "
            + AppConstants.TABLE_PRIMARY_KEY + " integer primary key autoincrement, " + PAGE_ID + " text, " + PAGE_LANGUAGE + " text, "
            + PAGE_TYPE + " text, " + PAGE_TITLE + " text, " + PAGE_INTRODUCTION + " text, " + PAGE_WARNING + " text, " + PAGE_COMPONENT + " text, "
            + PAGE_CONTENT + " text, " + PAGE_SUCCESS_ID + " text, " + PAGE_FAILED_ID + " text, " + PAGE_HEADING + " text, " + PAGE_SECTION_ORDER + " text);";

    private static final String INSERT_SQL = "insert into  " + TABLE_PAGE + " (" + PAGE_ID + ", " + PAGE_LANGUAGE + ", " + PAGE_TYPE + ", "
            + PAGE_TITLE + ", " + PAGE_INTRODUCTION + ", " + PAGE_WARNING + ", " + PAGE_COMPONENT + ", " + PAGE_CONTENT + ", " + PAGE_SUCCESS_ID
            + ", " + PAGE_FAILED_ID + ", " + PAGE_HEADING + ", " + PAGE_SECTION_ORDER + ") values (?,?,?,?,?,?,?,?,?,?,?,?)";

    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_WIZARD_PAGE);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAGE);
    }


    public static long insert(SQLiteDatabase db, Page page) throws SQLException {

        SQLiteStatement insertStatement = db.compileStatement(INSERT_SQL);

        if(page.getId() != null)
            insertStatement.bindString(1, page.getId());
        if(page.getLang() != null)
            insertStatement.bindString(2, page.getLang());
        if(page.getType() != null)
            insertStatement.bindString(3, page.getType());
        if(page.getTitle() != null)
            insertStatement.bindString(4, page.getTitle());
        if(page.getIntroduction() != null)
            insertStatement.bindString(5, page.getIntroduction());
        if(page.getWarning() != null)
            insertStatement.bindString(6, page.getWarning());
        if(page.getComponent() != null)
            insertStatement.bindString(7, page.getComponent());
        if(page.getContent() != null)
            insertStatement.bindString(8, page.getContent());
        if(page.getSuccessId() != null)
            insertStatement.bindString(9, page.getSuccessId());
        if(page.getFailedId() != null)
            insertStatement.bindString(10, page.getFailedId());
        if(page.getHeading() != null)
            insertStatement.bindString(11, page.getHeading());
        if(page.getSectionOrder() != null)
            insertStatement.bindString(12, page.getSectionOrder());

        return insertStatement.executeInsert();
    }


    public static Page retrieve(SQLiteDatabase db, String pageId, String lang) throws SQLException {
        Page page = null;

        Cursor c = db.query(TABLE_PAGE, null, PAGE_ID + "=? AND " + PAGE_LANGUAGE + "=?", new String[]{pageId, lang}, null, null, null);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            String pageType = c.getString(c.getColumnIndex(PAGE_TYPE));
            String pageTitle = c.getString(c.getColumnIndex(PAGE_TITLE));
            String pageIntro = c.getString(c.getColumnIndex(PAGE_INTRODUCTION));
            String pageWarning = c.getString(c.getColumnIndex(PAGE_WARNING));
            String pageComponent = c.getString(c.getColumnIndex(PAGE_COMPONENT));
            String pageContent = c.getString(c.getColumnIndex(PAGE_CONTENT));
            String successId = c.getString(c.getColumnIndex(PAGE_SUCCESS_ID));
            String failedId = c.getString(c.getColumnIndex(PAGE_FAILED_ID));
            String heading = c.getString(c.getColumnIndex(PAGE_HEADING));
            String secOrder = c.getString(c.getColumnIndex(PAGE_SECTION_ORDER));

            List<PageStatus> statusList = PageStatusDbManager.retrieve(db, pageId, lang);
            List<PageAction> actionList = PageActionDbManager.retrieve(db, pageId, lang);
            List<PageItem> itemList = PageItemDbManager.retrieve(db, pageId, lang);
            PageTimer timer = PageTimerDbManager.retrieve(db, pageId, lang);
            List<PageChecklist> checkList = PageChecklistDbManager.retrieve(db, pageId, lang);

            page = new Page(pageId, lang, pageType, pageTitle, pageIntro, pageWarning, pageComponent, statusList, actionList,
                    itemList, pageContent, timer, successId, failedId, checkList, heading, secOrder);
        }
        c.close();
        return page;
    }

    public static List<Page> retrieve(SQLiteDatabase db, String lang) throws SQLException {
        List<Page> pageList = new ArrayList<Page>();

        Cursor c = db.query(TABLE_PAGE, null, PAGE_LANGUAGE + "=?", new String[]{lang}, null, null, null);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                String pageId = c.getString(c.getColumnIndex(PAGE_ID));
                String pageType = c.getString(c.getColumnIndex(PAGE_TYPE));
                String pageTitle = c.getString(c.getColumnIndex(PAGE_TITLE));
                String pageIntro = c.getString(c.getColumnIndex(PAGE_INTRODUCTION));
                String pageWarning = c.getString(c.getColumnIndex(PAGE_WARNING));
                String pageComponent = c.getString(c.getColumnIndex(PAGE_COMPONENT));
                String pageContent = c.getString(c.getColumnIndex(PAGE_CONTENT));
                String successId = c.getString(c.getColumnIndex(PAGE_SUCCESS_ID));
                String failedId = c.getString(c.getColumnIndex(PAGE_FAILED_ID));
                String heading = c.getString(c.getColumnIndex(PAGE_HEADING));
                String secOrder = c.getString(c.getColumnIndex(PAGE_SECTION_ORDER));

                List<PageStatus> statusList = PageStatusDbManager.retrieve(db, pageId, lang);
                List<PageAction> actionList = PageActionDbManager.retrieve(db, pageId, lang);
                List<PageItem> itemList = PageItemDbManager.retrieve(db, pageId, lang);
                PageTimer timer = PageTimerDbManager.retrieve(db, pageId, lang);
                List<PageChecklist> checkList = PageChecklistDbManager.retrieve(db, pageId, lang);

                Page page = new Page(pageId, lang, pageType, pageTitle, pageIntro, pageWarning, pageComponent,
                        statusList, actionList, itemList, pageContent, timer, successId, failedId, checkList, heading, secOrder);
                pageList.add(page);
                c.moveToNext();
            }
        }
        c.close();
        return pageList;
    }


    public static long update(SQLiteDatabase db, Page page) throws SQLException {

        ContentValues cv = new ContentValues();

        cv.put(PAGE_TYPE, page.getType());
        cv.put(PAGE_TITLE, page.getTitle());
        cv.put(PAGE_INTRODUCTION, page.getIntroduction());
        cv.put(PAGE_WARNING, page.getWarning());
        cv.put(PAGE_COMPONENT, page.getComponent());
        cv.put(PAGE_CONTENT, page.getContent());
        cv.put(PAGE_SUCCESS_ID, page.getSuccessId());
        cv.put(PAGE_FAILED_ID, page.getFailedId());
        cv.put(PAGE_HEADING, page.getHeading());
        cv.put(PAGE_SECTION_ORDER, page.getSectionOrder());

        return db.update(TABLE_PAGE, cv, PAGE_ID + "=? AND " + PAGE_LANGUAGE + "=?", new String[]{page.getId(), page.getLang()});
    }


    public static boolean isExist(SQLiteDatabase db, String pageId, String lang) throws SQLException {
        boolean itemExist = false;

        Cursor c = db.query(TABLE_PAGE, null, PAGE_ID + "=? AND " + PAGE_LANGUAGE + "=?", new String[]{pageId, lang}, null, null, null);

        if ((c != null) && (c.getCount() > 0)) {
            itemExist = true;
        }
        c.close();
        return itemExist;
    }


    public static void insertOrUpdate(SQLiteDatabase db, Page page){
        if(isExist(db, page.getId(), page.getLang())){
            update(db, page);
        }
        else{
            insert(db, page);
        }
    }


    public static int delete(SQLiteDatabase db, String pageId, String lang){
        return db.delete(TABLE_PAGE, PAGE_ID + "=? AND " + PAGE_LANGUAGE + "=?", new String[]{pageId, lang});
    }
}
