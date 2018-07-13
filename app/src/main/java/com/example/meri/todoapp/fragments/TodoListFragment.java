package com.example.meri.todoapp.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.meri.todoapp.R;
import com.example.meri.todoapp.adapter.TodoAdapter;
import com.example.meri.todoapp.db.TodoItemContract;
import com.example.meri.todoapp.item.TodoItem;
import com.example.meri.todoapp.provider.TodoItemsProvider;

import java.util.List;

public class TodoListFragment extends Fragment implements View.OnClickListener{

    private final String SELECTED_ITEMS = " Selected items";
    private final String DIALOG_MESSAGE = "Are you sure?";
    private final String DIALOG_ACCEPT = "Yes";
    private final String DIALOG_REJECT = "Cancel";

    private FloatingActionButton mButtonAdd;
    private RecyclerView mRecyclerView;
    private TodoAdapter mTodoAdapter;
    private ActionMode mActionMode;

//    private DbManager mDbManager;

    private OnFragmentActionListener mOnActionOpen;

    public static boolean isSelected = false;

    public TodoListFragment() {
    }

    ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            MenuInflater inflater = actionMode.getMenuInflater();
            inflater.inflate(R.menu.multi_select, menu);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch(menuItem.getItemId()){
                case R.id.delete:
                    isSelected = false;
                    removeSelectedItems();
                    changeAdapterItems();
                    clearSelection();
                    finishActionMode();
                    
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            mActionMode = null;
            isSelected = false;
            mTodoAdapter.clearSelection();
            mTodoAdapter.notifyDataSetChanged();
        }
    };

    private TodoAdapter.OnAdapterItemsListener mOnAdapterItemsListener =
            new TodoAdapter.OnAdapterItemsListener() {
                @Override
                public void onItemLongClick(View view, int position, boolean isChecked) {

                    mTodoAdapter.selectOrDeselect(position, isChecked);

                    if(mActionMode == null){
                        mActionMode = getActivity().startActionMode(mActionModeCallback);
                        ((CheckBox) view).setChecked(isChecked);

                        isSelected = true;
                        mActionMode.setTitle(String.valueOf(mTodoAdapter.getCheckedItemsCount()) +
                                SELECTED_ITEMS);
                    }
                    mTodoAdapter.notifyDataSetChanged();
                }

                @Override
                public void onPopupMenuClick(View view, final long id) {
                    PopupMenu popup = new PopupMenu(getActivity(), view);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()){
                                case R.id.popup_delete:
                                    openDialog(id).show();
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.popup_menu, popup.getMenu());
                    popup.show();
                }

                @Override
                public void onCheckBoxClick(){
                    if(mActionMode != null){
                        mActionMode.setTitle(String.valueOf(mTodoAdapter.getCheckedItemsCount()) +
                                SELECTED_ITEMS);
                    }
                }

                @Override
                public void onOpenItem(long id){
                    mOnActionOpen.onItemClick(id);
                }

            };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_todo_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
        setListeners();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mOnActionOpen = (OnFragmentActionListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException("Not implemented");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_todo_list_add_item:
                isSelected = false;
                mTodoAdapter.clearSelection();

                mOnActionOpen.onAddClick();
                break;
        }
    }

    private Dialog openDialog(final long id){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(DIALOG_MESSAGE)
                .setPositiveButton(DIALOG_ACCEPT, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //mDbManager.deleteTodoItem(id);

                        String selection = TodoItemContract.TodoEntry._ID + "=?";
                        String[] selectionArgs = {Long.toString(id)};
                        Uri uri = ContentUris.withAppendedId(Uri
                                .parse(TodoItemsProvider.CONTENT_URI_TODO_ITEM), id);

                        getActivity().getContentResolver()
                                .delete(uri, selection, selectionArgs);
                        changeAdapterItems();
                    }
                })
                .setNegativeButton(DIALOG_REJECT, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        return builder.create();
    }

    private void init(View view){
        mButtonAdd = view.findViewById(R.id.button_todo_list_add_item);
        mRecyclerView = view.findViewById(R.id.recyclerView);

        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        //mDbManager = new DbManager(getActivity());
        //mTodoAdapter.createTodoList(mDbManager.getTodoItems());

        Cursor cursor = getActivity().getContentResolver()
                .query(Uri.parse(TodoItemsProvider.CONTENT_URI_TODO_ITEMS),
                        null,
                        null,
                        null,
                        null,
                        null);

        mTodoAdapter = new TodoAdapter();

        if(cursor != null){
            while (cursor.moveToNext()) {

                long mId = cursor.getLong(cursor.getColumnIndex(TodoItemContract.TodoEntry._ID));
                String title = cursor.getString(cursor.getColumnIndex(TodoItemContract.TodoEntry.TITLE));
                String description = cursor.getString(cursor.getColumnIndex(TodoItemContract.TodoEntry.DESCRIPTION));
                long date = cursor.getLong(cursor.getColumnIndex(TodoItemContract.TodoEntry.DATE));
                boolean isCheckedReminder = cursor.getInt(cursor.getColumnIndex(TodoItemContract.TodoEntry.REMINDER)) > 0;
                boolean isCheckedRepeat = cursor.getInt(cursor.getColumnIndex(TodoItemContract.TodoEntry.REPEAT)) > 0;
                int repeatPeriod = cursor.getInt(cursor.getColumnIndex(TodoItemContract.TodoEntry.REPEAT_PERIOD));
                int priority = cursor.getInt(cursor.getColumnIndex(TodoItemContract.TodoEntry.PRIORITY));

                mTodoAdapter.addTodoItem(new TodoItem(mId, title, description, date, isCheckedReminder, isCheckedRepeat,
                        repeatPeriod, priority));
                mTodoAdapter.notifyDataSetChanged();
            }
        }
        cursor.close();

        mRecyclerView.setAdapter(mTodoAdapter);
    }

    private void setListeners(){
        mButtonAdd.setOnClickListener(this);
        mTodoAdapter.setOnAdapterItemsListener(mOnAdapterItemsListener);
    }

    private void removeSelectedItems(){
        List<Long> idList = mTodoAdapter.getSelectedItemsId();
        for (int i = 0; i < idList.size(); i++){
            String selection = TodoItemContract.TodoEntry._ID + "=?";
            String[] selectionArgs = {Long.toString(idList.get(i))};
            //mDbManager.deleteTodoItem(idList.get(i));
            Uri uri = ContentUris.withAppendedId(Uri
                    .parse(TodoItemsProvider.CONTENT_URI_TODO_ITEM), idList.get(i));

            getActivity().getContentResolver()
                    .delete(uri, selection, selectionArgs);
        }
    }

    private void changeAdapterItems(){
        mTodoAdapter.clearList();

        Cursor cursor = getActivity().getContentResolver()
                .query(Uri.parse(TodoItemsProvider.CONTENT_URI_TODO_ITEMS),
                        null,
                        null,
                        null,
                        null,
                        null);

        if(cursor != null){
            while (cursor.moveToNext()) {

                long mId = cursor.getLong(cursor.getColumnIndex(TodoItemContract.TodoEntry._ID));
                String title = cursor.getString(cursor.getColumnIndex(TodoItemContract.TodoEntry.TITLE));
                String description = cursor.getString(cursor.getColumnIndex(TodoItemContract.TodoEntry.DESCRIPTION));
                long date = cursor.getLong(cursor.getColumnIndex(TodoItemContract.TodoEntry.DATE));
                boolean isCheckedReminder = cursor.getInt(cursor.getColumnIndex(TodoItemContract.TodoEntry.REMINDER)) > 0;
                boolean isCheckedRepeat = cursor.getInt(cursor.getColumnIndex(TodoItemContract.TodoEntry.REPEAT)) > 0;
                int repeatPeriod = cursor.getInt(cursor.getColumnIndex(TodoItemContract.TodoEntry.REPEAT_PERIOD));
                int priority = cursor.getInt(cursor.getColumnIndex(TodoItemContract.TodoEntry.PRIORITY));

                mTodoAdapter.addTodoItem(new TodoItem(mId, title, description, date, isCheckedReminder, isCheckedRepeat,
                        repeatPeriod, priority));
                mTodoAdapter.notifyDataSetChanged();
            }
        }
        cursor.close();

//        mTodoAdapter.createTodoList();
//        mTodoAdapter.createTodoList(mDbManager.getTodoItems());
    }
    
    private void clearSelection(){
        mTodoAdapter.removeSelection();
        mTodoAdapter.notifyDataSetChanged();
    }
    
    private void finishActionMode(){
        mActionMode.finish();
        mActionMode = null;
    }

    public void setOnActionListener(OnFragmentActionListener onActionOpen){
        mOnActionOpen = onActionOpen;
    }

    public interface OnFragmentActionListener{
        void onAddClick();
        void onItemClick(long id);
    }
}