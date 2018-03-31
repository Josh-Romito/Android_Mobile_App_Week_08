package com.example.josh.week_08_day_02;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    protected void onStart() {
        super.onStart();
        //get stored password if any
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String password = preferences.getString("password", null);

        //if our password existed
        if(password != null) {
            //hide the second password field and label
            EditText repeat_password = findViewById(R.id.password_repeat);
            repeat_password.setVisibility(View.GONE);
            TextView label = findViewById(R.id.repeat_password_label);
            label.setVisibility(View.INVISIBLE);

            //show the hidden reset button
            Button reset_button = findViewById(R.id.reset_button);
            reset_button.setVisibility(View.VISIBLE);
        }
    }


    public void save_click(View view){
        //getting password fields
        EditText repeat_password = findViewById(R.id.password_repeat);
        EditText password = findViewById(R.id.password);

        //getting password field values
        String password_text = password.getText().toString();
        String password_repeat_text = repeat_password.getText().toString();

        //if our second password field is hidden - aka password has been set
        if(repeat_password.getVisibility() == View.GONE){

            //get our saved password
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String password_saved = preferences.getString("password", null);

            //if our input matches the saved password -
            if(password_text.equals(password_saved)){

                //set the time we logged in
                Calendar calender = Calendar.getInstance();
                Date time = new Date();
                SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String logged_in_time = DATE_FORMAT.format(calender.getTime());

                //store the time
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("logged_in_time", logged_in_time);

                //reset the special flag that determines if we are returning or not
                editor.putBoolean("returning", true);
                editor.apply();

                //start new activity
                Intent intent = new Intent(this, logged_in.class);
                startActivity(intent);
                // else password not correct
            } else{
                //display error msg
                TextView msg = findViewById(R.id.textView);
                msg.setText("Wrong password - try again!");
            }
        //else a password has never been saved
        }else {

            //if passwords are the same - save it - and load next activity
            if(password_repeat_text.equals(password_text)){
                //store the password
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("password",password_text);
                editor.apply();

                //set the time we logged in
                Calendar calender = Calendar.getInstance();
                Date time = new Date();
                SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String logged_in_time = DATE_FORMAT.format(calender.getTime());
                editor.putString("logged_in_time", logged_in_time);
                editor.apply();

                //start new activity
                Intent intent = new Intent(this, logged_in.class);
                startActivity(intent);
            }else {
                //display error msg
                TextView msg = findViewById(R.id.textView);
                msg.setText("Passwords do not match... - try again!");
            }
        }
    }


    public void reset_click(View view){
        //reset the password
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("password", "password");
        editor.apply();

        //display success msg
        TextView msg = findViewById(R.id.textView);
        msg.setText("Success - your password has been reset to 'password' ");
    }
}
