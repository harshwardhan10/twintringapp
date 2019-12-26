package com.example.twintringregistration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
EditText memail,mpassword;
Button mloginbtn;
ProgressBar progressBar;
FirebaseAuth fAuth;
TextView  mcreatebtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        memail=findViewById(R.id.emailid);
        mpassword=findViewById(R.id.password);
        mloginbtn=findViewById(R.id.loginbtn);
        progressBar=findViewById(R.id.progressBar3);
        mcreatebtn=findViewById(R.id.createbutton);
        fAuth=FirebaseAuth.getInstance();
        mcreatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openregisteractivity();
            }
        });
        mloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = memail.getText().toString().trim();
                String password = mpassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    memail.setError("email is required");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mpassword.setError("password is requried");
                    return;
                }

                if(password.length()<6){
                    mpassword.setError("password must be >=6 character");
                    return;

                }
                progressBar.setVisibility(View.VISIBLE);
                //authentication of user
               fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful())
                       {
                           Toast.makeText(login.this," logged in successfully",Toast.LENGTH_SHORT).show();
                           startActivity(new Intent(getApplicationContext(),activity3.class));

                       }
                       else{
                           Toast.makeText(login.this," error! . ." +task.getException().getMessage(),Toast.LENGTH_SHORT).show();;
                           progressBar.setVisibility(View.GONE);


                       }

                   }
               });
            }
        });

    }
    public void openregisteractivity()
    {
        Intent intent = new Intent(this,registrationpage1.class);
        startActivity(intent);

    }
}
