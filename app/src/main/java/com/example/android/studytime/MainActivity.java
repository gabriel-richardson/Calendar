package com.example.android.studytime;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.tyczj.extendedcalendarview.Day;
import com.tyczj.extendedcalendarview.Event;
import com.tyczj.extendedcalendarview.ExtendedCalendarView;


public class MainActivity extends AppCompatActivity {

    private ExtendedCalendarView calendar;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        calendar = (ExtendedCalendarView) findViewById(R.id.calendar);
        calendar.setGesture(ExtendedCalendarView.LEFT_RIGHT_GESTURE);
        calendar.setWeekdayTextBackgroundColor();

        final Event event = new Event();
        event.setDescription("fuck");

        calendar.setOnDayClickListener(new ExtendedCalendarView.OnDayClickListener() {
            @Override
            public void onDayClicked(AdapterView<?> adapter, View view, int position, long id, Day day) {
                if (day.getNumOfEvents() != 0) {
                    intent = new Intent(MainActivity.this, Pop.class);
                    intent.putExtra("Day", day.getDay());
                    intent.putExtra("Month", day.getMonth());
                    intent.putExtra("Year", day.getYear());
                    startActivity(intent);
                } else {
                    intent = new Intent(MainActivity.this, AddEvent.class);
                    intent.putExtra("Day", day.getDay());
                    intent.putExtra("Month", day.getMonth());
                    intent.putExtra("Year", day.getYear());
                    startActivity(intent);
                    onPause();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        calendar.refreshCalendar();
    }
}