package com.cs3354group09.calendar_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/*
This class creates an instance of the add event window page. The add event page
gets the date, name of the event, description of the event, location, start and end times,
and a designation color to help organize events. Each option either has an onClick
option dialog, or a text box option. All fields must have a value, since the database
requires and uses those values.

Based on the input parameters uses the data and stores them into the data base for future uses.
 */
public class addEvent extends Activity implements OnClickListener
{
    // Used for database handling and storage
    TextView datePickedTextView;
    TextView startTimePickedTextView;
    TextView endTimePickedTextView;
    ImageButton backButton;
    int year_x, month_x, day_x, hour_x, minute_x;
    static final int DATE_DIALOG_ID = 0;
    static final int START_TIME_DIALOG_ID = 1;
    static final int END_TIME_DIALOG_ID = 2;
    SimpleDateFormat dbDateFormatter;
    SimpleDateFormat showDateFormatter;
    Date d;
    DBAdapter calendarDB;

    // Strings to hold the fields in the database to be much more easily read by the user
    String enteredName;
    String enteredDate;
    String enteredDesc;
    String enteredLocation;
    String enteredStart;
    String enteredEnd;
    String chosenColor = "#3498db";
    String storeDate;
    String errmsgNamesString;

    ArrayList<String> errmsgNames = new ArrayList<String>();

