package com.cs3354group09.calendar_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/*
This class helps store the data inputted from the user into an acceptable and readable form by the application to
help manipulate the data within the views. It allows for the ability to update the database if needed to extend
other applications.B
 */
public class DBAdapter
{

    private static final String TAG = "DBAdapter"; //used for logging database version changes

    // Field Names:
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_DATE = "date";
    public static final String KEY_DESCRIPTION = "desc";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_START_TIME = "startTime";
    public static final String KEY_END_TIME = "endTime";
    public static final String KEY_COLOR = "color";
    public static final String KEY_MONTH = "month";


    public static final String[] ALL_KEYS = new String[] {KEY_ROWID, KEY_NAME, KEY_DATE, KEY_DESCRIPTION, KEY_LOCATION,
            KEY_START_TIME, KEY_END_TIME, KEY_COLOR, KEY_MONTH};

    // Column Numbers for each Field Name:
    public static final int COL_ROWID = 0;
    public static final int COL_NAME = 1;
    public static final int COL_DATE = 2;
    public static final int COL_DESCRIPTION = 3;
    public static final int COL_LOCATION = 4;
    public static final int COL_START_TIME = 5;
    public static final int COL_END_TIME = 6;
    public static final int COL_COLOR = 7;
    public static final int COL_MONTH = 8;

    // DataBase info:
    public static final String DATABASE_NAME = "CalendarDB";
    public static final String DATABASE_TABLE = "events";
    public static final int DATABASE_VERSION = 4; // The version number must be incremented each time a change to DB structure occurs.

    //SQL statement to create database
    private static final String DATABASE_CREATE_SQL =
            "CREATE TABLE " + DATABASE_TABLE + " (" + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_NAME + " TEXT NOT NULL, " + KEY_DATE + " TEXT, " + KEY_DESCRIPTION + " TEXT, " + KEY_LOCATION + " TEXT, "
                    + KEY_START_TIME + " TEXT, " + KEY_END_TIME + " TEXT, " + KEY_COLOR + " TEXT, " + KEY_MONTH + " TEXT" + ");";

    private final Context context;
    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;


    public DBAdapter(Context ctx)
    {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }

    // Open the database connection.
    public DBAdapter open()
    {
        db = myDBHelper.getWritableDatabase();
        return this;
    }

    // Close the database connection.
    public void close()
    {
        myDBHelper.close();
    }

    // Add a new set of values to be inserted into the database.
    public long insertRow(String name, String date, String description, String location, String startTime, String endTime, String color, String month)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_DATE, date);
        initialValues.put(KEY_DESCRIPTION, description);
        initialValues.put(KEY_LOCATION, location);
        initialValues.put(KEY_START_TIME, startTime);
        initialValues.put(KEY_END_TIME, endTime);
        initialValues.put(KEY_COLOR, color);
        initialValues.put(KEY_MONTH, month);

        // Insert the data into the database.
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    // Delete a row from the database, by rowId (primary key)
    public boolean deleteRow(long rowId)
    {
        String where = KEY_ROWID + "=" + rowId;
        return db.delete(DATABASE_TABLE, where, null) != 0;
    }

    public void deleteAll()
    {
        Cursor c = getAllRows();
        long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
        if (c.moveToFirst())
        {
            do {
                deleteRow(c.getLong((int) rowId));
            } while (c.moveToNext());
        }
        c.close();
    }

    // Return all data in the database.
    public Cursor getAllRows()
    {
        String where = null;
        Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS, where, null, null, null, KEY_DATE, null);
        if (c != null)
        {
            c.moveToFirst();
        }
        return c;
    }

    // Get a specific row (by rowId)
    public Cursor getRow(long rowId)
    {
        String where = KEY_ROWID + "=" + rowId;
        Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null)
        {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getColumnWithDate(String[] condition)
    {
        Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS, KEY_DATE + "= ?", condition, null, null, KEY_DATE, null);
        if (c != null)
        {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getColumnWithMonth(String[] condition)
    {
        Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS, KEY_MONTH + "= ?", condition, null, null, KEY_DATE, null);
        if (c != null)
        {
            c.moveToFirst();
        }
        return c;
    }

    // Change an existing row to be equal to new data.
    public boolean updateRow(long rowId, String name, String date, String description, String location, String startTime, String endTime, String color, String month)
    {
        String where = KEY_ROWID + "=" + rowId;
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_NAME, name);
        newValues.put(KEY_DATE, date);
        newValues.put(KEY_DESCRIPTION, description);
        newValues.put(KEY_LOCATION, location);
        newValues.put(KEY_START_TIME, startTime);
        newValues.put(KEY_END_TIME, endTime);
        newValues.put(KEY_COLOR, color);
        newValues.put(KEY_MONTH, month);

        // Insert it into the database.
        return db.update(DATABASE_TABLE, newValues, where, null) != 0;
    }


    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase _db)
        {
            _db.execSQL(DATABASE_CREATE_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Destroy old database:
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);

            // Recreate new database:
            onCreate(_db);
        }
    }


}

