package ca.thewizards.flashcards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AlarmActivity extends AppCompatActivity {

    EditText txtDate;
    EditText txtTime;
    Button btnSetAlarm;

    String dateTimeAlarmStr;
    private boolean darkTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        darkTheme = intent.getBooleanExtra("darkTheme", false);
        if (darkTheme){
            setTheme(R.style.AppThemeDark);
        } else {
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        txtDate = findViewById(R.id.date_input);
        txtTime = findViewById(R.id.time_input);
        btnSetAlarm = findViewById(R.id.btnSetAlarm);

        if (darkTheme){
            txtTime.setTextColor(Color.WHITE);
            txtTime.setHintTextColor(Color.WHITE);
            txtDate.setTextColor(Color.WHITE);
            txtDate.setHintTextColor(Color.WHITE);
        }

        setDefaultDate();

        // hide keyboard when click
        txtDate.setInputType(InputType.TYPE_NULL);
        txtTime.setInputType(InputType.TYPE_NULL);

        txtDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showDateDialog(txtDate);
            }
        });

        txtTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showTimeDialog(txtTime);
            }
        });

        btnSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String dateStr = txtDate.getText().toString();
                String timeStr = txtTime.getText().toString();

                String errorMsg = validateDateTime(dateStr, timeStr);

                if(errorMsg.matches(""))
                {
                    dateTimeAlarmStr = dateStr + " " + timeStr;

                    Toast.makeText(AlarmActivity.this,
                            "Alarm has been set to " + dateTimeAlarmStr, Toast.LENGTH_SHORT).show();

                    try {
                        DateFormat dateFormat = new SimpleDateFormat(getString(R.string.date_time_format));

                        // get the date time of alarm
                        Date dateTimeAlarm = dateFormat.parse(dateTimeAlarmStr);

                        // get the date time of current (turning off the app)
                        String dateTimeCurrentStr = dateFormat.format(new Date());
                        Date dateTimeCurrent = dateFormat.parse(dateTimeCurrentStr);

                        int alarm_delay = (int)(dateTimeAlarm.getTime() - dateTimeCurrent.getTime()); // milliseconds

                        // Test
                        //alarm_delay = 10000;
                        //Toast.makeText(AlarmActivity.this,"Alarm after " + alarm_delay, Toast.LENGTH_SHORT).show();

                        Intent alarmIntent = new Intent(AlarmActivity.this, NotificationService.class);
                        alarmIntent.putExtra("DateTimeAlarm", alarm_delay);

                        startService(alarmIntent);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(AlarmActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String validateDateTime(String dateStr, String timeStr){
        String errors = "";

        if(dateStr.matches(""))
            errors += getString(R.string.date_empty);
        if(timeStr.matches(""))
            errors += getString(R.string.time_empty);

        // check if the date is before today
        DateFormat dateFormat = new SimpleDateFormat(getString(R.string.date_time_format));
        try {
            // get the date time of alarm
            Date dateTimeSelected = dateFormat.parse(dateStr + " " + timeStr);

            // get the date time of current (turning off the app)
            String dateTimeCurrentStr = dateFormat.format(new Date());
            Date dateTimeCurrent = dateFormat.parse(dateTimeCurrentStr);

            if(dateTimeCurrent.after(dateTimeSelected))
                errors += getString(R.string.date_is_before);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return errors;
    }

    private void showTimeDialog(final EditText txtTime) {
        final Calendar calendar = Calendar.getInstance();

        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getString(R.string.time_format));
                txtTime.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };

        new TimePickerDialog(AlarmActivity.this, timeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                false).show();
    }

    private void showDateDialog(final EditText txtDate){
        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getString(R.string.date_format)); // set date format
                txtDate.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };

        new DatePickerDialog(AlarmActivity.this, dateSetListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void setDefaultDate(){
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        date = calendar.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.date_format));
        txtDate.setText(sdf.format(date));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        boolean ret = true;

        switch (item.getItemId()){
            case android.R.id.home:
                super.onBackPressed();
                break;
            default:
                ret = super.onOptionsItemSelected(item);
                break;
        }

        return ret;
    }
}