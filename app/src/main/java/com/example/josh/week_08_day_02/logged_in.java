package com.example.josh.week_08_day_02;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class logged_in extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        //get the username from preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String username = preferences.getString("username", "Alice the Camel");

        //set the username in the edit text
        EditText username_field = findViewById(R.id.username_field);
        username_field.setText(username);

        //calculate the time difference
        calc_time_diff();

        //get the switch
        Switch mySwitch;
        mySwitch = findViewById(R.id.switch1);


        //attach a listener to check for changes in state
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TextView msg = findViewById(R.id.bottom_msg);
                if(isChecked){
                    msg.setVisibility(View.VISIBLE);
                }else{
                    msg.setVisibility(View.INVISIBLE);
                }
            }
        });
    }


    @Override
    public void onRestart(){
        super.onRestart();

        // if set true - kick back to login page
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean flag = preferences.getBoolean("flag", false);

        //if we are returning
        if(flag){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        //set the flag back to false
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("flag", false);
        editor.apply();

        // SO - in order to conform to the assignment instructions,
        // I had to use an additional flag to determine if we are re-opening the logged in page
        // This was necessary because, every time we restart the logged in activity,
        // the app kicks us back to the main activity, thus calling the onStop, onPause etc methods, and storing the time.
        // When that happens, the time we 'left' is stored as the time we actually re-opened... hmmmmm
        editor.putBoolean("returning", false);
        editor.apply();
    }


    @Override
    public void onStop(){
        super.onStop();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        //save current time
        Calendar calender = Calendar.getInstance();
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String time_unfocused = DATE_FORMAT.format(calender.getTime());

        //set flag to kick us back to login page
        editor.putBoolean("flag", true);
        editor.apply();

        //get the special flag to see if we are returning or not
        boolean returning = preferences.getBoolean("returning", false);

        //set the time we left the activity - only stored if we are not returning to the same activity
        if(returning){
            editor.putString("time_unfocused", time_unfocused);
            editor.apply();
        }
    }


    public void set_username_click(View view){
        //get username
        EditText username_field = findViewById(R.id.username_field);
        String username = username_field.getText().toString();

        //store username
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", username);
        editor.apply();

        username_field.setText(username);
    }


    private void calc_time_diff(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        //get time logged in
        String logged_in_time = preferences.getString("logged_in_time", "0:00");

        //get the time we left the app
        String time_exited = preferences.getString("time_unfocused", "0:00");

        //set text view with time logged in
        TextView logged_in_time_view = findViewById(R.id.logged_in_time);
        logged_in_time_view.setText(logged_in_time);

        //set the text view with time we left the page
        TextView left_page_time = findViewById(R.id.left_page_time);
        left_page_time.setText(time_exited);


        // TIME DIFFERENCE OPERATION
        //define a date format and variables
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date d1;
        Date d2;

        ///try catch possible parse errors
        try{
            //format our times into proper date types
            d1 = DATE_FORMAT.parse(time_exited);
            d2 = DATE_FORMAT.parse(logged_in_time);

            //in milliseconds
            long totalDiff = d2.getTime() - d1.getTime();

            long secondsDiff = totalDiff / 1000 % 60;
            long minutesDiff = totalDiff / (60 * 1000) % 60;
            long hoursDiff = totalDiff / (60 * 60 * 1000) % 24;
            long daysDiff = totalDiff / (24 * 60 * 60 * 1000);
            TextView diff_text = findViewById(R.id.time_diff);
            diff_text.setText(daysDiff + " days, \n" + hoursDiff + " hours, \n" + minutesDiff + " minutes, \n" + secondsDiff + " seconds.");

        } catch(Exception e){
            TextView diff_text = findViewById(R.id.time_diff);
            diff_text.setText("0:00 - First log-in");
        }
    }
}
