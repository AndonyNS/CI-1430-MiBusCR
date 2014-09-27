package com.example.busdevelop.buses;

import android.test.InstrumentationTestCase;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class LoginTest extends InstrumentationTestCase {

    public void testIsEmailValid(){
        LoginActivity l = new LoginActivity();
        String temp = "andony91@gmail.com";
        assertTrue(l.isEmailValid(temp));
    }

    public void testIsEmailInValid(){
        LoginActivity l = new LoginActivity();
        String temp = "Andony";
        assertFalse(l.isEmailValid(temp));
    }

    public void testIsPasswordValid(){
        LoginActivity l = new LoginActivity();
        String temp = "123456";
        assertTrue(l.isPasswordValid(temp));
    }

    public void testIsPasswordInValid(){
        LoginActivity l = new LoginActivity();
        String temp = "1";
        assertFalse(l.isEmailValid(temp));
    }

    public void testReturnCurrentEmailNull(){
        LoginActivity l = new LoginActivity();
        assertNull(l.getCurrentEmail());
    }

    public void testReturnCurrentPasswordNull(){
        LoginActivity l = new LoginActivity();
        assertNull(l.getCurrentPassword());
    }
}