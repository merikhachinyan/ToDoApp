package com.example.meri.todoapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.example.meri.todoapp.R;
import com.example.meri.todoapp.fragments.TodoListFragment;
import com.example.meri.todoapp.holder.TodoViewHolder;
import com.example.meri.todoapp.item.TodoItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TodoAdapter extends RecyclerView.Adapter<TodoViewHolder> {

    private static List<TodoItem>  mTodoList = new ArrayList<>();

    private OnAdapterItemsListener mOnAdapterItemsListener;

    private SparseBooleanArray mSelectedItems;

    static int count = 0;

    public TodoAdapter() {
        mSelectedItems = new SparseBooleanArray();
    }

    @Override
    public TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_item, parent, false);

        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TodoViewHolder holder, final int position) {

        final TodoItem item = mTodoList.get(position);

        holder.getTitle().setText(item.getTitle());
        holder.getDescription().setText(item.getDescription());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(item.getDate());

        holder.getDate().setText(dateFormat().format(calendar.getTime()));

        if(TodoListFragment.isSelected){
            holder.getSelection().setVisibility(View.VISIBLE);
            holder.getSelection().setChecked(mSelectedItems.get(holder.getAdapterPosition()));
        } else {
            holder.getSelection().setChecked(false);
            holder.getSelection().setVisibility(View.GONE);
        }

        mSelectedItems.put(holder.getAdapterPosition(), holder.getSelection().isChecked());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TodoItem todo = mTodoList.get(holder.getAdapterPosition());
                mOnAdapterItemsListener.onOpenItem(todo, holder.getAdapterPosition());
            }
        });

        holder.getSelection().setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        mSelectedItems.put(holder.getAdapterPosition(),
                                holder.getSelection().isChecked());

                        if(count != 0){
                            mOnAdapterItemsListener.onCheckBoxClick();
                        }
                        count++;
                    }
                });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                holder.getSelection().setVisibility(View.VISIBLE);
                holder.getSelection().setChecked(true);

                mOnAdapterItemsListener.onItemLongClick(holder.getSelection(),
                        holder.getAdapterPosition(), holder.getSelection().isChecked());
                return true;
            }
        });

//        holder.getDeleteItem().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mOnDeleteItemListener.onDeleteItem(holder.getAdapterPosition());
//            }
//        });

        holder.getMore().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnAdapterItemsListener.onPopupMenuClick(view, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTodoList.size();
    }

    private SimpleDateFormat dateFormat(){
        String dateFormat = "dd MMM yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                dateFormat, Locale.US);

        return simpleDateFormat;
    }

    public void removeSelection(){
        mSelectedItems.clear();
        notifyDataSetChanged();
    }

    public void selectOrDeselect(int position, boolean isChecked){
        mSelectedItems.put(position, isChecked);
    }

    public int getCheckedItemsCount(){
        int count = 0;

        for (int i = 0; i < getSelectedItemsCount(); i++){
            if(mSelectedItems.valueAt(i) == true){
                count++;
            }
        }
        return count;
    }

    public int getSelectedItemsCount(){
        return mSelectedItems.size();
    }

    public void addTodoItem(TodoItem todoItem){
        mTodoList.add(todoItem);
    }


    List<TodoItem> list = new ArrayList<>();

    public void removeTodoItems() {
        list.clear();
        for (int i = 0; i < mSelectedItems.size(); i++) {
            if (!mSelectedItems.valueAt(i)) {
                list.add(mTodoList.get(mSelectedItems.keyAt(i)));
            }
        }
        mTodoList.clear();
        mTodoList = list;
        notifyDataSetChanged();
    }

    public void setTodoItem(TodoItem todoItem, int position){
        mTodoList.set(position, todoItem);
    }

    public void removeItem(int position){
        mTodoList.remove(position);
    }

    public interface OnAdapterItemsListener{
        void onItemLongClick(View view, int position, boolean isChecked);
        void onPopupMenuClick(View view, int position);
        void onOpenItem(TodoItem todoItem, int position);
        //        void onDeleteItem(int position);
        void onCheckBoxClick();
    }

    public void setOnAdapterItemsListener(OnAdapterItemsListener adapterItemsListener){
        mOnAdapterItemsListener = adapterItemsListener;
    }

    public void clearSelection() {
        mSelectedItems.clear();
    }
}
