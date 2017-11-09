package com.cs3354team5.calendar_app;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.cs3354group09.calendar_app.R;

import java.util.GregorianCalendar;

public class CalendarMainActivity extends Activity implements OnClickListener
{
    public GregorianCalendar calendarMonth, calendarMonthCopy;
    private BaseCalendarAdapter cal_adapter;
    private TextView tv_month;
    private DBAdapter calendarDB;


    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate(savedInstanceState);

        setContentView( R.layout.activity_calender_main );
        //Set the list view for the main calendar activity.
        openDB();

        Cursor cursor = calendarDB.getAllRows();
        //Setup Calendar.
        calendarMonth = (GregorianCalendar) GregorianCalendar.getInstance();
        calendarMonthCopy = (GregorianCalendar) calendarMonth.clone();
        cal_adapter = new BaseCalendarAdapter( this, calendarMonth, cursor );
        tv_month = (TextView) findViewById( R.id.tv_month );
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", calendarMonth));

        //Setup Previous button.
        Button previous = (Button) findViewById( R.id.calendar_button_previous );
        previous.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setPreviousMonth();
                refreshCalendar();
            }
        });

        //Setup Next Button.
        Button next = (Button) findViewById( R.id.calendar_button_next );
        next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setNextMonth();
                refreshCalendar();
            }
        });

        //Set calendar GridView and create onClick to check for event dates on date selected.
        GridView gridview = (GridView) findViewById(R.id.group_view_calendar);
        gridview.setAdapter(cal_adapter);
        gridview.setOnItemClickListener( new OnItemClickListener()
        {
            public void onItemClick( AdapterView<?> parent, View v, int position, long id )
            {
                ((BaseCalendarAdapter) parent.getAdapter()).setSelected(v, position);
                String selectedGridDate = BaseCalendarAdapter.day_string.get(position);

                String separatedTime = selectedGridDate.substring(6, 8);
                String gridValueString = separatedTime.replaceFirst("^0*","");
                int gridValue = Integer.parseInt(gridValueString);

                if ( (gridValue > 10) && (position < 8) )
                {
                    setPreviousMonth();
                    refreshCalendar();
                } else if ( (gridValue < 7) && (position > 28) )
                {
                    setNextMonth();
                    refreshCalendar();
                }
                ((BaseCalendarAdapter) parent.getAdapter()).setSelected( v,position );

                ((BaseCalendarAdapter) parent.getAdapter()).checkIfEventDate( selectedGridDate, v );
            }
        } );
    }

    public void openDB()
    {
        calendarDB = new DBAdapter(this);
        calendarDB.open();
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
    }


    @Override
    public void onClick(View v)
    {
        Intent intent;
        switch ( v.getId() )
        {
            case R.id.add_event_button:
                startActivity(new Intent(CalendarMainActivity.this,addEvent.class));
                break;
            case R.id.daily_view_button:
                intent = new Intent(CalendarMainActivity.this, dailyActivityView.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                break;
            case R.id.weekly_view_button:
                intent = new Intent(CalendarMainActivity.this, CalendarWeeklyView.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                break;
            case R.id.list_event_button:
                intent = new Intent(CalendarMainActivity.this, ListViewActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                break;
            default:
                break;
        }
    }
}