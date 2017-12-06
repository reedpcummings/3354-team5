import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class onTimeSetTest {
	private TimePickerDialog tester;
	private int hours;
	private int minutes;
	private String expectedOutput;
	
	public onTimeSetTest(int hours, int minutes, String expectedOutput)
	{
		this.hours = hours;
		this.minutes = minutes;
		this.expectedOutput = expectedOutput;
	}

	@Before
	public void setup() {
		tester = new TimePickerDialog();
	}
	
	@Parameterized.Parameters
	public static Collection<Object[]> valuePairs() {
		return Arrays.asList(new Object[][] {
			{0,0,"12:00 AM"}, {0,10,"12:10 AM"},  {12,0,"12:00 PM"}, {12,10,"12:10 PM"},
			{0,5,"12:05 AM"}, {0,47,"12:47 AM"},  {12,3,"12:03 PM"}, {12,56,"12:56 PM"},
			{6,0,"6:00 AM"},  {17,0,"5:00 PM"},   {4,10,"4:10 AM"},  {20,10,"8:10 PM"},
			{2,5,"2:05 AM"},  {10,43,"10:43 AM"}, {13,9,"1:09 PM"},  {21,27,"9:27 PM"}
		} );
	}

	@Test
	public void test() {
		assertEquals("hr " + (int)hours + " min " + (int)minutes + " = " + expectedOutput,
				expectedOutput, tester.onTimeSet((int)hours, (int)minutes));
	}
}
