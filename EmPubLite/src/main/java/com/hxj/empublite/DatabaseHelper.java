package com.hxj.empublite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Process;

import de.greenrobot.event.EventBus;

/**
 * Created by huxj-win7 on 2016/8/11.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="empublite.db";
    private static final int SCHEMA_VERSION = 1;
    private static DatabaseHelper singleton = null;

    synchronized static DatabaseHelper getInstance(Context context) {
        if (singleton == null) {
            singleton = new DatabaseHelper(context.getApplicationContext());
        }
        return singleton;
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE notes (position INTEGER PRIMARY KEY, prose TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new RuntimeException("This should not be called");
    }

    public void loadNote(int position) {
        new LoadThread(position).start();
    }

    public void updateNote(int position, String prose) {
        new UpdateThread(position, prose).start();
    }

    private class LoadThread extends Thread {
        private int position = -1;
        LoadThread(int position) {
            super();
            this.position = position;
        }
        @Override
        public void run() {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

            String[] args = {String.valueOf(position)};
            Cursor c = getReadableDatabase().rawQuery("select prose from notes where position = ? ", args);
            if (c.getCount() > 0) {
                c.moveToFirst();
                EventBus.getDefault().post(new NoteLoadedEvent(position, c.getString(0)));
            }
            c.close();
        }
    }

    private class UpdateThread extends Thread {
        private int position = -1;
        private String prose = null;

        public UpdateThread(int position, String prose) {
            super();
            this.position = position;
            this.prose = prose;
        }

        @Override
        public void run() {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            String[] args = {String.valueOf(position), prose};
            getWritableDatabase().execSQL("insert or replace into notes (position, prose) values(?, ?)", args);
        }
    }
}
