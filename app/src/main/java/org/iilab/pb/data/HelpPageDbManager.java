package org.iilab.pb.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;


import java.util.ArrayList;
import java.util.List;

import org.iilab.pb.common.AppConstants;
import org.iilab.pb.model.HelpPage;
import org.iilab.pb.model.PageItem;

/**
 * Created by aoe on 2/28/14.
 */
public class HelpPageDbManager {

    private static final String TAG = HelpPageDbManager.class.getSimpleName();

    private static final String TABLE_CONTENT_HELP = "content_help_table";

    private static final String PAGE_ID = "page_id";
    private static final String PAGE_LANGUAGE = "page_language";
    private static final String PAGE_TYPE = "page_type";
    private static final String PAGE_TITLE = "page_title";
    private static final String PAGE_HEADING = "page_heading";
//    private static final String PAGE_CATEGORIES = "page_categories";
    private static final String PAGE_SECTION_ORDER = "page_section_order";
//    private static final String PAGE_TOC = "page_toc";
//    private static final String PAGE_IMAGE_TITLE = "page_image_title";
//    private static final String PAGE_IMAGE_CAPTION = "page_image_caption";
//    private static final String PAGE_IMAGE_SRC = "page_image_src";
//    private static final String PAGE_ALERT = "page_alert";
    private static final String PAGE_CONTENT = "page_content";


    private static final String CREATE_TABLE_CONTENT_HELP = "create table " + TABLE_CONTENT_HELP + " ( "
            + AppConstants.TABLE_PRIMARY_KEY + " integer primary key autoincrement, " + PAGE_ID + " text, "
            + PAGE_LANGUAGE + " text, " + PAGE_TYPE + " text, " + PAGE_TITLE + " text, " + PAGE_HEADING + " text, "
            + PAGE_SECTION_ORDER + " text, " + PAGE_CONTENT + " text);";

    private static final String INSERT_SQL = "insert into  " + TABLE_CONTENT_HELP + " (" + PAGE_ID + ", "
            + PAGE_LANGUAGE + ", " + PAGE_TYPE + ", " + PAGE_TITLE + ", " + PAGE_HEADING + ", " + PAGE_SECTION_ORDER
            + ", " + PAGE_CONTENT + ") values (?,?,?,?,?,?,?)";

    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CONTENT_HELP);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTENT_HELP);
    }


    public static long insert(SQLiteDatabase db, HelpPage page) throws SQLException {

        SQLiteStatement insertStatement = db.compileStatement(INSERT_SQL);


        if (page.getId() != null)
            insertStatement.bindString(1, page.getId());
        if (page.getLang() != null)
            insertStatement.bindString(2, page.getLang());
        if (page.getType() != null)
            insertStatement.bindString(3, page.getType());
        if (page.getTitle() != null)
            insertStatement.bindString(4, page.getTitle());
        if (page.getHeading() != null)
            insertStatement.bindString(5, page.getHeading());
        if (page.getSectionOrder() != null)
            insertStatement.bindString(6, page.getSectionOrder());
        if (page.getContent() != null)
            insertStatement.bindString(7, page.getContent());

        return insertStatement.executeInsert();


//        ContentValues cv = new ContentValues();
//
//        cv.put(PAGE_ID, page.getId());
//        cv.put(PAGE_LANGUAGE, page.getLang());
//        cv.put(PAGE_TYPE, page.getType());
//        cv.put(PAGE_TITLE, page.getTitle());
//        cv.put(PAGE_HEADING, page.getHeading());
//        cv.put(PAGE_CATEGORIES, page.getCategories());
//        cv.put(PAGE_SECTION_ORDER, page.getSectionOrder());
//        cv.put(PAGE_TOC, page.getToc());
//        cv.put(PAGE_CONTENT, page.getContent());
//        cv.put(PAGE_ALERT, page.getAlert());
//
//        if(page.getImage() != null){
//            cv.put(PAGE_IMAGE_TITLE, page.getImage().getTitle());
//            cv.put(PAGE_IMAGE_CAPTION, page.getImage().getCaption());
//            cv.put(PAGE_IMAGE_SRC, page.getImage().getSrc());
//        }
//
//        return db.insert(TABLE_CONTENT_HELP, null, cv);
    }


    public static List<HelpPage> retrieve(SQLiteDatabase db, String lang) throws SQLException {
        List<HelpPage> pageList = new ArrayList<HelpPage>();

        Cursor c = db.query(TABLE_CONTENT_HELP, null, PAGE_LANGUAGE + "=?", new String[]{lang}, null, null, null);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                String pageId = c.getString(c.getColumnIndex(PAGE_ID));
                String pageType = c.getString(c.getColumnIndex(PAGE_TYPE));
                String pageTitle = c.getString(c.getColumnIndex(PAGE_TITLE));
                String pageHeading = c.getString(c.getColumnIndex(PAGE_HEADING));
                String pageSectionOrder = c.getString(c.getColumnIndex(PAGE_SECTION_ORDER));
                String pageContent = c.getString(c.getColumnIndex(PAGE_CONTENT));

                List<PageItem> itemList = PageItemDbManager.retrieve(db, pageId, lang);

                HelpPage page = new HelpPage(pageId, lang, pageType, pageTitle, pageHeading,
                        pageSectionOrder, pageContent, itemList);
                pageList.add(page);
                c.moveToNext();
            }
        }
        c.close();
        return pageList;
    }


    public static long update(SQLiteDatabase db, HelpPage page) throws SQLException {

        ContentValues cv = new ContentValues();

        cv.put(PAGE_TYPE, page.getType());
        cv.put(PAGE_TITLE, page.getTitle());
        cv.put(PAGE_HEADING, page.getHeading());
        cv.put(PAGE_SECTION_ORDER, page.getSectionOrder());
        cv.put(PAGE_CONTENT, page.getContent());

        return db.update(TABLE_CONTENT_HELP, cv, PAGE_ID + "=? AND " + PAGE_LANGUAGE + "=?", new String[]{page.getId(), page.getLang()});
    }


    public static boolean isExist(SQLiteDatabase db, String pageId, String lang) throws SQLException {
        boolean itemExist = false;

        Cursor c = db.query(TABLE_CONTENT_HELP, null, PAGE_ID + "=? AND " + PAGE_LANGUAGE + "=?", new String[]{pageId, lang}, null, null, null);

        if ((c != null) && (c.getCount() > 0)) {
            itemExist = true;
        }
        c.close();
        return itemExist;
    }


    public static void insertOrUpdate(SQLiteDatabase db, HelpPage page) {
        if (isExist(db, page.getId(), page.getLang())) {
            update(db, page);
        } else {
            insert(db, page);
        }
    }


    public static int delete(SQLiteDatabase db, String pageId, String lang) {
        return db.delete(TABLE_CONTENT_HELP, PAGE_ID + "=? AND " + PAGE_LANGUAGE + "=?", new String[]{pageId, lang});
    }

}
