package com.example.meri.todoapp.item;

import java.io.Serializable;

public class TodoItem implements Serializable{

    private long id;
    private String title;
    private String description;
    private long date;

    private boolean isCheckedReminder;
    private boolean isCheckedRepeat;

    private int checkedRadioId;
    private int priority;

    public TodoItem(long id, String title, String description, long date,
                    boolean isCheckedReminder, boolean isCheckedRepeat,
                    int checkedRadioId, int priority){

        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.isCheckedReminder = isCheckedReminder;
        this.isCheckedRepeat = isCheckedRepeat;
        this.checkedRadioId = checkedRadioId;
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public long getDate() {
        return date;
    }

    public boolean isCheckedReminder() {
        return isCheckedReminder;
    }

    public boolean isCheckedRepeat() {
        return isCheckedRepeat;
    }

    public int getCheckedRadioId() {
        return checkedRadioId;
    }

    public int getPriority() {
        return priority;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setCheckedReminder(boolean checkedReminder) {
        isCheckedReminder = checkedReminder;
    }

    public void setCheckedRepeat(boolean checkedRepeat) {
        isCheckedRepeat = checkedRepeat;
    }

    public void setCheckedRadioId(int checkedRadioId) {
        this.checkedRadioId = checkedRadioId;
    }

    public long getId() {
        return id;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
