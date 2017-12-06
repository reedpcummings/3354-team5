package com.cs3354group09.calendar_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

/*
This class gives the information of an event in an overview. It accesses the database, and retrieves any
information from the database tables.
*/

public class eventOverview extends Activity implements OnClickListener
{
    private ImageButton back_button;
    private TextView event_name;
    private TextView event_date;
    private TextView event_description;
    private TextView event_location;
    private TextView event_start_time;
    private TextView event_end_time;
    private LinearLayout main_layout;
    DBAdapter calendarDB;
    Cursor cursor;
    int id = 0;

    String name = "";
    String date = "";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_overview);

        Bundle extras = getIntent().getExtras();

        if( extras != null)
        {
            id = extras.getInt("id");
        }

        back_button = (ImageButton)findViewById(R.id.back_button_event_view);
        back_button.setOnClickListener(this);

        openDB();
        getDataFromDatabase();
        setDataFromDatabase();
        setBackground();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getDataFromDatabase();
        setDataFromDatabase();
        setBackground();
    }

    public void openDB()
    {
        calendarDB = new DBAdapter(this);
        calendarDB.open();
    }

    public void getDataFromDatabase()
    {
        cursor = calendarDB.getRow(id);
    }

    public void setDataFromDatabase()
    {
        event_name = (TextView)findViewById(R.id.event_name_display);
        event_date = (TextView)findViewById(R.id.event_date_display);
        event_description = (TextView)findViewById(R.id.event_description_text);
        event_location = (TextView)findViewById(R.id.event_location_text);
        event_start_time = (TextView)findViewById(R.id.event_start_time);
        event_end_time = (TextView)findViewById(R.id.event_end_time);

        String newDate = cursor.getString(cursor.getColumnIndex(DBAdapter.KEY_DATE));
        String year = newDate.substring(0, 4);
        String month = newDate.substring(4, 6);
        String day = newDate.substring(6, 8);
        String newDateString = month + "/" + day + "/" + year;

        event_name.setText(cursor.getString(cursor.getColumnIndex(DBAdapter.KEY_NAME)));
        event_date.setText(newDateString);
        event_description.setText(cursor.getString(cursor.getColumnIndex(DBAdapter.KEY_DESCRIPTION)));
        event_location.setText(cursor.getString(cursor.getColumnIndex(DBAdapter.KEY_LOCATION)));
        event_start_time.setText(cursor.getString(cursor.getColumnIndex(DBAdapter.KEY_START_TIME)));
        event_end_time.setText(cursor.getString(cursor.getColumnIndex(DBAdapter.KEY_END_TIME)));
    }

    public void setBackground()
    {
        main_layout = (LinearLayout)findViewById(R.id.main_event_details);

        String backgroundColor = cursor.getString(cursor.getColumnIndex(DBAdapter.KEY_COLOR));

//        if( backgroundColor == "#1abc9c" )
//        {
//            main_layout.setBackgroundResource(R.mipmap.turqoisebg);
//        }
//        else if( backgroundColor == "#2ecc71" )
//        {
//            main_layout.setBackgroundResource(R.mipmap.emeraldbg);
//        }
//        else if( backgroundColor == "#3498db" )
//        {
//            main_layout.setBackgroundResource(R.mipmap.peterriverbg);
//        }
//        else if( backgroundColor == "#d35400" )
//        {
//            main_layout.setBackgroundResource(R.mipmap.pumkinbg);
//        }
//        else if( backgroundColor == "#f1c40f" )
//        {
//            main_layout.setBackgroundResource(R.mipmap.sunflowerbackground);
//        }
        main_layout.setBackgroundColor(Color.parseColor(backgroundColor));
    }


    @Override
    public void onClick(View v)
    {
        Intent intent;
        switch( v.getId() )
        {
            case R.id.back_button_event_view:
                finish();
                break;
            case R.id.delete_button:
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Confirm Deletion")
                        .setMessage("Are you sure you want to delete event: " + cursor.getString(cursor.getColumnIndex(DBAdapter.KEY_NAME)))
                        .setPositiveButton("DELETE", new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                calendarDB.deleteRow(cursor.getInt(cursor.getColumnIndex(DBAdapter.KEY_ROWID)));
                                finish();
                            }
                        })
                        .setNegativeButton("CANCEL", new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                break;
            case R.id.add_event_button:
                startActivity(new Intent(eventOverview.this, addEvent.class));
                break;
            case R.id.weekly_view_button:
                intent = new Intent(eventOverview.this, CalendarWeeklyView.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                break;
            case R.id.calendar_event_button:
                intent = new Intent(eventOverview.this, CalendarMainActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                break;
            case R.id.list_event_button:
                intent = new Intent(eventOverview.this, ListViewActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                break;
            case R.id.update_button:
                getDataFromDatabase();
                intent = new Intent(eventOverview.this, updateOverview.class);
                intent.putExtra("rowID", cursor.getInt(cursor.getColumnIndex(DBAdapter.KEY_ROWID)));
                intent.putExtra("color", cursor.getString(cursor.getColumnIndex(DBAdapter.KEY_COLOR)));
                startActivity(intent);
                overridePendingTransition(0,0);
                break;
            default:
                break;
        }
    }
}


