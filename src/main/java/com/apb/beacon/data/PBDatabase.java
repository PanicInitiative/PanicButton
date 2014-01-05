package com.apb.beacon.data;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.apb.beacon.model.LocalCachePage;
import com.apb.beacon.model.Page;
import com.apb.beacon.model.PageAction;
import com.apb.beacon.model.PageItem;
import com.apb.beacon.model.PageStatus;

import java.util.List;

/**
 * Created by aoe on 12/12/13.
 */
public class PBDatabase {

    private static final String TAG = PBDatabase.class.getSimpleName();

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Context mContext;

    private static final String DATABASE_NAME = "pb_db";
    private static final int DATABASE_VERSION = 3;


    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context ctx) {
            super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
//            LocalCacheDbManager.createTable(db);
            PageDbManager.createTable(db);
            PageStatusDbManager.createTable(db);
            PageItemDbManager.createTable(db);
            PageActionDbManager.createTable(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//            LocalCacheDbManager.dropTable(db);
            PageDbManager.dropTable(db);
            PageActionDbManager.dropTable(db);
            PageItemDbManager.dropTable(db);
            PageActionDbManager.dropTable(db);

            onCreate(db);
        }
    }

    /**
     * Constructor
     */
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


    public void insertOrUpdateLocalCachePage(LocalCachePage page) {
        LocalCacheDbManager.insertOrUpdate(this.db, page);
    }

    public LocalCachePage retrievePage(int pageNumber) {
        return LocalCacheDbManager.retrievePage(this.db, pageNumber);
    }

    public int deletePage(int pageNumber) {
        return LocalCacheDbManager.deletePage(this.db, pageNumber);
    }

    public void insertOrUpdateWizardPage(Page page) {
        PageDbManager.insertOrUpdate(this.db, page);

        if (page.getStatus() != null) {
            for (PageStatus status : page.getStatus())
                insertOrUpdateWizardPageStatus(status, page.getId(), page.getLang());
        }

        if (page.getAction() != null) {
            for (PageAction action : page.getAction())
                insertOrUpdateWizardPageAction(action, page.getId(), page.getLang());
        }

        if (page.getItems() != null) {
            for (PageItem item : page.getItems())
                insertOrUpdateWizardPageItem(item, page.getId(), page.getLang());
        }
    }


    public Page retrievePage(String pageId, String lang) {
        return PageDbManager.retrieve(this.db, pageId, lang);
    }

    public List<Page> retrievePages(String lang) {
        return PageDbManager.retrieve(this.db, lang);
    }

    public void insertOrUpdateWizardPageAction(PageAction action, String pageId, String lang) {
        PageActionDbManager.insertOrUpdate(this.db, action, pageId, lang);
    }

    public List<PageAction> retrieveWizardPageAction(String pageId, String lang) {
        return PageActionDbManager.retrieve(this.db, pageId, lang);
    }

    public void insertOrUpdateWizardPageItem(PageItem item, String pageId, String lang) {
        PageItemDbManager.insertOrUpdate(this.db, item, pageId, lang);
    }

    public List<PageItem> retrieveWizardPageItem(String pageId, String lang) {
        return PageItemDbManager.retrieve(this.db, pageId, lang);
    }

    public void insertOrUpdateWizardPageStatus(PageStatus status, String pageId, String lang) {
        PageStatusDbManager.insertOrUpdate(this.db, status, pageId, lang);
    }

    public List<PageStatus> retrieveWizardPageStatus(String pageId, String lang) {
        return PageStatusDbManager.retrieve(this.db, pageId, lang);
    }


}
