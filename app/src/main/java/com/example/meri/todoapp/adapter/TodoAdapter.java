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

    private List<TodoItem> mTodoList;
    private SparseBooleanArray mSelectedItems;

    private OnAdapterItemsListener mOnAdapterItemsListener;

    private static int count = 0;

    public TodoAdapter() {
        mTodoList = new ArrayList<>();
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

        if (TodoListFragment.isSelected) {
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
                long id = mTodoList.get(holder.getAdapterPosition()).getId();
                mOnAdapterItemsListener.onOpenItem(id);
            }
        });

        holder.getSelection().setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        mSelectedItems.put(holder.getAdapterPosition(),
                                holder.getSelection().isChecked());

                        if (count != 0) {
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

        holder.getMore().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long id = mTodoList.get(holder.getAdapterPosition()).getId();
                mOnAdapterItemsListener.onPopupMenuClick(view, id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTodoList.size();
    }

    public void createTodoList(List<TodoItem> todoItems){
        mTodoList.addAll(todoItems);
        notifyDataSetChanged();
    }

    public void addTodoItem(TodoItem todoItem){
        mTodoList.add(todoItem);
    }

    public void clearList(){
        mTodoList.clear();
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
            if(mSelectedItems.valueAt(i)){
                count++;
            }
        }
        return count;
    }

    public int getSelectedItemsCount(){
        return mSelectedItems.size();
    }

    public void clearSelection() {
        mSelectedItems.clear();
    }

    public List<Long> getSelectedItemsId(){
        List<Long> idList = new ArrayList<>();

        for (int i = 0; i < mSelectedItems.size(); i++){
            if(mSelectedItems.valueAt(i)){
                idList.add(mTodoList.get(mSelectedItems.keyAt(i)).getId());
            }
        }
        return idList;
    }

    public interface OnAdapterItemsListener{
        void onItemLongClick(View view, int position, boolean isChecked);
        void onPopupMenuClick(View view, long id);
        void onOpenItem(long id);
        void onCheckBoxClick();
    }

    public void setOnAdapterItemsListener(OnAdapterItemsListener adapterItemsListener){
        mOnAdapterItemsListener = adapterItemsListener;
    }
}
