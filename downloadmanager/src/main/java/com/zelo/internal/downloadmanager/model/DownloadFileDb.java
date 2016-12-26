package com.zelo.internal.downloadmanager.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.zelo.internal.downloadmanager.core.DownloadConfiguration;
import com.zelo.internal.downloadmanager.core.DownloadStatus;

import java.io.File;

/**
 * Created by mohan on 6/10/16.
 */

public class DownloadFileDb extends SQLiteOpenHelper {

    private SQLiteDatabase db;
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "downloadfile.db";
    public static final String TABLE_DOWNLOAD = "downloads_table";
    public static final String DOWNLOAD_ID = "_id";
    public static final String DOWNLOAD_KEY = "key";

    public static final String DOWNLOAD_URL = "download_url";
    public static final String DOWNLOAD_START = "download_start";
    public static final String DOWNLOAD_END = "download_end";

    public static final String DOWNLOAD_PROGRESS = "download_progress";
    public static final String DOWNLOAD_STATUS = "download_status";

    public static final String DOWNLOAD_FILE_DIR = "download_directory";
    public static final String DOWNLOAD_FILE_NAME = "download_file_name";


    public static final String CREATE_DOWNLOADS_TABLE ="CREATE TABLE "
            + TABLE_DOWNLOAD + " ("
            + DOWNLOAD_ID + " INTEGER PRIMARY KEY, "
            + DOWNLOAD_KEY + " text, "
            + DOWNLOAD_URL + " text, "
            + DOWNLOAD_START + " integer, "
            + DOWNLOAD_END + " long, "
            + DOWNLOAD_PROGRESS + " long,"
            + DOWNLOAD_FILE_DIR + " text, "
            + DOWNLOAD_FILE_NAME + " text, "
            + DOWNLOAD_STATUS + " integer);";

    public DownloadFile getDownloadFile(@NonNull String mKey){

      Cursor cursor= db.query(TABLE_DOWNLOAD,null,DOWNLOAD_KEY+"=?",new String[]{mKey},null,null,null);
        DownloadFile downloadFile;
        if(cursor.moveToFirst()){

            downloadFile=  new DownloadFile(cursor.getInt(cursor.getColumnIndex(DOWNLOAD_ID)),
            cursor.getString(cursor.getColumnIndex(DOWNLOAD_KEY)),
            cursor.getString(cursor.getColumnIndex(DOWNLOAD_URL)),
            cursor.getLong(cursor.getColumnIndex(DOWNLOAD_START)),
            cursor.getLong(cursor.getColumnIndex(DOWNLOAD_END)),
            cursor.getLong(cursor.getColumnIndex(DOWNLOAD_PROGRESS)));

            downloadFile.setDownloadConfiguration(new DownloadConfiguration.Builder().setmURL(cursor.getString(cursor.getColumnIndex(DOWNLOAD_KEY)))
                    .setmName(cursor.getString(cursor.getColumnIndex(DOWNLOAD_FILE_NAME)))
                    .setmFolder(new File(cursor.getString(cursor.getColumnIndex(DOWNLOAD_FILE_DIR)))).build());
            downloadFile.setStatus(cursor.getInt(cursor.getColumnIndex(DOWNLOAD_STATUS)));
            return downloadFile;
        }
        return null;
    }

    public void insert(DownloadFile downloadFile){

        DownloadConfiguration downloadConfiguration=downloadFile.getDownloadConfiguration();
        ContentValues contentValues=new ContentValues();
        contentValues.put(DOWNLOAD_KEY,downloadFile.getTag());
        contentValues.put(DOWNLOAD_URL,downloadFile.getUri());
        contentValues.put(DOWNLOAD_START,downloadFile.getStart());
        contentValues.put(DOWNLOAD_END,downloadFile.getEnd());
        contentValues.put(DOWNLOAD_PROGRESS,downloadFile.getFinished());
        contentValues.put(DOWNLOAD_FILE_NAME,downloadConfiguration.getmName().toString());
        contentValues.put(DOWNLOAD_FILE_DIR,downloadConfiguration.getmFolder().getAbsolutePath());  //
        contentValues.put(DOWNLOAD_STATUS,downloadFile.getStatus());
        db.insert(TABLE_DOWNLOAD,null,contentValues);
    }

    public void update(DownloadFile downloadFile,String mKey){
        DownloadConfiguration downloadConfiguration=downloadFile.getDownloadConfiguration();
        ContentValues contentValues=new ContentValues();
        contentValues.put(DOWNLOAD_KEY,downloadFile.getTag());
        contentValues.put(DOWNLOAD_URL,downloadFile.getUri());
        contentValues.put(DOWNLOAD_START,downloadFile.getStart());
        contentValues.put(DOWNLOAD_END,downloadFile.getEnd());
        contentValues.put(DOWNLOAD_PROGRESS,downloadFile.getFinished());
        contentValues.put(DOWNLOAD_FILE_NAME,downloadConfiguration.getmName().toString());
        contentValues.put(DOWNLOAD_FILE_DIR,downloadConfiguration.getmFolder().getAbsolutePath());  //
        contentValues.put(DOWNLOAD_STATUS,downloadFile.getStatus());
        db.update(TABLE_DOWNLOAD,contentValues,DOWNLOAD_KEY+"=?",new String[]{mKey});
    }

    public void xyz(){
      Cursor cursor=  db.query(TABLE_DOWNLOAD,null,DOWNLOAD_STATUS+"=?",new String[]{""+DownloadStatus.STATUS_FAILED},null,null,null);
        if(cursor.moveToFirst()){
          DownloadFile downloadFile=  getDownloadFile(cursor.getString(cursor.getColumnIndex(DOWNLOAD_KEY)));
        }
    }


    public void initialize() {
        db = this.getWritableDatabase();
    }

    private static DownloadFileDb dbInstance;

    public static synchronized DownloadFileDb getInstance(Context context) {
        if (dbInstance == null) {
            dbInstance = new DownloadFileDb(context);
            dbInstance.initialize();
        }
        return dbInstance;
    }

    public DownloadFileDb(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_DOWNLOADS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXIST "+TABLE_DOWNLOAD);
    }

    public void delete(String key) {
        db.delete(TABLE_DOWNLOAD,DOWNLOAD_KEY+"=?",new String[]{key});
    }

    public void deleteFiles() {
        Cursor cursor=db.query(TABLE_DOWNLOAD,null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            for (int i = 0; i < cursor.getCount(); i++) {
                File file = new File(cursor.getString(cursor.getColumnIndex(DOWNLOAD_FILE_DIR)), cursor.getString(cursor.getColumnIndex(DOWNLOAD_FILE_NAME)));
                if (file.exists() && file.isFile()) {
                    file.delete();
                }
                cursor.moveToNext();
            }
        }
        db.delete(TABLE_DOWNLOAD,null,null);

    }
}
