package com.cs3354group09.calendar_app;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
Class allows for the creation of the calendar views. It parses all the database data and
places them into the calendar views themselves.
 */
public class BaseCalendarAdapter extends BaseAdapter
{
    // Calendar handling, parsing and creation
    private Context context;
    private View previous_view;
    private java.util.Calendar month;
    private ArrayList<String> items;
    private GregorianCalendar selectedDate;

    public GregorianCalendar previousMonth;    //Used to get previous month for getting complete view.
    public GregorianCalendar previousMonthMaxSet;
    public static List<String> day_string;
    public Cursor cursor;
    DBAdapter calendarDB;

    int firstDay;
    int maxWeekNumber;
    int maxP;
    int calMaxP;
    int monthLength;

    String itemValue, currentDateString;
    DateFormat df;

    // Simple format for views of the calendar
    public BaseCalendarAdapter( Context context, GregorianCalendar monthCalendar, Cursor cursor )
    {
        this.cursor = cursor;
        BaseCalendarAdapter.day_string = new ArrayList<>();
        Locale.setDefault(Locale.US);
        month = monthCalendar;
        selectedDate = (GregorianCalendar) monthCalendar.clone();
        this.context = context;
        month.set(GregorianCalendar.DAY_OF_MONTH, 1);

        this.items = new ArrayList<>();
        df = new SimpleDateFormat("yyyyMMdd", Locale.US);
        currentDateString = df.format(selectedDate.getTime());
        refreshDays();

    }

    // Gets all the parsing data for the calendar views
    public int getCount()
    {
        return day_string.size();
    }


    public Object getItem(int position)
    {
        return day_string.get(position);
    }


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
        if ( (Integer.parseInt(gridValue) > 1) && (position < firstDay) )
        {
            dayView.setTextColor( Color.GRAY );
            dayView.setClickable( false );
            dayView.setFocusable( false );
        }
        else if ( (Integer.parseInt(gridValue) < 7) && (position > 28) )
        {
            dayView.setTextColor( Color.GRAY );
            dayView.setClickable( false );
            dayView.setFocusable( false );
        }
        else
        {
            // setting current month's days in white color.
            dayView.setTextColor( Color.WHITE );
        }

        if ( day_string.get(position).equals(currentDateString) )
        {
            v.setBackgroundColor( Color.parseColor("#10253A") );
        }
        else
        {
            v.setBackgroundColor( Color.parseColor("#343434") );
        }

        dayView.setText(gridValue);

        // create date string for comparison
        String date = day_string.get( position );

        if ( date.length() == 1 ) {
            //date = "0" + date;
        }
        String monthStr = "" + (month.get( GregorianCalendar.MONTH ) + 1);
        if ( monthStr.length() == 1 )
        {
            //monthStr = "0" + monthStr;
        }

        setEventView( v, position,dayView );
        return v;
    }

    // Sets selected background views or the default background
    public View setSelected( View view,int pos )
    {
        if ( previous_view != null )
        {
            previous_view.setBackgroundColor(Color.parseColor( "#343434" ) );
        }

        view.setBackgroundColor(Color.parseColor("#6C7E8F"));

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


    public void refreshDays()
    {
        // clear items
        items.clear();
        day_string.clear();
        Locale.setDefault( Locale.US );
        previousMonth = (GregorianCalendar) month.clone();

        // month start day. ie; sun, mon, etc
        firstDay = month.get(GregorianCalendar.DAY_OF_WEEK);

        // finding number of weeks in current month.
        maxWeekNumber = month.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);

        // allocating maximum row number for the GridView.
        monthLength = maxWeekNumber * 7;
        maxP = getMaxDayofPreviousMonth(); // previous month maximum day 31,30....
        calMaxP = maxP - (firstDay - 1);// calendar off day starting 24,25 ...

        //Create calendar instance for complete GridView including previous,current,next dates.
        previousMonthMaxSet = (GregorianCalendar) previousMonth.clone();

        //Set the start date as the previous months max date.
        previousMonthMaxSet.set( GregorianCalendar.DAY_OF_MONTH, calMaxP + 1 );

        //Populate the calender GridView.
        for ( int n = 0; n < monthLength; n++ )
        {
            itemValue = df.format(previousMonthMaxSet.getTime());
            previousMonthMaxSet.add(GregorianCalendar.DATE, 1);
            day_string.add(itemValue);

        }
    }

    // Determines when each month ends in relation to the months before and after
    private int getMaxDayofPreviousMonth()
    {
        int maxPreviousMonth;
        if ( month.get(GregorianCalendar.MONTH ) == month.getActualMinimum(GregorianCalendar.MONTH) )
        {
            previousMonth.set( (month.get(GregorianCalendar.YEAR) - 1), month.getActualMaximum(GregorianCalendar.MONTH), 1 );
        } else {
            previousMonth.set( GregorianCalendar.MONTH, month.get(GregorianCalendar.MONTH) - 1 );
        }
        maxPreviousMonth = previousMonth.getActualMaximum( GregorianCalendar.DAY_OF_MONTH );

        return maxPreviousMonth;
    }

    // Set the event on the calendar view, based on the database fields
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

    // Open database to store data or use data for calendar views
    public void openDB()
    {
        calendarDB = new DBAdapter(context);
        calendarDB.open();
    }

    // The dates hold the individual events, we need to access them and parse them
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

    // Checks if certain days have an event, and allows for further action in different views
    public void checkIfEventDate(final String date,final View view)
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
                        .setPositiveButton("GO TO DAILY VIEW", new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(context, dailyActivityView.class);
                                intent.putExtra("pickedDay", date);
                                context.startActivity(intent);
                            }
                        })
                        .setNegativeButton("CANCEL", null).show();
                break;
            }
            cursor.moveToNext();
        }
    }

}
