package com.example.welcome.jobpost;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class JptLoginPage extends AppCompatActivity {
    Button btnSignIn,btnSignUp;
    JobseekerDatabaseHelper loginDataBaseAdapter;
    EditText editTextUserName,editTextPassword;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jpt_login_page);
         editTextUserName=(EditText)findViewById(R.id.editTextUserNameToLogin);
         editTextPassword=(EditText)findViewById(R.id.editTextPasswordToLogin);
        btnSignIn=(Button)findViewById(R.id.buttonSignIN);
        btnSignUp=(Button)findViewById(R.id.buttonSignUP);

        loginDataBaseAdapter=new JobseekerDatabaseHelper(this);
       // loginDataBaseAdapter=loginDataBaseAdapter.onOpen();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSignUP=new Intent(getApplicationContext(),SignUpJobSeeker.class);
                startActivity(intentSignUP);
            }
        });


                btnSignIn.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        // get The User name and Password
                        String userName=editTextUserName.getText().toString();
                        String password=editTextPassword.getText().toString();

                        // fetch the Password form database for respective user name
                        String storedPassword=loginDataBaseAdapter.getSinlgeEntry(userName);

                        // check if the Stored password matches with  Password entered by user
                        if(password.equals(storedPassword))
                        {
                            Toast.makeText(getApplicationContext(), "Congrats: Login Successfull", Toast.LENGTH_LONG).show();

                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "User Name or Password does not match", Toast.LENGTH_LONG).show();
                        }
                    }
                });







    }
}
