package com.example.android.studytime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tyczj.extendedcalendarview.CalendarProvider;
import com.tyczj.extendedcalendarview.Event;

/**
 * Created by Gabriel Richardson on 8/9/2016.
 */
public class DetailedEvent extends AppCompatActivity {

    private CalendarProvider.DatabaseHelper myDb;
    Event event;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_event_view);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            event = getIntent().getParcelableExtra("Event");
        }

        TextView titleView = (TextView) findViewById(R.id.title);
        titleView.setText(event.getTitle());

        TextView timeView = (TextView) findViewById(R.id.time);
        if(event.isSameDay())
            timeView.setText(event.shortDateToString() + event.timeToString());
        else
            timeView.setText(event.longDateToString());

        TextView locationHeader = (TextView) findViewById(R.id.locationHeader);
        TextView locationView = (TextView) findViewById(R.id.location);
        if(event.getLocation() == null || event.getLocation().equals("")) {
            locationView.setVisibility(View.GONE);
            locationHeader.setVisibility(View.GONE);
        }
        else
            locationView.setText(event.getLocation());

        TextView descriptionHeader = (TextView) findViewById(R.id.descriptionHeader);
        TextView descriptionView = (TextView) findViewById(R.id.description);
        if(event.getDescription() == null || event.getDescription().equals("")) {
            descriptionView.setVisibility(View.GONE);
            descriptionHeader.setVisibility(View.GONE);
        }
        else
            descriptionView.setText(event.getDescription());

        Button edit = (Button) findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailedEvent.this, EditEvent.class);
                intent.putExtra("Event", event);
                startActivity(intent);
                finish();
            }
        });

        Button delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDb = new CalendarProvider.DatabaseHelper(DetailedEvent.this);
                Integer deletedRows = myDb.deleteData(String.valueOf(event.getId()));
                if(deletedRows > 0)
                    Toast.makeText(DetailedEvent.this, "Event Deleted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(DetailedEvent.this, "Event Not Deleted", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}