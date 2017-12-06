package com.cs3354group09.calendar_app;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.Calendar;

/*
This class creates the list view of the calendar views. It works similar to the daily view in showing the
events in a chronological order, but it shows all events within the database. It extends all the activities
and allows access to use button clicks.
*/

public class ListViewActivity extends Activity implements OnClickListener
{
    private ListView activity_list_view;
    private ImageButton calender_button;
    private ImageButton add_button;
    DBAdapter calendarDB;
    private CalendarListAdapter mCalCursorAdapter;

    // When the list view is first opened, we open the database to allow access of the information for the view
    // And then we populate the views once we parse the information into usable data
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_view);

        //Open and populate the database.
        openDB();

        populateListView();

        getWidget();

    }

    // To allow continuous access, when the view is changed or exited
    @Override
    public void onResume()
    {
        super.onResume();
        populateListView();
    }


    //Function to get the calendar button. (Maybe temporary until side menu implemented.)
    public void getWidget()
    {
        calender_button = (ImageButton) findViewById( R.id.calendar_event_button );
        calender_button.setOnClickListener(this);

        add_button = (ImageButton) findViewById(R.id.add_event_button);
        add_button.setOnClickListener(this);
    }

    // Get access to database and allow interaction between the view and the database
    public void openDB()
    {
        calendarDB = new DBAdapter(this);
        calendarDB.open();
    }

    // The functionality of each of the buttons when the button is clicked.
    @Override
    public void onClick(View v)
    {
        Intent intent;
        switch ( v.getId() )
        {
            case R.id.add_event_button:
                startActivity(new Intent(ListViewActivity.this, addEvent.class));
                break;
            case R.id.daily_view_button:
                intent = new Intent(ListViewActivity.this, dailyActivityView.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                break;
            case R.id.calendar_event_button:
                intent = new Intent(ListViewActivity.this, CalendarMainActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                break;
            case R.id.weekly_view_button:
                intent = new Intent(ListViewActivity.this, CalendarWeeklyView.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                break;
            default:
                break;
        }
    }

    public void populateListView()
    {
        //Get info from database, and populate the views with the information


        activity_list_view = (ListView) findViewById( R.id.list_view_main );

            Cursor cursor = calendarDB.getAllRows();
            mCalCursorAdapter = new CalendarListAdapter(getBaseContext(), R.layout.list_item, cursor);
            activity_list_view.setAdapter(mCalCursorAdapter);
            activity_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int rowID = Integer.parseInt(view.getTag().toString());
                    Intent intent = new Intent(ListViewActivity.this, eventOverview.class);
                    intent.putExtra("id", rowID);
                    startActivity(intent);
                }
            });


    }




}