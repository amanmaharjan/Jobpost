package com.example.welcome.jobpost;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.net.ConnectivityManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.example.welcome.jobpost.AppConfig;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class SignUpCompany extends AppCompatActivity {
    EditText CompanyName,Address,Email,ContactPerson,ContactPersonPhone,Username,Password,ConformPassword,About;
    Button SignUp,choose;
    ImageView img;
    SessionManager session;


    // Server user register url
   // public static String URL_REGISTER = "http://192.168.5.10/androidlogin/company/register.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_company);

        //initializing the components
        CompanyName=(EditText)findViewById(R.id.ed_Cname);
        Address=(EditText)findViewById(R.id.ed_Caddress);
        Email=(EditText)findViewById(R.id.ed_Cemail);
        ContactPerson=(EditText)findViewById(R.id.ed_CcontactPerson);
        ContactPersonPhone=(EditText)findViewById(R.id.ed_CcontactPersonPhone);
        Username=(EditText)findViewById(R.id.ed_Cusername);
        Password=(EditText)findViewById(R.id.ed_CPassword);
        ConformPassword=(EditText)findViewById(R.id.ed_CConfirmPassword);
        About=(EditText)findViewById(R.id.ed_Cabout);
        SignUp=(Button)findViewById(R.id.btnCsignUp);
        choose=(Button)findViewById(R.id.btn_choosefile);

        img=(ImageView)findViewById(R.id.img_logo);

        session=new SessionManager(getApplicationContext());

        //set Action on Signup button
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Companyname = CompanyName.getText().toString();
                String CompanyAddress=Address.getText().toString();
                String emailAddress = Email.getText().toString();
                String ContactPersonName = (ContactPerson.getText().toString());
                String Mobilenumber = (ContactPersonPhone.getText().toString());
                String passWord = Password.getText().toString();
                String conformpassword=ConformPassword.getText().toString();
                String username = (Username.getText().toString());
                String about = (About.getText().toString());

                if (!Companyname.isEmpty() && !CompanyAddress.isEmpty() && !emailAddress.isEmpty() && !ContactPersonName.isEmpty()
                        && !Mobilenumber.isEmpty() && !passWord.isEmpty() && !conformpassword.isEmpty() && !username.isEmpty()
                        && !about.isEmpty()) {
                    if(passWord.toString().contentEquals(conformpassword.toString())){
                       //Toast.makeText(getApplicationContext(),"SIGN SUCESS",Toast.LENGTH_SHORT).show();
                        registerUser(emailAddress,passWord);

                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Password MissMatch \n"+passWord +"\n"+conformpassword, Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(),"Please enter your details!", Toast.LENGTH_LONG).show();
                }

            }
        });

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                       }
        });




    }
    // module for volley connetcion and its respone
    private void registerUser(final String email1,final String password1) {
        StringRequest strReq = new StringRequest(Request.Method.POST,AppConfig.URL_Company_REGISTER , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Volley Respone Error", "Register Response: " + response.toString());
                Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                // Launch login activity
                Intent intent = new Intent(getApplicationContext(), Welcome.class);
                startActivity(intent);
                finish();
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
                params.put("Companyname", CompanyName.getText().toString());
                params.put("CompanyAddress",Address.getText().toString());
                params.put("emailAddress", Email.getText().toString());
                params.put("ContactPersonName",ContactPerson.getText().toString());
                params.put("Mobilenumber",ContactPersonPhone.getText().toString());
                params.put("passWord", Password.getText().toString());
                params.put("username",Username.getText().toString());
                params.put("about",About.getText().toString());

                return params;
            }

        };

        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }

}
