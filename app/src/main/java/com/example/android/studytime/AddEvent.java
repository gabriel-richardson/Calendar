package com.example.android.studytime;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.tyczj.extendedcalendarview.CalendarProvider;
import com.tyczj.extendedcalendarview.Event;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Gabriel Richardson on 6/10/2016.
 */
public class AddEvent extends AppCompatActivity implements View.OnClickListener {

    Event event;
    static Date startDate;
    static Date endDate;

    EditText titleEdit;
    EditText locationEdit;
    static Button startDateButton;
    static Button startTimeButton;
    static Button endDateButton;
    static Button endTimeButton;
    EditText descriptionEdit;
    Button addEventButton;
    Button cancelEventButton;
    DatePickerDialogFragment datePickerDialogFragment;
    TimePickerDialogFragment timePickerDialogFragment;

    static final SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a");
    static final SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Add Event");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        startDate = new Date(0);
        endDate = new Date(0);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Calendar cal = Calendar.getInstance();
            startDate.setDate(getIntent().getIntExtra("Day", 0));
            startDate.setMonth(getIntent().getIntExtra("Month", 0));
            startDate.setYear(getIntent().getIntExtra("Year", 0));
            startDate.setHours(cal.get(Calendar.HOUR_OF_DAY));
            startDate.setMinutes(0);

            endDate.setDate(getIntent().getIntExtra("Day", 0));
            endDate.setMonth(getIntent().getIntExtra("Month", 0));
            endDate.setYear(getIntent().getIntExtra("Year", 0));
            endDate.setHours(startDate.getHours()+1);
            endDate.setMinutes(0);

