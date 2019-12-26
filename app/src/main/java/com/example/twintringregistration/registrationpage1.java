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

public class registrationpage1 extends AppCompatActivity {
    EditText  memail , mpassword , mphone;
    Button mgobtn;
    TextView mloginbtn;
    ProgressBar progressBar;
    FirebaseAuth fAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrationpage1);
        memail = findViewById(R.id.email);
        mpassword = findViewById(R.id.password);
        mphone = findViewById(R.id.phone);
        mgobtn = (Button) findViewById(R.id.gobutton);
        mloginbtn = findViewById(R.id.createtext);
        fAuth=FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        if(fAuth.getCurrentUser()!=null){

            startActivity(new Intent(getApplicationContext(),activity3.class));
            finish();
        }

mloginbtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        openloginactivity();
    }
});
        mgobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = memail.getText().toString().trim();
                String password = mpassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    memail.setError("email is required");
                    return;
                }

                if(mphone.getText().toString().isEmpty() ) {
                    mphone.setError("phone number is required ");
                    return;
                }
                if(mphone.getText().toString().length()<10)
                {
                    mphone.setError("invalid number");
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
                //
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful()){
                           Toast.makeText(registrationpage1.this," user created ",Toast.LENGTH_SHORT).show();
                           startActivity(new Intent(getApplicationContext(),registrationpage2.class));

                       }else{
                          Toast.makeText(registrationpage1.this," error! . ." +task.getException().getMessage(),Toast.LENGTH_SHORT).show();;
                           progressBar.setVisibility(View.GONE);

                       }
                    }
                });

                openregistrationpage2();


            }

        });

    }
    public  void openregistrationpage2()
    {

        Intent intent = new Intent(this,registrationpage2.class);
        startActivity(intent);
    }
    public void openloginactivity()
    {
        Intent intent = new Intent(this,login.class);
        startActivity(intent);
    }
}

