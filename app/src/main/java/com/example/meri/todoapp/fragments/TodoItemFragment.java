package com.example.meri.todoapp.fragments;


import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.meri.todoapp.R;
import com.example.meri.todoapp.helper.ViewHelper;
import com.example.meri.todoapp.item.TodoItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TodoItemFragment extends Fragment implements View.OnClickListener{

    private final String EMPTY_TITLE = "Title is empty";
    private final String EMPTY_DESCRIPTION = "Description is empty";

    private static final String ITEM = "Todo";

    private final int SAVE_MODE = 0;
    private final int EDIT_MODE = 1;
    private int mMode;

    private EditText mEditTextTitle;
    private EditText mEditTextDescription;

    private TextView mTextViewDate;
    private TextView mTextPriorityNumber;

    private ImageView mImageUpPriority;
    private ImageView mImageDownPriority;

    private CheckBox mCheckReminder;
    private CheckBox mCheckRepeat;

    private RadioGroup mRadioGroup;
    private RadioButton mRadioDaily;
    private RadioButton mRadioWeekly;
    private RadioButton mRadioMonthly;


    private Calendar mCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    TimePickerDialog.OnTimeSetListener time;

    private int mPriorityNumber = 0;

    private String mTitle;
    private String mDescription;
    private Date mDate;

    private boolean isCheckedReminder;
    private boolean isCheckedRepeat;

    private int mCheckedRadioButtonId;

    private TodoItem mTodoItem;
    private OnFragmentActionListener mOnActionSave;

    public TodoItemFragment() {
    }

    public static TodoItemFragment newInstance(TodoItem todoItem){
        TodoItemFragment todoItemFragment = new TodoItemFragment();

        Bundle args = new Bundle();
        args.putSerializable(ITEM, todoItem);
        todoItemFragment.setArguments(args);

        return todoItemFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo_item,
                container, false);

        init(view);
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setListeners();

        getData();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.options_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_item:
                saveData(); //TODO change this method
                return true;
            case R.id.edit_item:
                enable();
                getActivity().invalidateOptionsMenu();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if(mMode == 1){
            menu.findItem(R.id.save_item).setEnabled(false);
        } else if(mMode == 0){
            menu.findItem(R.id.edit_item).setEnabled(false);
        }
    }

    private void getData(){
        mTodoItem = null;
        if(getArguments() != null){
            mTodoItem = (TodoItem)getArguments().getSerializable(ITEM);
            if(mTodoItem == null){
                enable();
            } else {
                disable();
                getItemValues(mTodoItem);
                setViewValues();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.text_todo_item_date:
                new DatePickerDialog(getActivity(), date, mCalendar.
                        get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH)).show();
                new TimePickerDialog(getActivity(), time, mCalendar.
                        get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE),
                        false).show();
                break;
            case R.id.image_todo_item_priority_up:
                if(mPriorityNumber < 5){
                    mPriorityNumber++;
                }
                ViewHelper.setViewNumber(mTextPriorityNumber, mPriorityNumber);
                break;
            case R.id.image_todo_item_priority_down:
                if(mPriorityNumber > 0){
                    mPriorityNumber--;
                }
                ViewHelper.setViewNumber(mTextPriorityNumber, mPriorityNumber);
                break;
            case R.id.check_todo_item_repeat:
                isCheckedRepeat = mCheckRepeat.isChecked();
                showOrHideRadioButtons(isCheckedRepeat, mRadioGroup);
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mOnActionSave = (OnFragmentActionListener)context;
        } catch (ClassCastException e){
            throw new ClassCastException("Not Implemented");
        }
    }

    private void init(View view){
        mEditTextTitle = view.findViewById(R.id.edit_todo_item_title);
        mEditTextDescription = view.findViewById(R.id.edit_todo_item_description);

        mTextViewDate = view.findViewById(R.id.text_todo_item_date);
        mTextPriorityNumber = view.findViewById(R.id.text_todo_item_priority_number);

        mImageUpPriority = view.findViewById(R.id.image_todo_item_priority_up);
        mImageDownPriority = view.findViewById(R.id.image_todo_item_priority_down);

        mCheckReminder = view.findViewById(R.id.check_todo_item_reminder);
        mCheckRepeat = view.findViewById(R.id.check_todo_item_repeat);

        mRadioGroup = view.findViewById(R.id.radio_todo_item);
        mRadioDaily = view.findViewById(R.id.radio_todo_item_daily);
        mRadioWeekly = view.findViewById(R.id.radio_todo_item_weekly);
        mRadioMonthly = view.findViewById(R.id.radio_todo_item_weekly);
    }

    private void setListeners(){
        mTextViewDate.setOnClickListener(this);
        mImageUpPriority.setOnClickListener(this);
        mImageDownPriority.setOnClickListener(this);
        mCheckRepeat.setOnClickListener(this);
        mCheckReminder.setOnClickListener(this);

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year,
                                  int month, int day){

                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, month);
                mCalendar.set(Calendar.DAY_OF_MONTH, day);
                setDate();
            }
        };

        time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mCalendar.set(Calendar.MINUTE, minute);
                setDate();
            }
        };
    }

    private void setDate(){
        String dateFormat = "MM/dd/yy hh:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                dateFormat, Locale.US);

        ViewHelper.setTextViewValue(mTextViewDate, simpleDateFormat
                .format(mCalendar.getTime()));
    }

    private void saveData() {
        mTitle = ViewHelper.getEditTextValue(mEditTextTitle);
        mDescription = ViewHelper.getEditTextValue(mEditTextDescription);
        mDate = mCalendar.getTime();

        isCheckedReminder = ViewHelper.getCheckBox(mCheckReminder);
        isCheckedRepeat = ViewHelper.getCheckBox(mCheckRepeat);

        mCheckedRadioButtonId = ViewHelper.getRadioButtonId(mRadioGroup);

        if (isEmpty(mTitle)) {
            Toast.makeText(getActivity(), EMPTY_TITLE, Toast.
                    LENGTH_SHORT).show();
        } else {
            if (isEmpty(mDescription)) {
                Toast.makeText(getActivity(), EMPTY_DESCRIPTION, Toast.
                        LENGTH_SHORT).show();
            } else {
                mTodoItem = new TodoItem(mTitle, mDescription, mCalendar.getTimeInMillis(),
                        isCheckedReminder, isCheckedRepeat, mCheckedRadioButtonId, mPriorityNumber);

                mOnActionSave.OnItemSave(mTodoItem);
            }
        }
    }

    private boolean isEmpty(String text){
        return text.matches("") ? true : false;
    }

    private void disable(){
        mMode = EDIT_MODE;

        ViewHelper.disableView(mEditTextTitle);
        ViewHelper.disableView(mEditTextDescription);
        ViewHelper.disableView(mTextViewDate);
        ViewHelper.disableView(mCheckReminder);
        ViewHelper.disableView(mCheckRepeat);
        ViewHelper.disableView(mRadioGroup);
        ViewHelper.disableView(mImageUpPriority);
        ViewHelper.disableView(mImageDownPriority);
        ViewHelper.disableView(mTextPriorityNumber);
        ViewHelper.disableView(mRadioDaily);
        ViewHelper.disableView(mRadioWeekly);
        ViewHelper.disableView(mRadioMonthly);
    }

    private void enable(){
        mMode = SAVE_MODE;

        ViewHelper.enableView(mEditTextTitle);
        ViewHelper.enableView(mEditTextDescription);
        ViewHelper.enableView(mTextViewDate);
        ViewHelper.enableView(mCheckReminder);
        ViewHelper.enableView(mCheckRepeat);
        ViewHelper.enableView(mRadioGroup);
        ViewHelper.enableView(mImageUpPriority);
        ViewHelper.enableView(mImageDownPriority);
        ViewHelper.enableView(mTextPriorityNumber);
        ViewHelper.enableView(mRadioDaily);
        ViewHelper.enableView(mRadioWeekly);
        ViewHelper.enableView(mRadioMonthly);
    }

    private void getItemValues(TodoItem todoItem){
        mTitle = todoItem.getTitle();
        mDescription = todoItem.getDescription();
        mCalendar.setTimeInMillis(todoItem.getDate());
        isCheckedReminder = todoItem.isCheckedReminder();
        isCheckedRepeat = todoItem.isCheckedRepeat();
        mCheckedRadioButtonId = todoItem.getCheckedRadioId();
        mPriorityNumber = todoItem.getPriority();
    }

    private void setViewValues(){
        ViewHelper.setEditTextValue(mEditTextTitle, mTitle);
        ViewHelper.setEditTextValue(mEditTextDescription, mDescription);

        setDate();

        ViewHelper.setCheckBox(mCheckReminder, isCheckedReminder);
        ViewHelper.setCheckBox(mCheckRepeat, isCheckedRepeat);

        ViewHelper.setRadioGroup(mRadioGroup, mCheckedRadioButtonId);
        ViewHelper.setViewNumber(mTextPriorityNumber, mPriorityNumber);

        showOrHideRadioButtons(isCheckedRepeat, mRadioGroup);
    }

    void showOrHideRadioButtons(boolean isChecked, RadioGroup rGroup){
        if(isChecked){
            rGroup.setVisibility(View.VISIBLE);
        } else {
            rGroup.setVisibility(View.GONE);
        }
    }

    public void setOnActionListener(OnFragmentActionListener onActionSave){
        mOnActionSave = onActionSave;
    }

    public interface OnFragmentActionListener{
        void OnItemSave(TodoItem todoItem);
    }
}
