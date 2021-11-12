package com.example.welcome.jobpost;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;

public class EditProfileJobseeker extends AppCompatActivity {
    EditText ed_JEFname,ed_JELname,JEcAddress,ed_JEmob,ed_JEDob,ed_JEemail,ed_JEusername,ed_JEPassword,ed_JEConformPassword;
    Spinner JEnationalitySpiner,JEfuncationSpinner,JEsalarySpinner;
    RadioGroup JEgenderRadioB;
    RadioButton   rbtn_JEmale,rbtn_female,rbtn_JEothers;
    Button save;
    JobseekerProductionModule pd;
    SQLiteHandler dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_jobseeker);
        ed_JEFname=(EditText)findViewById(R.id.ed_JEFname);
        ed_JELname=(EditText)findViewById(R.id.ed_JELname);
        JEcAddress=(EditText)findViewById(R.id.ed_JEcAddress);
        ed_JEmob=(EditText)findViewById(R.id.ed_JEmob);
        ed_JEDob=(EditText)findViewById(R.id.ed_JEDob);
        ed_JEemail=(EditText)findViewById(R.id.ed_JEemail);
        ed_JEusername=(EditText)findViewById(R.id.ed_JEusername);
        ed_JEPassword=(EditText)findViewById(R.id.ed_JEPassword);
        ed_JEConformPassword=(EditText)findViewById(R.id.ed_JEConformPassword);

        JEnationalitySpiner=(Spinner)findViewById(R.id.JEnationalitySpiner);
        JEfuncationSpinner=(Spinner)findViewById(R.id.JEfuncationSpinner);
        JEsalarySpinner=(Spinner)findViewById(R.id.JEsalarySpinner);

        JEgenderRadioB=(RadioGroup)findViewById(R.id.JEgenderRadioB);
        rbtn_JEmale=(RadioButton)findViewById(R.id.rbtn_JEmale);
        rbtn_female=(RadioButton) findViewById(R.id.rbtn_female);
        rbtn_JEothers=(RadioButton) findViewById(R.id.rbtn_JEothers);



        dbhelper = new SQLiteHandler(getApplicationContext());


        // Fetching user details from sqlite
        HashMap<String, String> user = dbhelper.getUserDetails();
        ed_JEFname.setText(user.get("FirstName"));
        ed_JELname.setText(user.get("LastName"));
        ed_JEmob.setText(user.get("mobile"));
        ed_JEemail.setText(user.get("email"));
        ed_JEusername.setText(user.get("name"));
        ed_JEDob.setText(user.get("DOB"));


        //JEgenderRadioB.(user.get("Gender"));
       // JEnationalitySpiner.getSelectedItem(user.get("Nationality"));
        JEcAddress.setText(user.get("CurrentAddress"));
        //JEfuncationSpinner.setText(user.get("FunctionalArea"));
        //JEsalarySpinner.setText(user.get("Salary"));




    }
}
