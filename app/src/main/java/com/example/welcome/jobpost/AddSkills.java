package com.example.welcome.jobpost;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class AddSkills extends AppCompatActivity {
    EditText Instituename,TrainingTitle,Duration;
    Button submmit;

    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_skills);

        Instituename=(EditText)findViewById(R.id.Tname);
        TrainingTitle=(EditText)findViewById(R.id.Ttitle);
        Duration=(EditText)findViewById(R.id.Tduration);
        submmit=(Button)findViewById(R.id.btn_Tsubmit);

        submmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String IName = Instituename.getText().toString();
                String Title = TrainingTitle.getText().toString();
                String TDuration = Duration.getText().toString();

                session=new SessionManager(getApplicationContext());


                if (!IName.isEmpty() && !Title.isEmpty() && !TDuration.isEmpty()) {
                    registerUser();

                } else {
                    Toast.makeText(getApplicationContext(),"Please enter all!", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
    private void registerUser() {
        StringRequest strReq = new StringRequest(Request.Method.POST,AppConfig.URL_Jobseeker_Training , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Volley Respone Error", "Register Response: " + response.toString());
                Toast.makeText(getApplicationContext(), "Training added Sucessfully !", Toast.LENGTH_LONG).show();
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
                params.put("InstituteName", Instituename.getText().toString());
                params.put("TrainingTitle", TrainingTitle.getText().toString());
                params.put("Duration", Duration.getText().toString());
                 return params;
            }

        };

        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }
    private void clearAll(){
        Instituename.setText("");
        TrainingTitle.setText("");
        Duration.setText("");;

    }
}
