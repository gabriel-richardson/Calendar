package com.example.android.studytime;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tyczj.extendedcalendarview.CalendarProvider;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Gabriel Richardson on 9/18/2016.
 */
public class EditEvent extends AddEvent implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Edit Event");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            event = getIntent().getParcelableExtra("Event");
            startDate = event.getStartDate();
            endDate = event.getEndDate();
        }

//        // Need to create a date from Calendar instance beacause Date is
//        // a terribly designed class. I need to find a better option than
//        // this because this is horribly messy.
        Calendar cal = Calendar.getInstance();
        cal.set(startDate.getYear(), startDate.getMonth(), startDate.getDate());
        Date stringStartDate = cal.getTime();
        cal.set(endDate.getYear(), endDate.getMonth(), endDate.getDate());
        Date stringEndDate = cal.getTime();

        titleEdit = (EditText) findViewById(R.id.eventTitle);
        if(!(event.getTitle().equals("(No Title)")))
            titleEdit.setText(event.getTitle());

        locationEdit = (EditText) findViewById(R.id.location);
        locationEdit.setText(event.getLocation());

        startDateButton = (Button) findViewById(R.id.startDate);
        startDateButton.setText(dateFormatter.format(stringStartDate));

        startTimeButton = (Button) findViewById(R.id.startTime);
        startTimeButton.setText(timeFormatter.format(startDate));

        endDateButton = (Button) findViewById(R.id.endDate);
        endDateButton.setText(dateFormatter.format(stringEndDate));

        endTimeButton = (Button) findViewById(R.id.endTime);
        endTimeButton.setText(timeFormatter.format(endDate));

        descriptionEdit = (EditText) findViewById(R.id.description);
        descriptionEdit.setText(event.getDescription());

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

    public void setEventDetails(){
        event.setStartDayJulian(startDate);
        event.setEndDayJulian(endDate);

        CalendarProvider.DatabaseHelper myDb = new CalendarProvider.DatabaseHelper(EditEvent.this);
        myDb.deleteData(String.valueOf(event.getId()));

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
}
