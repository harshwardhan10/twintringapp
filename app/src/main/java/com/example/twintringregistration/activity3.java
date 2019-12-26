package com.example.twintringregistration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;


public class activity3 extends AppCompatActivity {
    Button rider,go,routes,challenge,missions,logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity3);
        rider=(Button) findViewById(R.id.rider);
        routes=(Button)findViewById(R.id.routes);
        challenge = (Button)findViewById(R.id.challenge);
        go= (Button)findViewById(R.id.ride);

        missions = (Button)findViewById(R.id.mission);
        logout=findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),login.class));
            }
        });
        challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openchallengeactivity();

            }
        });
        missions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openmissionactivity();
            }
        });
        routes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openrouteactivity();
            }
        });
        rider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openrideractivity();
            }
        });
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openmapactivity();
            }
        });


    }
    void openrideractivity()
    {
        Intent intent = new Intent(this,rider.class);
        startActivity(intent);

    }
    void openrouteactivity()
    {
        Intent intent = new Intent(this,routes.class);
        startActivity(intent);

    }
    void openmissionactivity()
    {
        Intent intent = new Intent(this, mission.class);
        startActivity(intent);

    }
    void openchallengeactivity()
    {
        Intent intent = new Intent(this,challenge.class);
        startActivity(intent);

    }
    void openmapactivity()
    {
        Intent intent = new Intent(this,mappage.class);
        startActivity(intent);

    }




}
