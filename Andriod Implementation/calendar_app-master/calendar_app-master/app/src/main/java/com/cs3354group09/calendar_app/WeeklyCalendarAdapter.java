package com.cs3354group09.calendar_app;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

/*
This class allows the parsing of the traditional gregorian calendar to extrapolate the data into separable
weeks. It extends the BaseAdapter which creates the days into a monthly extraction, and then this class adds
another level of extraction to by dividing the months into the weeks.
 */
public class WeeklyCalendarAdapter extends BaseAdapter
{

    private Context context;
    private View previous_view;
    private java.util.Calendar month;
    private java.util.Calendar week;
    private GregorianCalendar selectedDate;
    private ArrayList<String> items;


    public GregorianCalendar previousWeek;    //Used to get previous month for getting complete view.
    public GregorianCalendar previousMonthMaxSet;
    public static List<String> day_string;
    public Cursor cursor;
    DBAdapter calendarDB;

    // Used for determining the what makes up a week, in reference to the months
    int firstDay;
    int maxWeekNumber;
    int maxP;
    int calMaxP;
    int monthLength;

    String itemValue, currentDateString;
    DateFormat df;

    // Adapter that parses through the given days to help create the weeks
    public WeeklyCalendarAdapter( Context context, GregorianCalendar weekCalendar, Cursor cursor )
    {
        this.cursor = cursor;
        WeeklyCalendarAdapter.day_string = new ArrayList<>();
        Locale.setDefault(Locale.US);
        week = weekCalendar;
        selectedDate = (GregorianCalendar) weekCalendar.clone();
        this.context = context;
        week.set(GregorianCalendar.DAY_OF_WEEK, 1);

        this.items = new ArrayList<>();
        df = new SimpleDateFormat("yyyyMMdd", Locale.US);
        currentDateString = df.format(selectedDate.getTime());
        refreshDays();

    }

    // Gets the number of events within a day
    public int getCount()
    {
        return day_string.size();
    }

    // Gets the position of each event
    public Object getItem(int position)
    {
        return day_string.get(position);
    }

    // Once the events have been placed, they will return an ID for later use
    public long getItemId(int position)
    {
        return 0;
    }


    // create a new view for each item referenced by the Adapter
    public View getView( int position, View convertView, ViewGroup parent )
    {
        View v = convertView;
        TextView dayView;

        if ( convertView == null )
        { // if it's not recycled, initialize some attributes
            LayoutInflater vi = ( LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE );
            v = vi.inflate( R.layout.cal_item, null );
        }

        dayView = (TextView) v.findViewById( R.id.date );
        String separatedTime = day_string.get(position).substring(6, 8);

        String gridValue = separatedTime.replaceFirst("^0*", "");


        if ( day_string.get(position).equals(currentDateString) )
        {
            v.setBackgroundColor( Color.parseColor("#10253A") );
        }
        else
        {
            v.setBackgroundColor( Color.parseColor("#343434") );
        }

        dayView.setText(gridValue);

        setEventView( v, position,dayView );
        return v;
    }


    public View setSelected( View view,int pos )
    {
        if ( previous_view != null )
        {
            previous_view.setBackgroundColor(Color.parseColor( "#343434" ) );
        }

        view.setBackgroundColor( Color.parseColor("#6C7E8F") );

        int len=day_string.size();
        if ( len>pos )
        {
            if ( !day_string.get(pos).equals( currentDateString ) )
            {
                previous_view = view;
            }
        }
        return view;
    }

    // Whenever the user asks to switch to different weeks, there must be a new set of data to
    // deal with. It refreshes the values and creates a new weekly view based on the parameters
    // given.
    public void refreshDays()
    {
        // clear items
        items.clear();
        day_string.clear();
        Locale.setDefault(Locale.US);
        previousWeek = (GregorianCalendar) week.clone();

        // finding number of weeks in current month.
        maxWeekNumber = week.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);

        //Create calendar instance for complete GridView including previous,current,next dates.
        previousMonthMaxSet = (GregorianCalendar) previousWeek.clone();

        //Set the start date as the previous months max date.
        int delta = -previousMonthMaxSet.get(GregorianCalendar.DAY_OF_WEEK) + 1;
        previousMonthMaxSet.add(GregorianCalendar.DAY_OF_WEEK, delta);

        //Populate the calender GridView.
        for ( int n = 0; n < 7; n++ )
        {
            itemValue = df.format(previousMonthMaxSet.getTime());
            previousMonthMaxSet.add(GregorianCalendar.DATE, 1);
            day_string.add(itemValue);
        }
    }

    // Populate the events within the calendar
    public void setEventView(View v,int pos,TextView txt){

        cursor.moveToFirst();
        while( !cursor.isAfterLast() )
        {
            String date = cursor.getString(cursor.getColumnIndex(DBAdapter.KEY_DATE));
            int len1=day_string.size();
            if (len1>pos)
            {

                if (day_string.get(pos).equals(date))
                {
                    v.setBackgroundColor(Color.parseColor(cursor.getString(cursor.getColumnIndex(DBAdapter.KEY_COLOR))));

                    txt.setTextColor(Color.WHITE);
                }
            }
            cursor.moveToNext();
        }
    }
    // Access and interact with the database
    public void openDB()
    {
        calendarDB = new DBAdapter(context);
        calendarDB.open();
    }

    // Gets event names from the database through the database adaptor
    public String getEventNames(String date)
    {
        String[] condition = {date};
        String returnString = "";
        openDB();
        Cursor tempCursor = calendarDB.getColumnWithDate(condition);

        tempCursor.moveToFirst();
        returnString = "\n" + tempCursor.getString(tempCursor.getColumnIndex(DBAdapter.KEY_NAME)) + "\n";
        while( tempCursor.moveToNext() )
        {
            returnString += tempCursor.getString(tempCursor.getColumnIndex(DBAdapter.KEY_NAME)) + "\n";
        }
        return returnString;
    }

    // Checks if there is are events available on the chosen day
    public void checkIfEventDate(String date,final View view)
    {
        cursor.moveToFirst();
        while( !cursor.isAfterLast() )
        {
            String event_date = cursor.getString(cursor.getColumnIndex(DBAdapter.KEY_DATE));
            String event_names = "";
            if ( date.equals(event_date) )
            {
                event_names = getEventNames(date);
                view.setBackgroundColor(Color.parseColor(cursor.getString(cursor.getColumnIndex(DBAdapter.KEY_COLOR))));

                Toast.makeText(context, "You have event on this date: "+event_date, Toast.LENGTH_LONG).show();
                //Create an alert dialog to display message. TEMPORARY
                //TODO replace with fragment that contains advanced details.
                new AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_menu_my_calendar)
                        .setTitle("Date: " + event_date)
                        .setMessage("\t\t\tEvents On This Day: \n" + event_names)
                        .setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                break;
            }
            cursor.moveToNext();
        }
    }

}
