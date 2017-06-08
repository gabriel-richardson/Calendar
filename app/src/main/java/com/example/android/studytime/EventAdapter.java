package com.example.android.studytime;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tyczj.extendedcalendarview.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Gabriel Richardson on 8/8/2016.
 */
public class EventAdapter extends BaseAdapter {
    ArrayList<Event> events;
    Context context;
    private static LayoutInflater inflater = null;
    private static final SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a");

    public EventAdapter(Pop pop, ArrayList<Event> events) {
        this.events = events;
        context = pop;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder {
        TextView tv1;
        TextView tv2;
        TextView tv3;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.event_list, null);
        holder.tv1 = (TextView) rowView.findViewById(R.id.textView1);
        holder.tv2 = (TextView) rowView.findViewById(R.id.textView2);
        holder.tv3 = (TextView) rowView.findViewById(R.id.textView3);

        holder.tv1.setText(events.get(position).getTitle());
        if(events.get(position).isSameDay())
            holder.tv2.setText(events.get(position).timeToString());
        else
            holder.tv2.setText(events.get(position).popDateToString());

        if(events.get(position).getLocation().equals("")) {
            holder.tv3.setVisibility(View.GONE);
            int leftPadding = context.getResources().getDimensionPixelSize(R.dimen.left_padding_pop_up);
            int bottomPadding = context.getResources().getDimensionPixelSize(R.dimen.bottom_padding_pop_up);
            holder.tv2.setPadding(leftPadding, 0, 0, bottomPadding);
        }
        else
            holder.tv3.setText(events.get(position).getLocation());
        return rowView;
    }
}
