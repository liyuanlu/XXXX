package com.mlly.xxalarm.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by liyuanlu on 2019/1/6.
 * 用于储存便签内容的数据库类
 */
public class NoteDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "notes.db";     //数据库文件名

    private static final int DATABASE_VERSION = 1;              //数据库版本

    public NoteDBHelper( Context context,SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * 创建数据库语句
     * @param sqLiteDatabase 数据库对象
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_NOTE_TABLE = "CREATE TABLE " + NoteContract.NoteEntry.TABLE_NAME + "("
                + NoteContract.NoteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NoteContract.NoteEntry.COLUMN_NOTE_CODE + " TEXT NOT NULL,"
                + NoteContract.NoteEntry.COLUMN_NOTE_SET_TIME + " TEXT NOT NULL,"
                + NoteContract.NoteEntry.COLUMN_NOTE_CONTENT + " TEXT NOT NULL);";
        sqLiteDatabase.execSQL(SQL_CREATE_NOTE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
