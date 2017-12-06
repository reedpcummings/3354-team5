package com.cs3354group09.calendar_app;

import junit.framework.TestCase;

import dalvik.annotation.TestTarget;

public class errmsgNamesToStringTest extends TestCase{
    private errmsgNamesToString tester;

    @Before
    public void setup(){
        tester = new errmsgNamesToString();
    }

    @Test
    public void test1empty() {
        assertEquals("Event Name", 1, tester.contains({"Event Name"}),"Event Name");
    }
    @Test
    public void test2empty(){
        assertEquals("Date", 1, tester.contains({"Date"}), "Date")
    }

    @Test
    public void test3empty(){
            assertEquals("Start Time" 1, tester.contains({"Start Time"}, "Start Time"))
        }
}