    /* When the add event button is pressed, we create an instance of it
       This instance can be overridden to change any state
       Creates all the options within the addEvent class and all necessary
       add-ons needed to fill in each field.
       Additionally opens the database so data fields can be placed into it.
    */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);

        datePickedTextView = (TextView)findViewById(R.id.date_picked);
        startTimePickedTextView = (TextView)findViewById(R.id.start_time_picked);
        endTimePickedTextView = (TextView)findViewById(R.id.end_time_picked);
        backButton = (ImageButton)findViewById(R.id.back_button_add_view);

        backButton.setOnClickListener(this);

        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);

        openDB();

        showDateDialogOnButtonClick();
        showTimeDialogOnButtonClick();
    }

    // Shows the data dialog box when clicked on
    public void showDateDialogOnButtonClick()
    {
        datePickedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
    }

    // Creates all the dialog boxes when chosen.
    // Each dialog is given an ID to reference from.
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

    // Creates a calendar dialog box to choose event date and converts the data into
    // usable info for the database
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

            Toast.makeText(addEvent.this,enteredDate, Toast.LENGTH_LONG).show();
            datePickedTextView.setText(enteredDate);
        }
    };

    // Creates a time dialog box to choose times
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

    // Puts the data from the dialog box into usable data for the database for start time
    private TimePickerDialog.OnTimeSetListener startTimePickerListener = new TimePickerDialog.OnTimeSetListener()
    {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute)
        {
            minute_x = minute;
            
            if( hourOfDay >= 12 )
            {
                hour_x = hourOfDay - 12;
                enteredStart = " PM";
            }
            else
            {
                hour_x = hourOfDay;
                enteredStart = " AM";
            }
            
            if(hour_x == 0)
            {
                hour_x = 12;
            }
            
            if(minute_x < 10)
            {
                enteredStart = hour_x + ":0" + minute_x + enteredStart;
            }
            else
            {
                enteredStart = hour_x + ":" + minute_x + enteredStart;
            }
            
            startTimePickedTextView.setText( enteredStart );
        }
    };

    // Puts the data from the dialog box into usable data for the database for end time
    private TimePickerDialog.OnTimeSetListener endTimePickerListener = new TimePickerDialog.OnTimeSetListener()
    {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute)
        {
            minute_x = minute;
            
            if( hourOfDay >= 12 )
            {
                hour_x = hourOfDay - 12;
                enteredEnd = " PM";
            }
            else
            {
                hour_x = hourOfDay;
                enteredEnd = " AM";
            }
            
            if (hour_x==0)
            {
                hour_x = 12;
            }
            
            if( minute_x < 10 )
            {
                enteredEnd = hour_x + ":0" + minute_x + enteredEnd; 
            }
            else
            {
                enteredEnd = hour_x + ":" + minute_x + enteredEnd;
            }
            
            endTimePickedTextView.setText( enteredEnd );
         }
    };

    // Method inputs all the data fields into the database
    public boolean allFieldsEntered()
    {
        // Clear the error messages and allow the fields to be inputted into the
        // database. Allows the option to edit still if needed
        errmsgNames.clear();

        EditText nameEditText = (EditText) findViewById(R.id.name_edit_text);
        String name_ed_text = nameEditText.getText().toString().trim();

        if(name_ed_text.isEmpty() || name_ed_text.length() == 0 || name_ed_text.equals("") || name_ed_text == null)
        {
            errmsgNames.add("Event Name");
        }

        String date_ed_text = datePickedTextView.getText().toString().trim();

        if(date_ed_text.equals("Click To Choose A Date") || date_ed_text.isEmpty() || date_ed_text.length() == 0 || date_ed_text.equals("") || date_ed_text == null)
        {
            errmsgNames.add("Date");
        }

        String start_ed_text = startTimePickedTextView.getText().toString().trim();

        if(start_ed_text.equals("Start Time") || start_ed_text.isEmpty() || start_ed_text.length() == 0 || start_ed_text.equals("") || start_ed_text == null)
        {
            errmsgNames.add("Start Time");
        }

        String end_ed_text = endTimePickedTextView.getText().toString().trim();

        if(end_ed_text.equals("End Time") || end_ed_text.isEmpty() || end_ed_text.length() == 0 || end_ed_text.equals("") || end_ed_text == null)
        {
            errmsgNames.add("End Time");
        }

        EditText locEditText = (EditText) findViewById(R.id.loc_edit_text);
        String loc_ed_text = locEditText.getText().toString().trim();

        if(loc_ed_text.isEmpty() || loc_ed_text.length() == 0 || loc_ed_text.equals("") || loc_ed_text == null)
        {
            errmsgNames.add("Location");
        }

        EditText descEditText = (EditText) findViewById(R.id.desc_edit_text);
        String desc_ed_text = descEditText.getText().toString().trim();

        if(desc_ed_text.isEmpty() || desc_ed_text.length() == 0 || desc_ed_text.equals("") || desc_ed_text == null)
        {
            errmsgNames.add("Description");
        }

        // If there was a mistake, and there is an error in the input
        // Finalize the data fields
        if(!errmsgNames.isEmpty())
        {
            return false;
        }
        return true;
    }

    // Determines which fields need to be fixed
    public void errmsgNamesToString()
    {
        errmsgNamesString = "";
        for(int i = 0; i < errmsgNames.size(); i++)
        {
            errmsgNamesString += errmsgNames.get(i) + "\n";
        }
    }

    /* Method that puts all the functionality within the button clicks.
       Each button has a different function. Also updates the different
       calendar views with the same event in the database
     */
    @Override
    public void onClick(View v)
    {
        Intent intent;

        //
        switch( v.getId() )
        {
            // Whenever back is pressed, quit the current task completely
            case R.id.back_button_add_view:
                finish();
                break;

            // Adds an event to the database to use, but must have complete information
            // Displays an alert to notify user they must input all fields
            case R.id.add_event_button:
                getEventData();
                String month = Integer.toString(month_x + 1);
                if( allFieldsEntered() )
                {
                    addEventToDataBase(enteredName, storeDate, enteredDesc, enteredLocation, enteredStart, enteredEnd, chosenColor, month);
                    finish();
                }
                else
                {
                    errmsgNamesToString();

                    new AlertDialog.Builder(this)
                            .setIcon(android.R.drawable.stat_notify_error)
                            .setTitle("Fields Left Empty")
                            .setMessage("\t\tFollowing Fields Are Required:\n" + errmsgNamesString)
                            .setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                }

                break;

            // Gives the color options to choose from
            case R.id.turquoise_button:
                chosenColor = "#1abc9c";
                displayColor(chosenColor);
                break;
            case R.id.emerald_button:
                chosenColor = "#2ecc71";
                displayColor(chosenColor);
                break;
            case R.id.pumpkin_button:
                chosenColor = "#d35400";
                displayColor(chosenColor);
                break;
            case R.id.sunflower_button:
                chosenColor = "#f1c40f";
                displayColor(chosenColor);
                break;
            case R.id.peter_river_button:
                chosenColor = "#3498db";
                displayColor(chosenColor);
                break;

            // Add the event to all views of the calendar
            case R.id.daily_view_button:
                intent = new Intent(addEvent.this, dailyActivityView.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                break;
            case R.id.weekly_view_button:
                intent = new Intent(addEvent.this, CalendarWeeklyView.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                break;
            case R.id.calendar_event_button:
                intent = new Intent(addEvent.this, CalendarMainActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                break;
            case R.id.list_event_button:
                intent = new Intent(addEvent.this, ListViewActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                break;
            default:
                break;
        }
    }

    // Allows the user to see the color options to choose from.
    public void displayColor(String color)
    {
        ImageButton colorDisplay = (ImageButton)findViewById(R.id.color_chosen_image);
        colorDisplay.setBackgroundColor(Color.parseColor(color));
    }

    // In the case of editing information before adding
    public void getEventData()
    {
        EditText nameText = (EditText)findViewById(R.id.name_edit_text);
        enteredName = nameText.getText().toString();

        EditText locText = (EditText)findViewById(R.id.loc_edit_text);
        enteredLocation = locText.getText().toString();

        EditText descText = (EditText)findViewById(R.id.desc_edit_text);
        enteredDesc = descText.getText().toString();
    }

    // Access the database and allow the data to be inputted through an adaptor
    public void openDB()
    {
        calendarDB = new DBAdapter(this);
        calendarDB.open();
    }

    // All the information is placed onto a data table of columns and rows
    public void addEventToDataBase(String name, String date, String description, String location, String startTime, String endTime, String color, String month )
    {
        calendarDB.insertRow(name, date, description, location, startTime, endTime, color, month);
    }
}
