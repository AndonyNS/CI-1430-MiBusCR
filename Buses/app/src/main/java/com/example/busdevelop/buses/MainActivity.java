package com.example.busdevelop.buses;

import android.view.Menu;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;



public class MainActivity extends ActionBarActivity {

    private static final String mFIREBASE_URL = "https://blazing-fire-9075.firebaseio.com/";
    private String mGps;
    private String mLocation;
    private String mLatitud;
    private String mLongitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MainMenuFragment())
                    .commit();
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getLocation(View v){



           Firebase firebaseRef = new Firebase(mFIREBASE_URL);

            firebaseRef.addChildEventListener(new ChildEventListener(){

                @Override
                public void onChildChanged(DataSnapshot snapshot, String previousChildName){

                   mGps = (String) snapshot.child("GpsID").getValue();
                   mLocation = (String) snapshot.child("Location").getValue();
                   String[] parts = mLocation.split(" ");
                    mLatitud = parts[0];
                    mLongitud = parts[1];


                }

                @Override
                public void onChildAdded(DataSnapshot snapshot, String previousChildName){}

                @Override
                public void onChildRemoved(DataSnapshot snapshot){}

                @Override
                public void onChildMoved(DataSnapshot snapshot, String previousChildName){}

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    System.out.println("The read failed: " + firebaseError.getMessage());
                }

            });



    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
