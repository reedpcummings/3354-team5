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
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class dailyActivityView extends Activity implements OnClickListener
{
    private ListView activity_list_view;
    DBAdapter calendarDB;
    private CalendarListAdapter mCalCursorAdapter;
    String pickedDay;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.daily_activity_view);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            pickedDay = extras.getString("pickedDay");
        }
        else
        {
            GregorianCalendar tempCal = (GregorianCalendar)GregorianCalendar.getInstance();
            pickedDay = android.text.format.DateFormat.format("yyyyMMdd", tempCal).toString();
        }
        //Open and populate the database.
        openDB();
        populateListView();
        checkForEvents();

    }

    public void checkForEvents()
    {

    }

    @Override
    public void onResume()
    {
        super.onResume();
        populateListView();
    }


    public void openDB()
    {
        calendarDB = new DBAdapter(this);
        calendarDB.open();
    }

    @Override
    public void onClick(View v)
    {
        Intent intent;
        switch ( v.getId() )
        {
            case R.id.add_event_button:
                startActivity(new Intent(dailyActivityView.this,addEvent.class));
                break;
            case R.id.calendar_event_button:
                intent = new Intent(dailyActivityView.this, CalendarMainActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                break;
            case R.id.weekly_view_button:
                intent = new Intent(dailyActivityView.this, CalendarWeeklyView.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                break;
            case R.id.list_event_button:
                intent = new Intent(dailyActivityView.this, ListViewActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                break;
            default:
                break;
        }
    }

    public void populateListView()
    {
        //Get info from database.

        activity_list_view = (ListView) findViewById( R.id.list_view_daily );
        String[] condition = {pickedDay};
        Cursor cursor = calendarDB.getColumnWithDate(condition);
        if(!(cursor.getCount() == 0) )
        {
            mCalCursorAdapter = new CalendarListAdapter(getBaseContext(), R.layout.list_item, cursor);
            activity_list_view.setAdapter(mCalCursorAdapter);
            activity_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int rowID = Integer.parseInt(view.getTag().toString());
                    Intent intent = new Intent(dailyActivityView.this, eventOverview.class);
                    intent.putExtra("id", rowID);
                    startActivity(intent);
                }
            });
            TextView hideView = (TextView)findViewById(R.id.no_events_text_view);
            hideView.setVisibility(View.GONE);
        }



    }




}