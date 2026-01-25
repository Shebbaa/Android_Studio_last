package com.example.folomeev.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor; // ЭТО ВАЖНО
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList; // ЭТО ВАЖНО
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "meetingflow.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_MEETINGS = "meetings";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_DATE = "date";

    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_MEETINGS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_CATEGORY + " TEXT, " +
                COLUMN_DATE + " TEXT" + ")";
        db.execSQL(createTable);
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


}
