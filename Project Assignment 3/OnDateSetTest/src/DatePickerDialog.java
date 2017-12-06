    
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

//Creates a calendar dialog box to choose event date and converts the data into
//usable info for the database
    public class DatePickerDialog{
    	int year_x = 0;
    	int month_x = 0;
    	int day_x = 0;
    	Date d;
    	SimpleDateFormat dbDateFormatter,showDateFormatter;
    	String enteredDate,storeDate;
    	
        @SuppressWarnings("deprecation")
		public String onDateSet(int year, int monthOfYear, int dayOfMonth )
        {
            year_x = year - 1900;
            month_x = monthOfYear;
            day_x = dayOfMonth;

            d = new Date(year_x, month_x, day_x);
            dbDateFormatter = new SimpleDateFormat("yyyyMMdd");
            showDateFormatter = new SimpleDateFormat("MM/dd/yyyy");
            enteredDate = showDateFormatter.format(d);
            storeDate = dbDateFormatter.format(d);


            return enteredDate;
        }
    };