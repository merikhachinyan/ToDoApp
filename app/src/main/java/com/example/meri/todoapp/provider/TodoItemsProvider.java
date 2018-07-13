package com.example.meri.todoapp.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.meri.todoapp.db.TodoItemContract;
import com.example.meri.todoapp.db.TodoItemDbHelper;

public class TodoItemsProvider extends ContentProvider{

    public static final String AUTHORITY = "com.aca.content.todo.app";
    public static final String PATH_TODO_ITEMS = "todo_items";
    public static final String PATH_TODO_ITEM = "todo_items/#";

    public static final String CONTENT_URI_TODO_ITEMS = "content://" + AUTHORITY +
            "/" + PATH_TODO_ITEMS;
    public static final String CONTENT_URI_TODO_ITEM = "content://" + AUTHORITY +
            "/" + PATH_TODO_ITEM;
    

    public static final int URI_TODO_ITEMS = 1;
    public static final int URI_TODO_ITEM = 2;

    private static final UriMatcher sUriMatcher;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, PATH_TODO_ITEMS, URI_TODO_ITEMS);
        sUriMatcher.addURI(AUTHORITY, PATH_TODO_ITEM, URI_TODO_ITEM);
    }


    private TodoItemDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new TodoItemDbHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s,
                        @Nullable String[] strings1, @Nullable String s1) {

        Cursor cursor = null;

        switch (sUriMatcher.match(uri)){
            case URI_TODO_ITEMS:
                cursor = mDbHelper.getReadableDatabase()
                     .query(TodoItemContract.TodoEntry.TABLE_NAME,
                             null,
                             null,
                             null,
                             null,
                             null,
                             null);
                break;
            case URI_TODO_ITEM:
                break;
            default:
                throw new IllegalStateException("Unknown URI");
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        switch (sUriMatcher.match(uri)){
            case URI_TODO_ITEMS:
                mDbHelper.getWritableDatabase()
                        .insert(TodoItemContract.TodoEntry.TABLE_NAME,
                                null, contentValues);
                break;
            case URI_TODO_ITEM:
                break;
            default:
                throw new IllegalStateException("Unknown URI");
        }
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        switch (sUriMatcher.match(uri)){
            case URI_TODO_ITEMS:
                break;
            case URI_TODO_ITEM:
                int id = mDbHelper.getWritableDatabase()
                        .delete(TodoItemContract.TodoEntry.TABLE_NAME,
                                s, strings);
                return id;
            default:
                throw new IllegalStateException("Unknown URI");
        }
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s,
                      @Nullable String[] strings) {
        switch (sUriMatcher.match(uri)){
            case URI_TODO_ITEMS:
                break;
            case URI_TODO_ITEM:
                int id = mDbHelper.getWritableDatabase()
                        .update(TodoItemContract.TodoEntry.TABLE_NAME,
                                contentValues,
                                s,
                                strings);
                return id;
            default:
                throw new IllegalStateException("Unknown URI");
        }
        return 0;
    }
}
