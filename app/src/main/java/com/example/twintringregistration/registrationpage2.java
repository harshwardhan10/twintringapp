package com.example.twintringregistration;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class registrationpage2 extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
RadioGroup radioGroup;
RadioButton radioButton;
Button registerbtn;
String gender;
ProgressBar progressBar ;
    EditText fullname;
    Calendar c;
    Button dobbtn;

    TextView displaydate;
    DatePickerDialog dpd;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrationpage2);
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        registerbtn = (Button) findViewById(R.id.registerbtn);
        progressBar = findViewById(R.id.progressbar2);
        fullname = findViewById(R.id.fullname);
        displaydate = (TextView) findViewById(R.id.displaydate);
        dobbtn = (Button)findViewById(R.id.dob);
        dobbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c=Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);
                dpd = new DatePickerDialog(registrationpage2.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        displaydate.setText(dayOfMonth + " / " + (month+1) + " / " + year);
                    }
                },day,month,year);
                dpd.show();
            }
        });



        radioGroup.setOnCheckedChangeListener(this);
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String FN= fullname.getText().toString().trim();
                String dob = displaydate.getText().toString().trim();
                if(TextUtils.isEmpty(FN)){
                    fullname.setError("name is required");
                    return;
                }
                if(TextUtils.isEmpty(dob))
                {
                    Toast.makeText(registrationpage2.this," DOB is requred ",Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                openactivity3();

            }
        });
    }
    public void openactivity3()
    {
        Intent intent = new Intent(this,activity3.class);
        startActivity(intent);
    }
    @Override
    public void onCheckedChanged(RadioGroup group, int i) {
        switch (i){
            case R.id.radiobuttonfemale:
                Toast.makeText(registrationpage2.this," female ",Toast.LENGTH_SHORT).show();
              gender ="female";
                break;
            case R.id.radiobuttonmale:
                Toast.makeText(registrationpage2.this," male ",Toast.LENGTH_SHORT).show();

                gender="male";
                break;
            case R.id.radiobuttonother:
                Toast.makeText(registrationpage2.this," other ",Toast.LENGTH_SHORT).show();

                gender="other" ;

                break;
        }

    }
}
