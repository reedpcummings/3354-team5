    // Creates a calendar dialog box to choose event date and converts the data into
    // usable info for the database
    public class DatePickerDialog()
    {
        @Override
        public Date onDateSet(int year, int monthOfYear, int dayOfMonth )
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