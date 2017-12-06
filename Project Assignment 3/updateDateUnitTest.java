package com.cs3354group09.calendar_app;

import junit.framework.TestCase;

import dalvik.annotation.TestTarget;

public class updateDateUnitTest extends TestCase{
    private updateOverview tester;
    private String expectedOutput;

    @Before
    public void setup(){
        tester = new updateOverview();
        this.expectedOutput = tester.enteredDate;
    }

    @Test
    public void test1() {
        assertEquals("new date equals information entered",true,tester.expectedOutput == tester.setInfo().newDate);
    }
    
    @Test
    public void test2() {
    	assertEquals("new date equals information entered",false,tester.expectedOutput == tester.setInfo().newDate);
    }
}
