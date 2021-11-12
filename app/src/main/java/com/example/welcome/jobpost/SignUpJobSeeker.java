package com.example.welcome.jobpost;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import com.example.welcome.jobpost.AppConfig;

import java.util.HashMap;
import java.util.Map;

public class SignUpJobSeeker extends AppCompatActivity{
    Button jsignup;
    JobseekerProductionModule jpd;
    private SessionManager session;
    private SQLiteHandler db;
    String Gender="";
    //private JobseekerDatabaseHelper db;


    //referencing the components
    EditText firstname,lastname,address,mobile,dob,email,username,password,conformPassword;
    Spinner nationality,fuctionalarea,salary;
    RadioButton rbutton,rmale;
    RadioGroup rg;
     // Server user login url
     //public static String URL_LOGIN = "http://192.168.6.161/androidlogin/login.php";

     // Server user register url
   // public static String URL_REGISTER = "http://192.168.1.2/androidlogin/register.php";

    // Server user login url
    //public static String URL_LOGIN = "http://192.168.5.10/androidlogin/jobseeker/login.php";

    // Server user register url
   //public static String URL_REGISTER = "http://192.168.5.10/androidlogin/jobseeker/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());
        //db = new JobseekerDatabaseHelper(getApplicationContext());


        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(getApplicationContext(),JobLogedInPage.class);
            startActivity(intent);
            finish();
        }

        //initilazing components // chinako
        setContentView(R.layout.activity_sign_up_job_seeker);
        jsignup = (Button)findViewById(R.id.btnJsignUp);
         firstname = (EditText)findViewById(R.id.ed_JFname);
        lastname = (EditText)findViewById(R.id.ed_JLname);
        address = (EditText)findViewById(R.id.ed_JcAddress);
        mobile = (EditText)findViewById(R.id.ed_Jmob);
        dob = (EditText)findViewById(R.id.ed_JDob);
        email = (EditText)findViewById(R.id.ed_Jemail);
        username = (EditText)findViewById(R.id.ed_Jusername);
        password= (EditText)findViewById(R.id.ed_JPassword);
        conformPassword=(EditText)findViewById(R.id.ed_JConformPassword);
        nationality = (Spinner) findViewById(R.id.nationalitySpiner);
        fuctionalarea = (Spinner) findViewById(R.id.funcationSpinner);
        salary = (Spinner) findViewById(R.id.salarySpinner);
        rmale=(RadioButton)findViewById(R.id.rbtn_male);

        rg=(RadioGroup)findViewById(R.id.genderRadioB);
        rg.check(rmale.getId());
        jsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = username.getText().toString();
                String emailAddress = email.getText().toString();
                String passWord = password.getText().toString();
                String conformpassword=conformPassword.getText().toString();
                String FirstName = (firstname.getText().toString());
                String LastName = (lastname.getText().toString());
                String CurrentAddress = (address.getText().toString());
                String Mobilenumber = (mobile.getText().toString());
                String DOB = (dob.getText().toString());
                String UserName = (username.getText().toString());
                String NationalitySpinner=(nationality.getSelectedItem().toString());
                String FunctonalAreaSpinner=(fuctionalarea.getSelectedItem().toString());
                String SalarySpinner=(salary.getSelectedItem().toString());
                rbutton = (RadioButton) findViewById(rg.getCheckedRadioButtonId());
                 //Gender=rbutton.getText().toString();
                if (!name.isEmpty() && !emailAddress.isEmpty() && !passWord.isEmpty()&&!conformpassword.isEmpty()&&!FirstName.isEmpty()&&!LastName.isEmpty()
                        &&!CurrentAddress.isEmpty()&&!Mobilenumber.isEmpty()&&!DOB.isEmpty()&&!UserName.isEmpty()
                        &&!NationalitySpinner.isEmpty()&&!FunctonalAreaSpinner.isEmpty()&&!SalarySpinner.isEmpty()&&!rbutton.getText().toString().isEmpty()) {
                    if(passWord.contentEquals(conformpassword)){
                        registerUser(name, emailAddress, passWord);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Password MissMatch \n"+passWord +"\n"+conformpassword, Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(),"Please enter your details!", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

/*
    @Override
    public void onClick(View v) {
        //onRegister();
        String name = username.getText().toString();
        String emailAddress = email.getText().toString();
        String passWord = password.getText().toString();
        String conformpassword=conformPassword.getText().toString();
        String FirstName = (firstname.getText().toString());
        String LastName = (lastname.getText().toString());
        String CurrentAddress = (address.getText().toString());
        String Mobilenumber = (mobile.getText().toString());
        String DOB = (dob.getText().toString());
        String UserName = (username.getText().toString());
        String NationalitySpinner=(nationality.getSelectedItem().toString());
        String FunctonalAreaSpinner=(fuctionalarea.getSelectedItem().toString());
        String SalarySpinner=(salary.getSelectedItem().toString());
        rbutton = (RadioButton) findViewById(rg.getCheckedRadioButtonId());
        String Gender=rbutton.getText().toString();
         if (!name.isEmpty() && !emailAddress.isEmpty() && !passWord.isEmpty()&&!conformpassword.isEmpty()&&!FirstName.isEmpty()&&!LastName.isEmpty()
                 &&!CurrentAddress.isEmpty()&&!Mobilenumber.isEmpty()&&!DOB.isEmpty()&&!UserName.isEmpty()
                 &&!NationalitySpinner.isEmpty()&&!FunctonalAreaSpinner.isEmpty()&&!SalarySpinner.isEmpty()&&!Gender.isEmpty()) {
            if(passWord.toString().contentEquals(conformpassword.toString())){
               registerUser(name, emailAddress, passWord);
               }
            else{
                Toast.makeText(getApplicationContext(),"Password MissMatch \n"+passWord +"\n"+conformpassword, Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(getApplicationContext(),"Please enter your details!", Toast.LENGTH_LONG).show();
        }

    }
    */
    public void onRegister()
    {
        jpd= new JobseekerProductionModule();
        jpd.pFirstName = (firstname.getText().toString());
        jpd.pLastName = (lastname.getText().toString());
        jpd.pCurrentAddress = (address.getText().toString());
        jpd.pMobilenumber = (mobile.getText().toString());
        jpd.pDOB = (dob.getText().toString());
        jpd.pEmail = (email.getText().toString());
        jpd.pUserName = (username.getText().toString());
        jpd.pPassword = (password.getText().toString());
        jpd.pNationalitySpinner=(nationality.getSelectedItem().toString());
        jpd.pFunctonalAreaSpinner=(fuctionalarea.getSelectedItem().toString());
        jpd.pSalarySpinner=(salary.getSelectedItem().toString());

        // get selected radio button from radioGroup
        // find the radiobutton by returned id
        rbutton = (RadioButton) findViewById(rg.getCheckedRadioButtonId());
        jpd.pGenderRadioB=rbutton.getText().toString();
    }
    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void registerUser(final String name, final String email1,final String password1) {
         StringRequest strReq = new StringRequest(Request.Method.POST,AppConfig.URL_Jobseeker_REGISTER , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Volley Respone Error", "Register Response: " + response.toString());
                Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();
              /*
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session

                        // Now store the user in SQLite
                        String uid = jObj.getString("uid");
                        JSONObject user = jObj.getJSONObject("user");
                        String FirstName = user.getString("FirstName");
                        String LastName = user.getString("LastName");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String CurrentAddress = user.getString("CurrentAddress");
                        String mobile = user.getString("mobile");
                        String DOB = user.getString("DOB");
                        String Nationality = user.getString("Nationality");
                        String FunctionalArea = user.getString("FunctionalArea");
                        String Salary = user.getString("Salary");
                        String Gender = user.getString("Gender");
                        String created_at = user.getString("created_at");
                        // Launch login activity
                        Intent intent = new Intent(getApplicationContext(), JobLogedInPage.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                */
                // Launch login activity
                Intent intent = new Intent(getApplicationContext(), JobLogedInPage.class);
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
                // Posting params to register url
                //onRegister();
               // get selected radio button from radioGroup
                // find the radiobutton by returned id
                rbutton = (RadioButton) findViewById(rg.getCheckedRadioButtonId());


                Map<String, String> params = new HashMap<String, String>();
                params.put("FirstName", firstname.getText().toString());
                params.put("LastName", lastname.getText().toString());
                params.put("name", username.getText().toString());
                params.put("email", email.getText().toString());
                params.put("password", password.getText().toString());
                params.put("CurrentAddress",address.getText().toString());
                params.put("mobile",mobile.getText().toString());
                params.put("DOB",dob.getText().toString());
                params.put("Nationality",nationality.getSelectedItem().toString());
                params.put("FunctionalArea",fuctionalarea.getSelectedItem().toString());
                params.put("Salary",salary.getSelectedItem().toString());
                params.put("Gender",rbutton.getText().toString());

                return params;
            }

        };

        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }
}
