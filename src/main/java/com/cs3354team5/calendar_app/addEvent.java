package com.cs3354team5.calendar_app;

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


import com.cs3354group09.calendar_app.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class addEvent extends Activity implements OnClickListener
{
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

    public void showDateDialogOnButtonClick()
    {
        datePickedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
    }

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

    public boolean allFieldsEntered()
    {

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

        if(!errmsgNames.isEmpty())
        {
            return false;
        }
        return true;
    }

    public void errmsgNamesToString()
    {
        errmsgNamesString = "";
        for(int i = 0; i < errmsgNames.size(); i++)
        {
            errmsgNamesString += errmsgNames.get(i) + "\n";
        }
    }

    @Override
    public void onClick(View v)
    {
        Intent intent;
        switch( v.getId() )
        {
            case R.id.back_button_add_view:
                finish();
                break;
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

    public void displayColor(String color)
    {
        ImageButton colorDisplay = (ImageButton)findViewById(R.id.color_chosen_image);
        colorDisplay.setBackgroundColor(Color.parseColor(color));
    }

    public void getEventData()
    {
        EditText nameText = (EditText)findViewById(R.id.name_edit_text);
        enteredName = nameText.getText().toString();

        EditText locText = (EditText)findViewById(R.id.loc_edit_text);
        enteredLocation = locText.getText().toString();

        EditText descText = (EditText)findViewById(R.id.desc_edit_text);
        enteredDesc = descText.getText().toString();
    }

    public void openDB()
    {
        calendarDB = new DBAdapter(this);
        calendarDB.open();
    }

    public void addEventToDataBase(String name, String date, String description, String location, String startTime, String endTime, String color, String month )
    {
        calendarDB.insertRow(name, date, description, location, startTime, endTime, color, month);
    }
}
