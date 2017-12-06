package com.cs3354group09.calendar_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/*
This class works similar to the addEvent class. The functionality is the same, except that the data has
to be updated in the database. Also the database data is set into the view, so all data values have an
an initial value. New changes are updated into the database, and old changes override the same data leaving
the same data if unchanged.
 */
public class updateOverview extends Activity implements View.OnClickListener
{
    DBAdapter calendarDB;
    int rowID;
    String color;
    String month;
    Cursor cursor;
    static final int DATE_DIALOG_ID = 0;
    static final int START_TIME_DIALOG_ID = 1;
    static final int END_TIME_DIALOG_ID = 2;
    SimpleDateFormat dbDateFormatter;
    SimpleDateFormat showDateFormatter;
    int year_x, month_x,day_x, hour_x, minute_x;
    ImageButton back_button;

    TextView datePickedTextView;
    TextView startTimePickedTextView;
    TextView endTimePickedTextView;

    String enteredDate;
    String enteredStart;
    String enteredEnd;
    String storeDate;
    Date d;

    /*
    When the edit event is accessed, we create a page similar to the addEvent page where we have the similar access
    information, but instead of having empty text boxes and options all the fields are already filled in with an
    initial value.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_overview);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            rowID = extras.getInt("rowID");
            color = extras.getString("color");
            month = extras.getString("month");
        }

        openDB();
        cursor = calendarDB.getRow(rowID);
        setInfo();

        // Get the current information of the date of the event wanting to edit
        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);

        // Access the database to set the current initial values to the update info page
        enteredStart = cursor.getString(cursor.getColumnIndex(DBAdapter.KEY_START_TIME));
        enteredEnd = cursor.getString(cursor.getColumnIndex(DBAdapter.KEY_END_TIME));
        enteredDate = cursor.getString(cursor.getColumnIndex(DBAdapter.KEY_DATE));

        datePickedTextView = (TextView)findViewById(R.id.event_date_display);
        startTimePickedTextView = (TextView)findViewById(R.id.event_start_time);
        endTimePickedTextView = (TextView)findViewById(R.id.event_end_time);

        showDateDialogOnButtonClick();
        showTimeDialogOnButtonClick();

        // Once all the fields are updated, user can save the information to be updated into the
        // database
        Button saveButton = (Button)findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfoToDB();
                finish();
            }
        });

        // If you want to go back, not changing any fields in the database
        back_button = (ImageButton)findViewById(R.id.back_button_event_view);
        back_button.setOnClickListener(this);
    }

    public void showDateDialogOnButtonClick()
    {
        datePickedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
    }

    // When the information is updated to user liking, we set the new information to be ready to be
    // placed into the database.
    public void setInfo()
    {
        EditText nameEditText = (EditText)findViewById(R.id.event_name_display);
        TextView dateEditText = (TextView)findViewById(R.id.event_date_display);
        TextView startEditText = (TextView)findViewById(R.id.event_start_time);
        TextView endEditText = (TextView)findViewById(R.id.event_end_time);
        EditText locEditText = (EditText)findViewById(R.id.event_location_text);
        EditText descEditText = (EditText)findViewById(R.id.event_description_text);

        String newDate = cursor.getString(cursor.getColumnIndex(DBAdapter.KEY_DATE));
        String year = newDate.substring(0, 4);
        String month = newDate.substring(4, 6);
        String day = newDate.substring(6, 8);
        String newDateString = month + "/" + day + "/" + year;

        nameEditText.setText(cursor.getString(cursor.getColumnIndex(DBAdapter.KEY_NAME)));
        dateEditText.setText(newDateString);
        startEditText.setText(cursor.getString(cursor.getColumnIndex(DBAdapter.KEY_START_TIME)));
        endEditText.setText(cursor.getString(cursor.getColumnIndex(DBAdapter.KEY_END_TIME)));
        locEditText.setText(cursor.getString(cursor.getColumnIndex(DBAdapter.KEY_LOCATION)));
        descEditText.setText(cursor.getString(cursor.getColumnIndex(DBAdapter.KEY_DESCRIPTION)));
    }

    // Once the information is updated to user liking, once they click save, the application will
    // set all the new fields into the database. That will be the new information stored for that event
    public void saveInfoToDB()
    {
        String name;
        String location;
        String desc;

        EditText nameEditText = (EditText)findViewById(R.id.event_name_display);
        EditText locEditText = (EditText)findViewById(R.id.event_location_text);
        EditText descEditText = (EditText)findViewById(R.id.event_description_text);

        name = nameEditText.getText().toString();
        location = locEditText.getText().toString();
        desc = descEditText.getText().toString();

        openDB();
        updateEventToDatabase(name, enteredDate, desc, location, enteredStart, enteredEnd);
    }

    // To allow access into the database and update the information
    public void openDB()
    {
        calendarDB = new DBAdapter(this);
        calendarDB.open();
    }

    // Within the database, we update the individual table with the current values needed for the event
    public void updateEventToDatabase(String name, String date, String description, String location, String startTime, String endTime )
    {
        calendarDB.updateRow(rowID, name, date, description, location, startTime, endTime, cursor.getString(cursor.getColumnIndex(DBAdapter.KEY_COLOR)), cursor.getString(cursor.getColumnIndex(DBAdapter.KEY_MONTH)));
    }

    // ID to determine which dialog to open up when the specific corresponding buttons are pressed
    @Override
    protected Dialog onCreateDialog(int id)
    {
        if( id == DATE_DIALOG_ID )
        {
            return new DatePickerDialog(this, datePickerListener, year_x, month_x, day_x);
        }
        else if( id == START_TIME_DIALOG_ID )
        {
            return new TimePickerDialog(this, startTimePickerListener, hour_x, minute_x, DateFormat.is24HourFormat(this));
        }
        else if ( id == END_TIME_DIALOG_ID )
        {
            return new TimePickerDialog(this, endTimePickerListener, hour_x, minute_x, DateFormat.is24HourFormat(this));
        }
        else
        {
            return null;
        }
    }

    // functionality for the date picker, similar to the addEvent functionality
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet( DatePicker view, int year, int monthOfYear, int dayOfMonth )
        {
            year_x = year - 1900;
            month_x = monthOfYear;
            day_x = dayOfMonth;

            d = new Date(year_x, month_x, day_x);
            dbDateFormatter = new SimpleDateFormat("yyyyMMdd");
            showDateFormatter = new SimpleDateFormat("MM/dd/yyyy");
            enteredDate = showDateFormatter.format(d);
            storeDate = dbDateFormatter.format(d);

            Toast.makeText(updateOverview.this, enteredDate, Toast.LENGTH_LONG).show();
            datePickedTextView.setText(enteredDate);
        }
    };

    public void showTimeDialogOnButtonClick()
    {
        startTimePickedTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showDialog(START_TIME_DIALOG_ID);
            }
        });

        endTimePickedTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showDialog(END_TIME_DIALOG_ID);
            }
        });
    }

    private TimePickerDialog.OnTimeSetListener startTimePickerListener = new TimePickerDialog.OnTimeSetListener()
    {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute)
        {
            if( hourOfDay > 12 )
            {
                hour_x = hourOfDay - 12;

                minute_x = minute;
                if(minute_x < 10)
                {
                    enteredStart = hour_x + ":0" + minute_x + " PM";
                }
                else
                {
                    enteredStart = hour_x + ":" + minute_x + " PM";
                }
            }
            else
            {
                hour_x = hourOfDay;
                minute_x = minute;
                if(hour_x == 0)
                {
                    hour_x = 12;
                }
                if(minute_x < 10)
                {
                    enteredStart = hour_x + ":0" + minute_x + " AM";
                }
                else
                {
                    enteredStart = hour_x + ":" + minute_x + " AM";
                }
            }
            startTimePickedTextView.setText( enteredStart );

        }
    };

    private TimePickerDialog.OnTimeSetListener endTimePickerListener = new TimePickerDialog.OnTimeSetListener()
    {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute)
        {
            if( hourOfDay > 12 )
            {
                hour_x = hourOfDay - 12;

                minute_x = minute;
                if(minute_x < 10)
                {
                    enteredEnd = hour_x + ":0" + minute_x + " PM";
                }
                else
                {
                    enteredEnd = hour_x + ":" + minute_x + " PM";
                }
            }
            else
            {
                hour_x = hourOfDay;
                minute_x = minute;
                if(hour_x == 0)
                {
                    hour_x = 12;
                }
                if(minute_x < 10)
                {
                    enteredEnd = hour_x + ":0" + minute_x + " AM";
                }
                else
                {
                    enteredEnd = hour_x + ":" + minute_x + " AM";
                }
            }
            endTimePickedTextView.setText( enteredEnd );

        }
    };

    // To handle the functionality of the different button clicks
    @Override
    public void onClick(View v)
    {
        Intent intent;
        switch( v.getId() )
        {
            // When the back button is pressed it will just finish the current execution, and exit
            case R.id.back_button_event_view:
                finish();
                break;
            // When the delete button is pressed, we alert the user for confirmation. If they say yes, then delete the
            // entire event information from the database. If the user cancels the confirmation, we go back showing the
            // update info page
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
                startActivity(new Intent(updateOverview.this, addEvent.class));
                break;
            case R.id.weekly_view_button:
                intent = new Intent(updateOverview.this, CalendarWeeklyView.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                break;
            case R.id.calendar_event_button:
                intent = new Intent(updateOverview.this, CalendarMainActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                break;
            case R.id.list_event_button:
                intent = new Intent(updateOverview.this, ListViewActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                break;
            default:
                break;
        }
    }
}
