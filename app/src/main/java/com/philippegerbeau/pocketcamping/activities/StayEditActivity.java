package com.philippegerbeau.pocketcamping.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philippegerbeau.pocketcamping.Handler;
import com.philippegerbeau.pocketcamping.R;
import com.philippegerbeau.pocketcamping.data.Alert;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class StayEditActivity extends AppCompatActivity {
    private final String DATE_FORMAT = "d MMM yyyy";
    private final String TIME_FORMAT = "HH:mm";

    private TextInputEditText location;
    private TextInputEditText arrivalDate;
    private TextInputEditText departureDate;
    private TextInputEditText arrivalTime;
    private TextInputEditText departureTime;
    private TextInputEditText spot;

    private Calendar calArrival;
    private Calendar calDeparture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stay_edit);

        location = findViewById(R.id.location);
        arrivalDate = findViewById(R.id.arrival_date);
        departureDate = findViewById(R.id.departure_date);
        arrivalTime = findViewById(R.id.arrival_time);
        departureTime = findViewById(R.id.departure_time);
        spot = findViewById(R.id.spot);

        calArrival = Calendar.getInstance();
        calDeparture = Calendar.getInstance();

        updateLabel(calArrival, arrivalDate, DATE_FORMAT);
        updateLabel(calDeparture, departureDate, DATE_FORMAT);
        updateLabel(calArrival, arrivalTime, TIME_FORMAT);
        updateLabel(calDeparture, departureTime, TIME_FORMAT);

        findExistingData();
    }

    private void findExistingData() {
        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        String userID = "";
        if (fbUser != null) {
            userID = fbUser.getUid();
        }

        DatabaseReference fbUserRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(userID);

        fbUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("stayID").exists()) {
                    setExistingData();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void setExistingData() {
        DatabaseReference fbStayRef = FirebaseDatabase.getInstance().getReference()
                .child("stays").child(Handler.user.getStayID());

        fbStayRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                location.setText((String) dataSnapshot.child("location").getValue());
                location.setSelection(location.getText().length());
                spot.append((String) dataSnapshot.child("spot").getValue());
                spot.setSelection(spot.getText().length());

                calArrival.setTimeInMillis((long) dataSnapshot.child("arrival").getValue());
                calDeparture.setTimeInMillis((long) dataSnapshot.child("departure").getValue());

                updateLabel(calArrival, arrivalDate, DATE_FORMAT);
                updateLabel(calDeparture, departureDate, DATE_FORMAT);
                updateLabel(calArrival, arrivalTime, TIME_FORMAT);
                updateLabel(calDeparture, departureTime, TIME_FORMAT);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void popDate(View view) {
        Calendar calendar;
        EditText label;
        if (view.getId() == R.id.arrival_date) {
            calendar = calArrival;
            label = arrivalDate;
        } else {
            calendar = calDeparture;
            label = departureDate;
        }

        DatePickerDialog.OnDateSetListener listener = setDateListener(calendar, label);
        new DatePickerDialog(this, listener, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private DatePickerDialog.OnDateSetListener setDateListener(final Calendar calendar,
                                                             final EditText label) {
        return new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(calendar, label, DATE_FORMAT);
            }
        };
    }

    public void popTime(View view) {
        Calendar calendar;
        EditText label;
        if (view.getId() == R.id.arrival_time) {
            calendar = calArrival;
            label = arrivalTime;
        } else {
            calendar = calDeparture;
            label = departureTime;
        }

        TimePickerDialog.OnTimeSetListener listener = setTimeListener(calendar, label);
        new TimePickerDialog(this, listener, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), true).show();
    }

    private TimePickerDialog.OnTimeSetListener setTimeListener(final Calendar calendar, final EditText label) {
        return new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                calendar.set(Calendar.MINUTE, selectedMinute);
                updateLabel(calendar, label, TIME_FORMAT);
            }
        };
    }

    private void updateLabel(Calendar calendar, EditText label, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());

        label.setText(sdf.format(calendar.getTime()));
    }

    public void confirm(View view) {
        if (validForm()) {
            submitStay();

            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);
            finish();
        }
    }

    private boolean validForm() {
        boolean valid = false;

        if (!validName()) {
            location.setError(getString(R.string.invalid_location));
        } else if (!validArrival()) {
            arrivalDate.setError(getString(R.string.invalid_date));
        } else if (!validDeparture()) {
            departureDate.setError(getString(R.string.invalid_date));
        } else {
            valid = true;
        }
        return valid;
    }

    private boolean validName() {
        return location.getText().length() > 0;
    }

    private boolean validArrival() {
        return calArrival.compareTo(Calendar.getInstance()) > 0;
    }

    private boolean validDeparture() {
        return calDeparture.compareTo(calArrival) > 0;
    }

    private void submitStay() {
        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        String userID = "";
        if (fbUser != null) {
            userID = fbUser.getUid();
        }
        DatabaseReference fbUserRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(userID);

        if (Handler.user.getStayID() == null) {
            Handler.user.setStayID(fbUserRef.push().getKey());
            fbUserRef.child("stayID").setValue(Handler.user.getStayID());
            Alert.log(Alert.CREATED, Alert.CAT_STAY, location.getText().toString());
        }

        DatabaseReference fbStayRef = FirebaseDatabase.getInstance().getReference()
                .child("stays").child(Handler.user.getStayID());
        fbStayRef.child("location").setValue(location.getText().toString());
        fbStayRef.child("arrival").setValue(calArrival.getTimeInMillis());
        fbStayRef.child("departure").setValue(calDeparture.getTimeInMillis());
        fbStayRef.child("spot").setValue(spot.getText().toString());
    }
}
