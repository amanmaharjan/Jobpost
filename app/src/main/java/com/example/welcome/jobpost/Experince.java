package com.example.welcome.jobpost;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Experince extends AppCompatActivity {
    private EditText JedCompanyN,JedJobTitle,JedDuratonOw;
    private  Spinner JSpinerWA;
    private Button JExSubmit;
    public SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experince);
        JExSubmit = (Button)findViewById(R.id.JExSubmit);
        JedCompanyN = (EditText)findViewById(R.id.JedCompanyN);
        JedJobTitle = (EditText)findViewById(R.id.JedJobTitle);
        JedDuratonOw = (EditText)findViewById(R.id.JedDuratonOw);
        JSpinerWA = (Spinner) findViewById(R.id.JSpinerWA);
        session=new SessionManager(getApplication());

        JExSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String CompanyName = JedCompanyN.getText().toString();
                String JobTitle = JedJobTitle.getText().toString();
                String DurationOfWork = JedDuratonOw.getText().toString();
                String FuctionArea=(JSpinerWA.getSelectedItem().toString());
                Log.d("jpt"+CompanyName,"hsjhdj");
                if (!CompanyName.isEmpty() && !JobTitle.isEmpty() && !DurationOfWork.isEmpty()&&!FuctionArea.isEmpty()) {
                    registerUser();
                } else {
                    Toast.makeText(getApplicationContext(),"Please enter all!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void registerUser() {
        StringRequest strReq = new StringRequest(Request.Method.POST,AppConfig.URL_Jobseeker_Experience , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Volley Respone Error", "Register Response: " + response.toString());
                Toast.makeText(getApplicationContext(), "Experince Added Sucessfully!", Toast.LENGTH_LONG).show();
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
                params.put("CompanyName", JedCompanyN.getText().toString());
                params.put("JobTitle", JedJobTitle.getText().toString());
                params.put("DurationOfWork", JedDuratonOw.getText().toString());
                params.put("FuctionArea",JSpinerWA.getSelectedItem().toString());

                return params;
            }

        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }
    private void clearAll(){
        JedCompanyN.setText("");
        JedJobTitle.setText("");
        JedDuratonOw.setText("");

    }
}
