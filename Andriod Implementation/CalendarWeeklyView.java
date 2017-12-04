package com.cs3354group09.calendar_app;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/*
This class is a part of the main activity (user side). It uses the calendar adaptors to
help manipulate the data from the database and gregorian calendar to help create the weekly view
This view as long as all the other ones should have the same input data coming from the database
and should have the ability to move in and out of the calendar app with ease.
 */

public class CalendarWeeklyView extends Activity implements OnClickListener
{
    // For the use of creating the weekly view and the parsing and data handling
    // from the database
    public GregorianCalendar calendarMonth, calendarWeek;
    private WeeklyCalendarAdapter cal_adapter;
    private TextView tv_month;
    private ListView calendar_main_list_view;
    private DBAdapter calendarDB;
    private CalendarListAdapter mCalCursorAdapter;
    // Weeks can only have 7 days in them
    String [] days = new String[7];

    String pickedMonth;


    // When the weekly view is clicked, we create an instance of the class
    // and use the adaptors to help manipulate the data to the individual view
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate(savedInstanceState);


        setContentView( R.layout.activity_weekly_view );
        //Set the list view for the main calendar activity.
        calendar_main_list_view = (ListView) findViewById( R.id.list_view_main );

        GregorianCalendar tempCal = (GregorianCalendar)GregorianCalendar.getInstance();
        pickedMonth = android.text.format.DateFormat.format("M", tempCal).toString();

        openDB();
        populateListView();


        Cursor cursor = calendarDB.getAllRows();
        //Setup Calendar.
        calendarMonth = (GregorianCalendar) GregorianCalendar.getInstance();
        calendarWeek = (GregorianCalendar) GregorianCalendar.getInstance();
        calendarWeek.get(Calendar.WEEK_OF_MONTH);
        cal_adapter = new WeeklyCalendarAdapter( this, calendarWeek, cursor );
        tv_month = (TextView) findViewById(R.id.tv_month);
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", calendarMonth));

        //Setup Previous button.
        Button previous = (Button) findViewById( R.id.calendar_button_previous );
        previous.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setPreviousWeek();
                refreshCalendar();
            }
        });

        //Setup Next Button.
        Button next = (Button) findViewById( R.id.calendar_button_next );
        next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setNextWeek();
                refreshCalendar();
            }
        });

        //Set calendar GridView and create onClick to check for event dates on date selected.
        GridView gridview = (GridView) findViewById(R.id.weekly_view_calendar);
        gridview.setAdapter(cal_adapter);
        gridview.setOnItemClickListener( new OnItemClickListener()
        {
            public void onItemClick( AdapterView<?> parent, View v, int position, long id )
            {
                String selectedGridDate = WeeklyCalendarAdapter.day_string.get(position);
                ((WeeklyCalendarAdapter) parent.getAdapter()).setSelected( v,position );
                ((WeeklyCalendarAdapter) parent.getAdapter()).checkIfEventDate( selectedGridDate, v );
            }
        } );
    }

    // Allows for usability when switching functionality
    @Override
    public void onResume()
    {
        super.onResume();
        populateListView();
    }

    // For manipulating the database information to the calendar view
    public void openDB()
    {
        calendarDB = new DBAdapter(this);
        calendarDB.open();
    }


    // Get the starting and ending dates of each week within the month, and
    // the weeks within every other month
    protected void setNextWeek()
    {
        if( calendarWeek.get(GregorianCalendar.WEEK_OF_MONTH) == calendarWeek.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH) )
        {
            setNextMonth();
        }
        calendarWeek.set( GregorianCalendar.WEEK_OF_MONTH, calendarWeek.get(GregorianCalendar.WEEK_OF_MONTH) + 1 );
    }

    protected void setPreviousWeek()
    {
        if( calendarWeek.get(GregorianCalendar.WEEK_OF_MONTH) == calendarWeek.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH) - 1  )
        {
            setPreviousMonth();
        }
        calendarWeek.set( GregorianCalendar.WEEK_OF_MONTH, calendarWeek.get(GregorianCalendar.WEEK_OF_MONTH) - 1 );
    }

    protected void setNextMonth()
    {
        if ( calendarMonth.get(GregorianCalendar.MONTH) == calendarMonth.getActualMaximum(GregorianCalendar.MONTH) )
        {
            calendarMonth.set( (calendarMonth.get(GregorianCalendar.YEAR) + 1 ), calendarMonth.getActualMinimum(GregorianCalendar.MONTH), 1 );
        }
        else
        {
            calendarMonth.set( GregorianCalendar.MONTH, calendarMonth.get(GregorianCalendar.MONTH) + 1 );
        }
    }


    protected void setPreviousMonth()
    {
        if (calendarMonth.get( GregorianCalendar.MONTH) == calendarMonth.getActualMinimum(GregorianCalendar.MONTH) )
        {
            calendarMonth.set( (calendarMonth.get(GregorianCalendar.YEAR) - 1), calendarMonth.getActualMaximum(GregorianCalendar.MONTH), 1 );
        } else
        {
            calendarMonth.set( GregorianCalendar.MONTH, calendarMonth.get(GregorianCalendar.MONTH) - 1 );
        }
    }


    //Function to refresh the calendar page if user clicks on a date.
    public void refreshCalendar()
    {
        cal_adapter.refreshDays();
        cal_adapter.notifyDataSetChanged();
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", calendarMonth));
        pickedMonth = android.text.format.DateFormat.format("M", calendarMonth).toString();
        populateListView();
    }

    // When certain buttons are clicked, different functionality, but
    // allow for more even flow and accessibility from each view.
    @Override
    public void onClick(View v)
    {
        Intent intent;
        switch ( v.getId() )
        {
            case R.id.add_event_button:
                startActivity(new Intent(CalendarWeeklyView.this,addEvent.class));
                break;
            case R.id.daily_view_button:
                intent = new Intent(CalendarWeeklyView.this, dailyActivityView.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                break;
            case R.id.calendar_event_button:
                intent = new Intent(CalendarWeeklyView.this, CalendarMainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
                break;
            case R.id.list_event_button:
                intent = new Intent(CalendarWeeklyView.this, ListViewActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
                break;
            default:
                break;
        }
    }

    // Place the event data into the list view, same data being fed into the views
    public void populateListView()
    {
        //Get info from database.
        String[] condition = {pickedMonth};
        calendar_main_list_view = (ListView) findViewById( R.id.list_view_main_calendar );

        Cursor cursor = calendarDB.getColumnWithMonth(condition);
        cursor.moveToFirst();
        mCalCursorAdapter = new CalendarListAdapter(getBaseContext(), R.layout.list_item, cursor);
        calendar_main_list_view.setAdapter(mCalCursorAdapter);
        calendar_main_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int rowID = Integer.parseInt(view.getTag().toString());
                Intent intent = new Intent(CalendarWeeklyView.this, eventOverview.class);
                intent.putExtra("id", rowID);
                startActivity(intent);
            }
        });


    }
}