package com.example.android.studytime;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.tyczj.extendedcalendarview.CalendarProvider;
import com.tyczj.extendedcalendarview.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by Gabriel Richardson on 6/10/2016.
 */
public class Pop extends Activity {
    private int day;
    private int month;
    private int year;
    ArrayList<Event> events;
    Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        createPopUp();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_up_window);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            day = getIntent().getIntExtra("Day", 0);
            month = getIntent().getIntExtra("Month", 0);
            year = getIntent().getIntExtra("Year", 0);
        }

        events = new ArrayList<>();

        cal = Calendar.getInstance();
        cal.set(year, month, day);
        Date date = cal.getTime();
        TextView text = (TextView) findViewById(R.id.date);
        SimpleDateFormat longDateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy");
        String longFormattedDate = longDateFormatter.format(date);
        text.setText(longFormattedDate);

        setDetails();
        ListView lv = (ListView) findViewById(R.id.list);
        lv.setAdapter(new EventAdapter(Pop.this, events));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Pop.this, DetailedEvent.class);
                intent.putExtra("Event", events.get(position));
                intent.putExtra("Day", day);
                intent.putExtra("Month", month);
                intent.putExtra("Year", year);
                startActivity(intent);
                finish();
            }
        });


        Button addEvent = (Button) findViewById(R.id.addEvent);
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Pop.this, AddEvent.class);
                intent.putExtra("Day", day);
                intent.putExtra("Month", month);
                intent.putExtra("Year", year);
                startActivity(intent);
                finish();
            }
        });
    }

    public void setDetails(){

        ContentResolver cr = getContentResolver();
        String[] projection = new String[]{CalendarProvider.EVENT, CalendarProvider.LOCATION, CalendarProvider.DESCRIPTION,
                CalendarProvider.START, CalendarProvider.END, CalendarProvider.START_DAY, CalendarProvider.END_DAY, CalendarProvider.ID};
        String selection = null;
        String[] selectionArguments = null;
        String sortOrder = null;

        Uri uri = CalendarProvider.CONTENT_URI;

        Cursor cursor = cr.query(uri, projection, selection, selectionArguments, sortOrder);

        //populate ArrayLists
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            if (isInDayRange(cursor.getString(5), cursor.getString(6))) {
                Event event = new Event();
                event.setId(Integer.parseInt(cursor.getString(7)));
                event.setTitle(cursor.getString(0));
                event.setStartTime(Long.parseLong(cursor.getString(3)));
                event.setEndTime(Long.parseLong(cursor.getString(4)));
                event.setStartDate(new Date(Long.parseLong(cursor.getString(3))));
                event.setEndDate(new Date(Long.parseLong(cursor.getString(4))));
                event.setLocation(cursor.getString(1));
                event.setDescription(cursor.getString(2));
                events.add(event);
            }
            cursor.moveToNext();
        }
    }

    /**
     * Checks if selected date is between an event's start and end date
     * @param start
     *      julian day of event start day (cursor.getString(5))
     * @param end
     *      julian day of event end day (cursor.getString(6))
     * @return
     *      returns true if currentDay is between start and end
     */
    public boolean isInDayRange(String start, String end) {
        int startDay = Integer.parseInt(start);
        int endDay = Integer.parseInt(end);
        //julian day of selected date
        int currentDay = Time.getJulianDay(cal.getTimeInMillis(),
                TimeUnit.MILLISECONDS.toSeconds(TimeZone.getDefault().getOffset(cal.getTimeInMillis())));

        if (startDay <= currentDay && currentDay <= endDay) {
            return true;
        }
        return false;
    }

    public void createPopUp(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.widthPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * .6));
    }
}