
public class TimePickerDialog {
	
	public String onTimeSet(int hourOfDay, int minute)
    	{
		int minute_x;
		int hour_x;
		String enteredEnd;
		
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

		if (hour_x==0) {
		    hour_x = 12; }

		if( minute_x < 10 )
		{
		    enteredEnd = hour_x + ":0" + minute_x + enteredEnd;
		}
		else
		{
		    enteredEnd = hour_x + ":" + minute_x + enteredEnd;
		}
		return enteredEnd;
    	}
	
}
