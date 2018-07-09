package com.example.meri.todoapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.meri.todoapp.fragments.TodoItemFragment;
import com.example.meri.todoapp.fragments.TodoListFragment;
import com.example.meri.todoapp.item.TodoItem;

public class MainActivity extends AppCompatActivity
        implements TodoListFragment.OnFragmentActionListener,
        TodoItemFragment.OnFragmentActionListener{

    private final String TODO_ITEM_TAG = "Todo Item";
    private final String TODO_LIST_TAG = "Items List";

    private boolean isEdited = false;

    private int itemPosition;

    private TodoListFragment mTodoListFragment;
    private TodoItemFragment mTodoItemFragment;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTodoListFragment = new TodoListFragment();
        mFragmentManager = getFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.add(R.id.frame, mTodoListFragment, TODO_LIST_TAG);
        mFragmentTransaction.commit();

        mTodoListFragment.setOnActionListener(this);

        mTodoItemFragment = new TodoItemFragment();
        mTodoItemFragment.setOnActionListener(this);
    }

    @Override
    public void OnItemSave(TodoItem todoItem) {
        if(isEdited){
            mTodoListFragment.setTodoItem(todoItem, itemPosition);
        } else {
            mTodoListFragment.addTodoItem(todoItem);
        }
        getFragmentManager().popBackStack();
    }

    @Override
    public void onAddClick() {
        isEdited = false;
        mTodoItemFragment = TodoItemFragment.newInstance(null);

        mFragmentManager = getFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.frame, mTodoItemFragment, TODO_ITEM_TAG);
        mFragmentTransaction.addToBackStack(TODO_ITEM_TAG);
        mFragmentTransaction.commit();
    }

    @Override
    public void onItemClick(TodoItem todoItem, int position){
        itemPosition = position;
        isEdited = true;

        mTodoItemFragment = TodoItemFragment.newInstance(todoItem);

        mFragmentManager = getFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.frame, mTodoItemFragment, TODO_ITEM_TAG);
        mFragmentTransaction.addToBackStack(TODO_ITEM_TAG);
        mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        mFragmentTransaction.commit();
    }
}

