package com.example.meri.todoapp.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
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

import com.example.meri.todoapp.R;
import com.example.meri.todoapp.adapter.TodoAdapter;
import com.example.meri.todoapp.item.TodoItem;

public class TodoListFragment extends Fragment implements View.OnClickListener{

    private final String SELECTED_ITEMS = " Selected items";
    private final String DIALOG_MESSAGE = "Are you sure?";
    private final String DIALOG_ACCEPT = "Yes";
    private final String DIALOG_REJECT = "Cancel";

    private FloatingActionButton mButtonAdd;
    private RecyclerView mRecyclerView;
    private TodoAdapter mTodoAdapter;
    private ActionMode mActionMode;

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
                    mTodoAdapter.removeTodoItems();
                    mTodoAdapter.removeSelection();
                    mTodoAdapter.notifyDataSetChanged();
                    mActionMode.finish();
                    mActionMode = null;
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
                public void onPopupMenuClick(View view, final int position) {
                    PopupMenu popup = new PopupMenu(getActivity(), view);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()){
                                case R.id.popup_delete:
                                    openDialog(position).show();
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
                public void onOpenItem(TodoItem todoItem, int position){
                    mOnActionOpen.onItemClick(todoItem, position);
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

    private Dialog openDialog(final int position){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(DIALOG_MESSAGE)
                .setPositiveButton(DIALOG_ACCEPT, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        removeTodoItem(position);
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

        mTodoAdapter = new TodoAdapter();
        mRecyclerView.setAdapter(mTodoAdapter);
    }

    private void setListeners(){
        mButtonAdd.setOnClickListener(this);
        mTodoAdapter.setOnAdapterItemsListener(mOnAdapterItemsListener);
    }

    public void addTodoItem(TodoItem item){
        mTodoAdapter.addTodoItem(item);
        mTodoAdapter.notifyDataSetChanged();
    }

    public void setTodoItem(TodoItem item, int position){
        mTodoAdapter.setTodoItem(item, position);
        mTodoAdapter.notifyDataSetChanged();
    }

    private void removeTodoItem(int position){
        mTodoAdapter.removeItem(position);
        mTodoAdapter.notifyDataSetChanged();
    }

    public void setOnActionListener(OnFragmentActionListener onActionOpen){
        mOnActionOpen = onActionOpen;
    }

    public interface OnFragmentActionListener{
        void onAddClick();
        void onItemClick(TodoItem todoItem, int position);
    }
}

