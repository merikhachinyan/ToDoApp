package com.example.meri.todoapp.helper;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class ViewHelper {

    public static String getEditTextValue(EditText textView){
        return textView.getText().toString();
    }

    public static void setTextViewValue(TextView textView, String text){
        textView.setText(text);
    }

    public static void setEditTextValue(EditText textView, String text){
        textView.setText(text);
    }

    public static boolean getCheckBox(CheckBox checkBox){
        return checkBox.isChecked();
    }

    public static int getRadioButtonId(RadioGroup radioGroup){
        return radioGroup.getCheckedRadioButtonId();
    }

    public static void setCheckBox(CheckBox checkBox, boolean flag){
        checkBox.setChecked(flag);
    }

    public static void setRadioGroup(RadioGroup radioGroup, int id){
        radioGroup.check(id);
    }

    public static void setViewNumber(TextView textView, int number){
        textView.setText(Integer.toString(number));
    }

    public static void disableView(View view){
        view.setEnabled(false);
    }

    public static void enableView(View view){
        view.setEnabled(true);
    }
}
