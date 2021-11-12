package com.example.welcome.jobpost;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import  com.example.welcome.jobpost.AppConfig;

public class CompanyJobs extends AppCompatActivity {
    private EditText ed_Cjobtitle,ed_SeatsA,ed_CjobDes,ed_ExperienceR;
    private Spinner CspinnerJobC,CspinnerOsalary,CspinnerMinEdu;
    private RadioGroup rgPart;
    private Button btn_CpSubmit;
    private RadioButton Rbutton1,btn_fulltime;

    private SessionManager session;

  //  public static String URL_REGISTER = "http://192.168.5.10/androidlogin/companyjob/companyjobs.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_jobs);

        session=new SessionManager(getApplicationContext());

        ed_Cjobtitle=(EditText)findViewById(R.id.ed_Cjobtitle);
        ed_CjobDes=(EditText)findViewById(R.id.ed_CjobDes);
        ed_SeatsA=(EditText)findViewById(R.id.ed_SeatsA);
        ed_ExperienceR=(EditText)findViewById(R.id.ed_ExperienceR);
        CspinnerJobC =(Spinner)findViewById(R.id.CspinnerJobC);
        CspinnerOsalary =(Spinner)findViewById(R.id.CspinnerOsalary);
        CspinnerMinEdu =(Spinner)findViewById(R.id.CspinnerMinEdu);
        rgPart=(RadioGroup)findViewById(R.id.rgPart);
        btn_fulltime=(RadioButton)findViewById(R.id.fulltime);
        rgPart.check(btn_fulltime.getId());

        btn_CpSubmit=(Button)findViewById(R.id.btn_CpSubmit);
        btn_CpSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("errrrr","gdjhggdjhgdjhgdj");
                //  edittext bata php ma lanako lagi
                String JobTitle = ed_Cjobtitle.getText().toString();
                String JobDescription=ed_CjobDes.getText().toString();
                String SeatsAvailable = ed_SeatsA.getText().toString();
                String ExperiencedRequried = (ed_ExperienceR.getText().toString());
                String JobCategorySpinner=(CspinnerJobC.getSelectedItem().toString());
                String SalarSpinner=(CspinnerOsalary.getSelectedItem().toString());
                String MinimuEducation=(CspinnerMinEdu.getSelectedItem().toString());
                btn_fulltime=(RadioButton)findViewById(R.id.fulltime);
                Rbutton1 = (RadioButton) findViewById(rgPart.getCheckedRadioButtonId());
                String JobType=Rbutton1.getText().toString();
                rgPart.check(btn_fulltime.getId());


                if (!JobTitle.isEmpty() && !JobDescription.isEmpty()&& !SeatsAvailable.isEmpty() && !ExperiencedRequried.isEmpty()
                        && !JobCategorySpinner.isEmpty() && !SalarSpinner.isEmpty() && !MinimuEducation.isEmpty() && !JobType.isEmpty()
                        ) {
                    registerUser(JobTitle,JobDescription);
                } else {
                    Toast.makeText(getApplicationContext(),"Please enter your details!", Toast.LENGTH_LONG).show();
                }



            }
        });




    }

    private void registerUser(final String JObTile1,final String JObDes) {
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        StringRequest strReq = new StringRequest(Request.Method.POST,AppConfig.URL_CompanyJobs_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Volley Respone Error", "Register Response: " + response.toString());
                loading.dismiss();
                Toast.makeText(getApplicationContext(), "Job Posted Sucessfully !", Toast.LENGTH_LONG).show();

                // Launch login activity
                Intent intent = new Intent(getApplicationContext(), CompanyLogedInPage.class);
                startActivity(intent);
                finish();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley ERROR RESPONSE", "Registration Error: " + error.getMessage());
                loading.dismiss();
                Toast.makeText(getApplicationContext(),
                        "No internet Connection ...Please connect to network", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                 params.put("CompanyEmail",session.getEmail());
                params.put("CompanyName",session.getUserName());
                params.put("JobTitle", ed_Cjobtitle.getText().toString());
                params.put("JobDescription",ed_CjobDes.getText().toString());
                params.put("SeatsAvailable", ed_SeatsA.getText().toString());
                params.put("ExperiencedRequried",ed_ExperienceR.getText().toString());
                params.put("JobCategorySpinner",(CspinnerJobC.getSelectedItem().toString()));
                params.put("SalarSpinner",(CspinnerOsalary.getSelectedItem().toString()));
                params.put("MinimuEducation",(CspinnerMinEdu.getSelectedItem().toString()));
                Rbutton1 = (RadioButton) findViewById(rgPart.getCheckedRadioButtonId());
                params.put("PartTime",Rbutton1.getText().toString());


                return params;
            }

        };

        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }
}

