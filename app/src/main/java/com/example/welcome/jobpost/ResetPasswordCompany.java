package com.example.welcome.jobpost;

import android.content.Intent;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResetPasswordCompany extends AppCompatActivity {
    EditText email,password,confirmPassword;
    Button resetPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_company);
        email=(EditText)findViewById(R.id.CresetEmail);
        password=(EditText)findViewById(R.id.CresetPassword);
        confirmPassword=(EditText)findViewById(R.id.CresetConfirmPassword);
        resetPassword=(Button)findViewById(R.id.CresetbtnPassword);
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!email.getText().toString().isEmpty()&& !password.getText().toString().isEmpty()&& !confirmPassword.getText().toString().isEmpty()) {
                    if (password.getText().toString().contentEquals(confirmPassword.getText().toString())) {
                        resetPassword();
                        // Launch login activity
                        Intent intent = new Intent(getApplicationContext(), Welcome.class);
                        startActivity(intent);
                        finish();



                    } else {
                        Toast.makeText(getApplicationContext(), "Password Missmatch", Toast.LENGTH_LONG).show();

                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Fill up all the field",Toast.LENGTH_LONG).show();

                }


            }
        });

    }
    // module for volley connetcion and its respone
    private void resetPassword() {
        StringRequest strReq = new StringRequest(Request.Method.POST,AppConfig.URL_Company_RESET , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Volley Respone Error", "Register Response: " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    // Toast.makeText(getApplicationContext(),obj.getString("message"), Toast.LENGTH_SHORT).show();

                    String message = obj.getString("error_msg");
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email.getText().toString());
                params.put("password",password.getText().toString());


                return params;
            }

        };

        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }
}