            event = new Event(startDate, endDate);
        }

        titleEdit = (EditText) findViewById(R.id.eventTitle);
        locationEdit = (EditText) findViewById(R.id.location);

        startDateButton = (Button) findViewById(R.id.startDate);
        startDateButton.setText(dateFormatter.format(getStringDate(startDate)));

        startTimeButton = (Button) findViewById(R.id.startTime);
        startTimeButton.setText(timeFormatter.format(startDate));

        endDateButton = (Button) findViewById(R.id.endDate);
        endDateButton.setText(dateFormatter.format(getStringDate(startDate)));

        endTimeButton = (Button) findViewById(R.id.endTime);
        endTimeButton.setText(timeFormatter.format(endDate));

        descriptionEdit = (EditText) findViewById(R.id.description);

        addEventButton = (Button) findViewById(R.id.addEvent);
        cancelEventButton = (Button) findViewById(R.id.cancelEvent);

        startDateButton.setOnClickListener(this);
        startTimeButton.setOnClickListener(this);
        endDateButton.setOnClickListener(this);
        endTimeButton.setOnClickListener(this);
        addEventButton.setOnClickListener(this);
        cancelEventButton.setOnClickListener(this);

        datePickerDialogFragment = new DatePickerDialogFragment();
        timePickerDialogFragment = new TimePickerDialogFragment();
    }

    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.startDate:
                datePickerDialogFragment.setFlag(DatePickerDialogFragment.START_DATE);
                datePickerDialogFragment.show(getSupportFragmentManager(), "datePicker");
                break;
            case R.id.endDate:
                datePickerDialogFragment.setFlag(DatePickerDialogFragment.END_DATE);
                datePickerDialogFragment.show(getSupportFragmentManager(), "datePicker");
                break;
            case R.id.startTime:
                timePickerDialogFragment.setFlag(TimePickerDialogFragment.START_TIME);
                timePickerDialogFragment.show(getSupportFragmentManager(), "timePicker");
                break;
            case R.id.endTime:
                timePickerDialogFragment.setFlag(TimePickerDialogFragment.END_TIME);
                timePickerDialogFragment.show(getSupportFragmentManager(), "timePicker");
                break;
            case R.id.cancelEvent:
                finish();
                break;
            case R.id.addEvent:
                if (titleEdit.getText().toString().equals(""))
                    event.setTitle("(No Title)");
                else
                    event.setTitle(titleEdit.getText().toString());
                event.setLocation(locationEdit.getText().toString());
                event.setDescription(descriptionEdit.getText().toString());
                setEventDetails();
                finish();
                break;
        }
    }

    public void setEventDetails(){
        event.setStartDayJulian(startDate);
        event.setEndDayJulian(endDate);

        ContentValues values = new ContentValues();
        values.put(CalendarProvider.DESCRIPTION, event.getDescription());
        values.put(CalendarProvider.LOCATION, event.getLocation());
        values.put(CalendarProvider.EVENT, event.getTitle());
        values.put(CalendarProvider.START, event.getStartDate().getTime());
        values.put(CalendarProvider.START_DAY, event.getStartDayJulian());
        values.put(CalendarProvider.END, event.getEndDate().getTime());
        values.put(CalendarProvider.END_DAY, event.getEndDayJulian());
        Uri uri = getContentResolver().insert(CalendarProvider.CONTENT_URI, values);
    }


    // Need to return a date from Calendar instance beacause Date is
    // a terribly designed class. I need to find a better option than
    // this because this is horribly messy.
    public static Date getStringDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.set(date.getYear(), date.getMonth(), date.getDate());
        Date stringDate = cal.getTime();
        return stringDate;
    }

    public static class TimePickerDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        public static final int START_TIME = 0;
        public static final int END_TIME = 1;
        public int flag = 0;
        int hour;
        int minute;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            switch (flag) {
                case START_TIME:
                    hour = startDate.getHours();
                    minute = startDate.getMinutes();
                    break;
                case END_TIME:
                    hour = endDate.getHours();
                    minute = endDate.getMinutes();
                    break;
            }
            return new TimePickerDialog(getActivity(), TimePickerDialog.THEME_HOLO_LIGHT, this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void setFlag(int i) {
            flag = i;
        }

        //get event start/end times
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            switch (flag) {
                case START_TIME:
                    //if start time is later than end time, update start time and set end time an hour later than new start time
                    startDate.setHours(hourOfDay);
                    startDate.setMinutes(minute);
                    startTimeButton.setText(timeFormatter.format(startDate));
                    if(!(startDate.before(endDate)) && startDate.getHours() < 23) {
                        endDate.setHours(hourOfDay+1);
                        endDate.setMinutes(minute);
                        endTimeButton.setText(timeFormatter.format(endDate));
                    }
                    else if(!(startDate.before(endDate)) && startDate.getHours() >= 23) {
                        Toast.makeText(getContext(), "End date is set to the next day.", Toast.LENGTH_SHORT).show();
                        endDate.setHours(hourOfDay+1);
                        endDate.setMinutes(minute);
                        endTimeButton.setText(timeFormatter.format(endDate));
                        endDate.setDate(endDate.getDate()+1);
                        endDateButton.setText(dateFormatter.format(getStringDate(endDate)));
                    }
                    else {
                        endDate.setMinutes(minute);
                        endTimeButton.setText(timeFormatter.format(endDate));
                    }
                    break;
                case END_TIME:
                    //if end time is earlier than start time, don't update anything and display Toast
                    Date date = new Date(endDate.getYear(), endDate.getMonth(), endDate.getDate(), hourOfDay, minute);
                    date.setHours(hourOfDay);
                    date.setMinutes(minute);
                    if (date.before(startDate)) {

                        Toast.makeText(getContext(), "End time must be later than start time.", Toast.LENGTH_SHORT).show();
                    } else {
                        //else set end time
                        endDate.setHours(hourOfDay);
                        endDate.setMinutes(minute);
                        endTimeButton.setText(timeFormatter.format(endDate));
                    }
                    break;
            }
        }
    }

    //create DatePickerDialog
    public static class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        public static final int START_DATE = 0;
        public static final int END_DATE = 1;
        public int flag = 0;
        int year;
        int month;
        int day;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            switch (flag) {
                case START_DATE:
                    year = startDate.getYear();
                    month = startDate.getMonth();
                    day = startDate.getDate();
                    break;
                case END_DATE:
                    year = endDate.getYear();
                    month = endDate.getMonth();
                    day = endDate.getDate();
                    break;
            }

            return new DatePickerDialog(getActivity(), DatePickerDialog.THEME_HOLO_LIGHT, this, year, month, day);
        }

        public void setFlag(int i) {
            flag = i;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            switch (flag) {
                case START_DATE:
                    startDate.setDate(dayOfMonth);
                    startDate.setMonth(monthOfYear);
                    startDate.setYear(year);
                    startDateButton.setText(dateFormatter.format(getStringDate(startDate)));
                    //if start date is after end date, set end date to new start date
                    if (startDate.after(endDate)) {
                        endDateButton.setText(dateFormatter.format(getStringDate(startDate)));
                        endDate.setDate(dayOfMonth);
                        endDate.setMonth(monthOfYear);
                        endDate.setYear(year);
                    }
                    break;
                case END_DATE:
                    //if end date is before start date, don't update anything and display Toast
                    Date date = new Date(year, monthOfYear, dayOfMonth, endDate.getHours(), endDate.getMinutes());
                    if (date.before(startDate))
                        Toast.makeText(getContext(), "End date must be later than start date.", Toast.LENGTH_SHORT).show();
                    else {
                        //else set end date
                        endDate.setDate(dayOfMonth);
                        endDate.setMonth(monthOfYear);
                        endDate.setYear(year);
                        endDateButton.setText(dateFormatter.format(getStringDate(endDate)));
                    }
                    break;
            }
        }
    }
}