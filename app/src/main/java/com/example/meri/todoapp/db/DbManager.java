package com.example.meri.todoapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.example.meri.todoapp.item.TodoItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.meri.todoapp.db.TodoItemContract.TodoEntry;

public class DbManager {

    private TodoItemDbHelper mTodoItemDbHelper;

    public DbManager(Context context) {
        mTodoItemDbHelper = new TodoItemDbHelper(context);
    }

    public void insertTodoItem(TodoItem todoItem) {

        ContentValues cv = new ContentValues();
        cv.put(TodoEntry.TITLE, todoItem.getTitle());
        cv.put(TodoEntry.DESCRIPTION, todoItem.getDescription());
        cv.put(TodoEntry.DATE, todoItem.getDate());
        cv.put(TodoEntry.REMINDER, todoItem.isCheckedReminder());
        cv.put(TodoEntry.REPEAT, todoItem.isCheckedRepeat());
        cv.put(TodoEntry.REPEAT_PERIOD, todoItem.getCheckedRadioId());
        cv.put(TodoEntry.PRIORITY, todoItem.getPriority());

        mTodoItemDbHelper.getWritableDatabase().insert(TodoEntry.TABLE_NAME, null, cv);
    }

    public void updateTodoItem(long id, TodoItem todoItem) {
        String selection = TodoEntry._ID + "=?";
        String[] selectionArgs = {String.valueOf(id)};

        ContentValues cv = new ContentValues();
        cv.put(TodoEntry.TITLE, todoItem.getTitle());
        cv.put(TodoEntry.DESCRIPTION, todoItem.getDescription());
        cv.put(TodoEntry.DATE, todoItem.getDate());
        cv.put(TodoEntry.REMINDER, todoItem.isCheckedReminder());
        cv.put(TodoEntry.REPEAT, todoItem.isCheckedRepeat());
        cv.put(TodoEntry.REPEAT_PERIOD, todoItem.getCheckedRadioId());
        cv.put(TodoEntry.PRIORITY, todoItem.getPriority());

        mTodoItemDbHelper.getWritableDatabase().update(TodoEntry.TABLE_NAME, cv,
                selection, selectionArgs);
    }

    public List<TodoItem> getTodoItems() {
        List<TodoItem> todoItems = new ArrayList<>();

        Cursor cursor = mTodoItemDbHelper.getReadableDatabase()
                .query(TodoEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndex(TodoEntry._ID));
                String title = cursor.getString(cursor.getColumnIndex(TodoEntry.TITLE));
                String description = cursor.getString(cursor.getColumnIndex(TodoEntry.DESCRIPTION));
                long date = cursor.getLong(cursor.getColumnIndex(TodoEntry.DATE));
                boolean isCheckedReminder = cursor.getInt(cursor.getColumnIndex(TodoEntry.REMINDER)) > 0;
                boolean isCheckedRepeat = cursor.getInt(cursor.getColumnIndex(TodoEntry.REPEAT)) > 0;
                int repeatPeriod = cursor.getInt(cursor.getColumnIndex(TodoEntry.REPEAT_PERIOD));
                int priority = cursor.getInt(cursor.getColumnIndex(TodoEntry.PRIORITY));

                todoItems.add(new TodoItem(id, title, description, date, isCheckedReminder,
                        isCheckedRepeat, repeatPeriod, priority));
            }
        }
        cursor.close();
        return todoItems;
    }

//    public TodoItem getTodoItemById(long id){
//        String selection = TodoEntry._ID + " = ?";
//        String[] selectionArgs = {String.valueOf(id)};
//
//        Cursor cursor = mTodoItemDbHelper.getReadableDatabase()
//                .query(TodoEntry.TABLE_NAME,
//                        null,
//                        selection,
//                        selectionArgs,
//                        null,
//                        null,
//                        null);
//
//        if(cursor != null){
//            cursor.moveToFirst();
//
//            long mId = cursor.getLong(cursor.getColumnIndex(TodoEntry._ID));
//            String title = cursor.getString(cursor.getColumnIndex(TodoEntry.TITLE));
//            String description = cursor.getString(cursor.getColumnIndex(TodoEntry.DESCRIPTION));
//            long date = cursor.getLong(cursor.getColumnIndex(TodoEntry.DATE));
//            boolean isCheckedReminder = cursor.getInt(cursor.getColumnIndex(TodoEntry.REMINDER)) > 0;
//            boolean isCheckedRepeat = cursor.getInt(cursor.getColumnIndex(TodoEntry.REPEAT)) > 0;
//            int repeatPeriod = cursor.getInt(cursor.getColumnIndex(TodoEntry.REPEAT_PERIOD));
//            int priority = cursor.getInt(cursor.getColumnIndex(TodoEntry.PRIORITY));
//
//            return new TodoItem(mId, title, description, date, isCheckedReminder, isCheckedRepeat,
//                    repeatPeriod, priority);
//        }
//        cursor.close();
//
//        return null;
//    }

    public void deleteTodoItem(long id){
        String selection = TodoEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        mTodoItemDbHelper.getWritableDatabase().delete(TodoEntry.TABLE_NAME, selection, selectionArgs);
    }
}
