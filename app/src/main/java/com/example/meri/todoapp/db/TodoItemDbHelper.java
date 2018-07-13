package com.example.meri.todoapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.meri.todoapp.db.TodoItemContract.*;

public class TodoItemDbHelper extends SQLiteOpenHelper{
    private static final String DB_NAME = "todo.db";
    private static final int DB_VERSION = 1;

    public TodoItemDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override

    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TODO_ITEMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private static final String SQL_CREATE_TODO_ITEMS =
            "CREATE TABLE " + TodoEntry.TABLE_NAME + " (" +
            TodoEntry._ID + " INTEGER PRIMARY KEY," +
            TodoEntry.TITLE + " TEXT NOT NULL," +
            TodoEntry.DESCRIPTION + " TEXT NOT NULL," +
            TodoEntry.DATE + " DATE NOT NULL," +
            TodoEntry.REMINDER + " BOOLEAN," +
            TodoEntry.REPEAT + " BOOLEAN," +
            TodoEntry.REPEAT_PERIOD + " INTEGER," +
            TodoEntry.PRIORITY + " INTEGER)";
}
