package com.example.welcome.jobpost;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Education extends AppCompatActivity {
    private EditText JedIName,JedPassedY,JedGrade;
    private  Spinner Jspinner;
    private Button btn_jsubmit;
    public SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education);
        btn_jsubmit = (Button)findViewById(R.id.btn_jsubmit);
        JedIName = (EditText)findViewById(R.id.JedIName);
        JedPassedY = (EditText)findViewById(R.id.JedPassedY);
        JedGrade = (EditText)findViewById(R.id.JedGrade);
        Jspinner = (Spinner) findViewById(R.id.Jspinner);
        session=new SessionManager(getApplicationContext());

        btn_jsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String InstituteName = JedIName.getText().toString();
                String PassedYear = JedPassedY.getText().toString();
                String Grade = JedGrade.getText().toString();
                String EducationlQualification=(Jspinner.getSelectedItem().toString());

                if (!InstituteName.isEmpty() && !PassedYear.isEmpty() && !Grade.isEmpty()&&!EducationlQualification.isEmpty()) {
                    registerUser();

                } else {
                    Toast.makeText(getApplicationContext(),"Please enter all!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void registerUser() {
        StringRequest strReq = new StringRequest(Request.Method.POST,AppConfig.URL_Jobseeker_Education , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Volley Respone Error", "Register Response: " + response.toString());
                Toast.makeText(getApplicationContext(), "Education added Sucessfully !", Toast.LENGTH_LONG).show();
                clearAll();

                // Launch login activity
                // Intent intent = new Intent(getApplicationContext(), JobLogedInPage.class);
                //startActivity(intent);
                //finish();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley ERROR RESPONSE", "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "No internet Connection ...Please connect to network", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                //onRegister();
                // get selected radio button from radioGroup
                // find the radiobutton by returned id



                Map<String, String> params = new HashMap<String, String>();
                params.put("email", session.getEmail());
                params.put("InstituteName", JedIName.getText().toString());
                params.put("PassedYear", JedPassedY.getText().toString());
                params.put("Grade", JedGrade.getText().toString());



                params.put("EducationlQualification",Jspinner.getSelectedItem().toString());


                return params;
            }

        };

        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }
    private void clearAll(){
        JedIName.setText("");
        JedPassedY.setText("");
        JedGrade.setText("");;

    }
}
