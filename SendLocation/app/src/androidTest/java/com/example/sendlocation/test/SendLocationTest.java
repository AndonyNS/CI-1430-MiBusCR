package com.example.sendlocation.test;

import android.test.ActivityTestCase;

import com.example.sendlocation.MainActivity;

public class SendLocationTest extends ActivityTestCase{

    public void testConversionNanoTime(){
        MainActivity m = new MainActivity();
        assertEquals(30000000, m.min2nano(5));
    }

}
