package com.example.meri.todoapp.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.meri.todoapp.R;


public class TodoViewHolder extends RecyclerView.ViewHolder {

    private TextView title;
    private TextView description;
    private TextView date;
    private CheckBox mSelection;
    private ImageView more;


    public TodoViewHolder(View view){
        super(view);

        title = view.findViewById(R.id.item_text_title);
        description = view.findViewById(R.id.item_text_description);
        date = view.findViewById(R.id.item_text_date);
        mSelection = view.findViewById(R.id.item_check_select_item);
        more = view.findViewById(R.id.item_image_popup_menu);
    }

    public TextView getTitle() {
        return title;
    }

    public TextView getDescription() {
        return description;
    }

    public TextView getDate() {
        return date;
    }

    public ImageView getMore() {
        return more;
    }

    public CheckBox getSelection(){
        return mSelection;
    }
}