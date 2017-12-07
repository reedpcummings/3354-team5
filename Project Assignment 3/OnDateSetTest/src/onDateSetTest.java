import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class onDateSetTest {
	private DatePickerDialog tester;
	private int year;
	private int month;
	private int day;
	private String expectedOutput;
	
	public onDateSetTest(int year, int month, int day, String expectedOutput)
	{
		this.year = year;
		this.month = month;
		this.day = day;
		this.expectedOutput = expectedOutput;
	}

	@Before
	public void setup() {
		tester = new DatePickerDialog();
	}
	
	@Parameterized.Parameters
	public static Collection<Object[]> valuePairs() {
		return Arrays.asList(new Object[][] {
			{2010,5,1, "06/01/2010"},{2010,0,1, "01/01/2010"},
			// leap days
			{2000,1,29, "02/29/2000"},{2004,1,29, "02/29/2004"},{2008,1,29, "02/29/2008"},{2012,1,29, "02/29/2012"},
			// boundary check
			{1900,0,1, "01/01/1900"}
		} );
	}

	@Test
	public void test() {
		assertEquals("yr " + (int)year + " mth " + (int)month + " day " + (int)day + " = " + expectedOutput,
				expectedOutput, tester.onDateSet((int)year, (int)month, (int)day));
	}
}
