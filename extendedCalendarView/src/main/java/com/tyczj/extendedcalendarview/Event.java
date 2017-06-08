package com.tyczj.extendedcalendarview;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.Time;

public class Event implements Parcelable {
	
	private int color;
	private int id;
	private String title;
	private String description;
	private String location;
	private Calendar cal;
	private TimeZone tz;
	private int startDayJulian;
	private int endDayJulian;
	private long startTime;
	private long endTime;
	private Date startDate;
	private Date endDate;

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel out, int flags) {
		out.writeSerializable(startDate);
		out.writeSerializable(endDate);
		out.writeInt(id);
		out.writeString(title);
		out.writeString(description);
		out.writeString(location);
		out.writeLong(startTime);
		out.writeLong(endTime);
	}

	public static final Parcelable.Creator<Event> CREATOR
			= new Parcelable.Creator<Event>() {
		public Event createFromParcel(Parcel in) {
			return new Event(in);
		}

		public Event[] newArray(int size) {
			return new Event[size];
		}
	};

	private Event(Parcel in) {
		startDate = (Date) in.readSerializable();
		endDate = (Date) in.readSerializable();
		id = in.readInt();
		title = in.readString();
		description = in.readString();
		location = in.readString();
		startTime = in.readLong();
		endTime = in.readLong();
	}

	public Event(){

	}

	public Event(Date startDate, Date endDate) {
		this.startDate = startDate;
		this.endDate = endDate;

		cal = Calendar.getInstance();
		tz = TimeZone.getDefault();

		cal.set(startDate.getYear(), startDate.getMonth(), startDate.getDate(), startDate.getHours(), startDate.getMinutes());
		startTime = cal.getTimeInMillis();
		startDayJulian = Time.getJulianDay(startTime, TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(startTime)));

		cal.set(endDate.getYear(), endDate.getMonth(), endDate.getDate(), endDate.getHours(), endDate.getMinutes());
		endTime = cal.getTimeInMillis();
		endDayJulian = Time.getJulianDay(endTime, TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(endTime)));
	}

	public void setStartDayJulian(Date startDate) {
		cal = Calendar.getInstance();
		tz = TimeZone.getDefault();
		cal.set(startDate.getYear(), startDate.getMonth(), startDate.getDate(), startDate.getHours(), startDate.getMinutes());
		startTime = cal.getTimeInMillis();
		startDayJulian = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));
	}

	public void setEndDayJulian(Date endDate) {
		cal = Calendar.getInstance();
		tz = TimeZone.getDefault();
		cal.set(endDate.getYear(), endDate.getMonth(), endDate.getDate(), endDate.getHours(), endDate.getMinutes());
		endTime = cal.getTimeInMillis();
		endDayJulian = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));
	}

	public Boolean isSameDay() {
		if(startDate.getDay() == endDate.getDay())
			return true;
		return false;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public int getStartDayJulian() { return startDayJulian; }

	public int getEndDayJulian() { return endDayJulian; }

	public void setStartTime(long startTime) { this.startTime = startTime; }

	public void setEndTime(long endTime) { this.endTime = endTime; }

	public void setColor(int color){
		this.color = color;
	}

	public int getColor() {
		return color;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}
	
	public void setLocation(String location){
		this.location = location;
	}
	
	public String getLocation(){
		return location;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setId(int id) { this.id = id;}

	public int getId() { return id; }

	public String timeToString() {
		Date strStartDate = dateConverter(startDate);
		Date strEndDate = dateConverter(endDate);
		SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a");
		String str = timeFormatter.format(strStartDate) + " - " + timeFormatter.format(strEndDate);
		return str;
	}

	public String longDateToString() {
		Date strStartDate = dateConverter(startDate);
		Date strEndDate = dateConverter(endDate);
		SimpleDateFormat longDateFormatter = new SimpleDateFormat("EEE, MMM d, h:mm a");
		String str = longDateFormatter.format(strStartDate) + " - " + longDateFormatter.format(strEndDate);
		return str;
	}

	public String popDateToString() {
		Date strStartDate = dateConverter(startDate);
		Date strEndDate = dateConverter(endDate);
		SimpleDateFormat longDateFormatter = new SimpleDateFormat("MMMM d, h:mm a");
		String str = longDateFormatter.format(strStartDate) + " - " + longDateFormatter.format(strEndDate);
		return str;
	}

	public String shortDateToString() {
		Date strStartDate = dateConverter(startDate);
		SimpleDateFormat shortDateFormatter = new SimpleDateFormat("MMMM d, ");
		String str = shortDateFormatter.format(strStartDate);
		return str;
	}

	private Date dateConverter(Date date) {
		cal = Calendar.getInstance();
		cal.set(date.getYear(), date.getMonth(), date.getDate(), date.getHours(), date.getMinutes());
		return cal.getTime();
	}
}
