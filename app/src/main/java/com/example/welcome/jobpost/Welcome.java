package com.example.welcome.jobpost;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import com.example.welcome.jobpost.AppConfig;

public class Welcome extends AppCompatActivity implements View.OnClickListener {
    Button btn_jobseeker,btn_employer,btn_jobseekerSignup,btn_employerSignup;
    EditText username,passWord;
    TextView resetJobseekr,resetCompany;

    //SessionManager Reference
   // private SessionManager session;
    private SessionManager session;
    //DAtabase Reference
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

                //Splash screen pachi ko activities...Main Activity
        btn_jobseeker =  (Button)findViewById(R.id.btn_jobseeker);
        btn_employer =  (Button)findViewById(R.id.btn_employer);
        btn_jobseekerSignup =  (Button)findViewById(R.id.btn_jobseekerSignup);
        btn_employerSignup =  (Button)findViewById(R.id.btn_employerSignup);
        username=(EditText)findViewById(R.id.ed_us);
        passWord=(EditText)findViewById(R.id.ed_pw);

        resetJobseekr=(TextView)findViewById(R.id.resetJobseeker);
        resetCompany=(TextView)findViewById(R.id.resetCompany);
        //Initialzing the button on click
        btn_jobseekerSignup.setOnClickListener(this);
        btn_employerSignup.setOnClickListener(this);
        btn_jobseeker.setOnClickListener(this);
        btn_employer.setOnClickListener(this);

        resetJobseekr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(getApplicationContext(),ResetPasswordJobSeeker.class);
                startActivity(in);
            }
        });
        resetCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(getApplicationContext(),ResetPasswordCompany.class);
                startActivity(in);
            }
        });
        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
     if (session.isLoggedIn()) {

            // User is already logged in. Take him to main activity
            if(session.getUser().contentEquals("JobSeeker"))
            {
                Intent intent = new Intent(getApplicationContext(), JobLogedInPage.class);
                startActivity(intent);
                finish();
            }
            else if(session.getUser().contentEquals("Company"))
            {
                Intent intent = new Intent(getApplicationContext(), CompanyLogedInPage.class);
                startActivity(intent);
                finish();

            }
       // Toast.makeText(getApplicationContext(),session.getEmail(),Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_jobseekerSignup:
                Intent i1 =new Intent(Welcome.this,SignUpJobSeeker.class);
                startActivity(i1);
                break;
            case R.id.btn_employerSignup:
                Intent i2 =new Intent(Welcome.this,SignUpCompany.class);
                startActivity(i2);
                break;
            case R.id.btn_employer:
                String companyEmail = username.getText().toString().trim();
                String companyPassword = passWord.getText().toString().trim();

                // Check for empty data in the form
                if (!companyEmail.isEmpty() && !companyPassword.isEmpty()) {
                    // login user
                    checkCompanyLogin(companyEmail, companyPassword);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
                break;
            case R.id.btn_jobseeker:
                String email = username.getText().toString().trim();
                String password = passWord.getText().toString().trim();

                // Check for empty data in the form
                if (!email.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin(email, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }


                break;

        }

    }
    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        StringRequest strReq = new StringRequest(Request.Method.POST,AppConfig.URL_Jobseeker_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("VOLLEY RESPONNSE", "Login Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                       // session.setLogin(true);

                        // Now store the user in SQLite
                        String uid = jObj.getString("uid");
                        JSONObject user = jObj.getJSONObject("user");
                        String FirstName=user.getString("FirstName");
                        String LastName=user.getString("LastName");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String CurrentAddress=user.getString("CurrentAddress");
                        String mobile=user.getString("mobile");
                        String DOB=user.getString("DOB");
                        String Nationality=user.getString("Nationality");
                        String FunctionalArea=user.getString("FunctionalArea");
                        String Salary=user.getString("Salary");
                        String Gender = user.getString("Gender");
                        String created_at = user.getString("created_at");

                        session.setFirstname(FirstName,LastName);
                        session.setLogin(true,email,"JobSeeker",FirstName);


                        // Inserting row in users table
                        db.addUser1(FirstName,LastName,name, email,CurrentAddress,mobile,DOB,Nationality,
                            FunctionalArea,Salary,Gender, uid, created_at);

                        // Launch main activity
                        Intent intent = new Intent(Welcome.this,JobLogedInPage.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY ERROR", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "No internet Connection ...Please connect to network", Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }

        };

        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }

    private void checkCompanyLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";


        StringRequest strReq = new StringRequest(Request.Method.POST,AppConfig.URL_COMPANY_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("VOLLEY RESPONNSE", "Login Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        //session.setLogin(true);


                        // Now store the user in SQLite
                        String uid = jObj.getString("uid");
                        JSONObject user = jObj.getJSONObject("user");
                        String Companyname=user.getString("Companyname");
                        String CompanyAddress=user.getString("CompanyAddress");
                        String email = user.getString("email");
                        String ContactPersonName = user.getString("ContactPersonName");
                        String Mobilenumber=user.getString("Mobilenumber");
                        String about=user.getString("about");
                        session.setLogin(true,email,"Company",Companyname);
                        session.setFirstname(Companyname,"");

                        // Inserting row in users tabl
                       db.addCompanyData(Companyname,CompanyAddress,email,ContactPersonName, Mobilenumber,about);
                                                // Launch main activity
                        Intent intent = new Intent(Welcome.this,CompanyLogedInPage.class);
                        startActivity(intent);
                       // Toast.makeText(getApplicationContext(),Companyname+CompanyAddress,Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY ERROR", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "No internet Connection ...Please connect to network", Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }

        };

        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }
    private boolean checkInternetConenction() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec
                =(ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() ==
                android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
            Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            return true;
        }else if (
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() ==
                                android.net.NetworkInfo.State.DISCONNECTED  ) {
            Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }



}

