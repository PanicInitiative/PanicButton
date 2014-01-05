package com.apb.beacon.data;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.apb.beacon.model.LocalCachePage;
import com.apb.beacon.model.WizardPage;
import com.apb.beacon.model.WizardPageAction;
import com.apb.beacon.model.WizardPageItem;
import com.apb.beacon.model.WizardPageStatus;

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
    private static final int DATABASE_VERSION = 2;


    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context ctx) {
            super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
//            LocalCacheDbManager.createTable(db);
            WizardPageDbManager.createTable(db);
            WizardPageActionDbManager.createTable(db);
            WizardPageItemDbManager.createTable(db);
            WizardPageActionDbManager.createTable(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//            LocalCacheDbManager.dropTable(db);
            WizardPageDbManager.dropTable(db);
            WizardPageActionDbManager.dropTable(db);
            WizardPageItemDbManager.dropTable(db);
            WizardPageActionDbManager.dropTable(db);

            onCreate(db);
        }
    }

    /** Constructor */
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


    public void insertOrUpdateLocalCachePage(LocalCachePage page){
        LocalCacheDbManager.insertOrUpdate(this.db, page);
    }

    public LocalCachePage retrievePage(int pageNumber){
        return LocalCacheDbManager.retrievePage(this.db, pageNumber);
    }

    public int deletePage(int pageNumber){
        return LocalCacheDbManager.deletePage(this.db, pageNumber);
    }

    public void insertOrUpdateWizardPage(WizardPage page){
        WizardPageDbManager.insertOrUpdate(this.db, page);

        for(WizardPageStatus status : page.getStatus())
            insertOrUpdateWizardPageStatus(status, page.getId(), page.getLang());

        for(WizardPageAction action : page.getAction())
            insertOrUpdateWizardPageAction(action, page.getId(), page.getLang());

        for(WizardPageItem item : page.getItems())
            insertOrUpdateWizardPageItem(item, page.getId(), page.getLang());
    }


    public WizardPage retrievePage(String pageId, String lang){
        return WizardPageDbManager.retrieve(this.db, pageId, lang);
    }

    public List<WizardPage> retrievePages(String lang){
        return WizardPageDbManager.retrieve(this.db, lang);
    }

    public void insertOrUpdateWizardPageAction(WizardPageAction action, String pageId, String lang){
        WizardPageActionDbManager.insertOrUpdate(this.db, action, pageId, lang);
    }

    public List<WizardPageAction> retrieveWizardPageAction(String pageId, String lang){
        return WizardPageActionDbManager.retrieve(this.db, pageId, lang);
    }

    public void insertOrUpdateWizardPageItem(WizardPageItem item, String pageId, String lang){
        WizardPageItemDbManager.insertOrUpdate(this.db, item, pageId, lang);
    }

    public List<WizardPageItem> retrieveWizardPageItem(String pageId, String lang){
        return WizardPageItemDbManager.retrieve(this.db, pageId, lang);
    }

    public void insertOrUpdateWizardPageStatus(WizardPageStatus status, String pageId, String lang){
        WizardPageStatusDbManager.insertOrUpdate(this.db, status, pageId, lang);
    }

    public List<WizardPageStatus> retrieveWizardPageStatus(String pageId, String lang){
        return WizardPageStatusDbManager.retrieve(this.db, pageId, lang);
    }


}
