package com.example.folomeev.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "meetingflow.db";
    private static final int DATABASE_VERSION = 2;
    public static final String TABLE_MEETINGS = "meetings";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_DATE = "date";

    public static final String TABLE_TASKS = "tasks";
    public static final String COLUMN_TASK_ID = "id";
    public static final String COLUMN_TASK_MEETING_ID = "meeting_id";
    public static final String COLUMN_TASK_TEXT = "task_text";
    public static final String COLUMN_TASK_IS_DONE = "is_done";

    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createMeetingsTable = "CREATE TABLE " + TABLE_MEETINGS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " + COLUMN_CATEGORY + " TEXT, " +
                COLUMN_DATE + " TEXT" + ")";
        db.execSQL(createMeetingsTable);

        String createTasksTable = "CREATE TABLE " + TABLE_TASKS + " (" +
                COLUMN_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TASK_MEETING_ID + " INTEGER, " +
                COLUMN_TASK_TEXT + " TEXT, " +
                COLUMN_TASK_IS_DONE + " INTEGER" + ")";
        db.execSQL(createTasksTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEETINGS);
        onCreate(db);
    }
    public void addMeeting(Meeting meeting){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_TITLE, meeting.getTitle());
        values.put(COLUMN_CATEGORY, meeting.getCategory());
        values.put(COLUMN_DATE,meeting.getDate());

        db.insert(TABLE_MEETINGS,null,values);
        db.close();
    }


    public List<Meeting> getAllMeetings() {
        List<Meeting> meetingList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MEETINGS, null);

        if (cursor.moveToFirst()) {
            do {

                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String category = cursor.getString(2);
                String date = cursor.getString(3);


                meetingList.add(new Meeting(id, title, category, date));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return meetingList;
    }
    public void updateMeeting(Meeting meeting) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_TITLE, meeting.getTitle());
        values.put(COLUMN_CATEGORY, meeting.getCategory());
        values.put(COLUMN_DATE, meeting.getDate());

        db.update(TABLE_MEETINGS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(meeting.getId())});
        db.close();
    }

    public void deleteMeeting(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MEETINGS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void addTask(int meetingId, String text) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(COLUMN_TASK_MEETING_ID, meetingId);
        v.put(COLUMN_TASK_TEXT, text);
        v.put(COLUMN_TASK_IS_DONE, 0);
        db.insert(TABLE_TASKS, null, v);
        db.close();
    }
    public Cursor getTasksByMeeting(int meetingId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_TASKS + " WHERE " + COLUMN_TASK_MEETING_ID + " = ?",
                new String[]{String.valueOf(meetingId)});
    }

    public void updateTaskStatus(int taskId, boolean isDone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(COLUMN_TASK_IS_DONE, isDone ? 1 : 0);
        db.update(TABLE_TASKS, v, COLUMN_TASK_ID + " = ?", new String[]{String.valueOf(taskId)});
    }

}
