package com.example.meri.todoapp.db;

import android.provider.BaseColumns;

public final class TodoItemContract {

    public TodoItemContract() {
    }

    public static class TodoEntry implements BaseColumns {
        public static final String TABLE_NAME = "todo_items";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String DATE = "date";
        public static final String REMINDER = "reminder";
        public static final String REPEAT = "repeat";
        public static final String REPEAT_PERIOD = "repeat_period";
        public static final String PRIORITY = "priority";
    }
}
