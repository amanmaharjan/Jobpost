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
import com.example.welcome.jobpost.AppConfig;
import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity  {
    EditText firstname,lastname,address,mobile,dob,email,username,password,conformPassword;
    Spinner nationality,fuctionalarea,salary;
    RadioButton rbutton,rbtnmale;
    RadioGroup rg;
    Button edit;
    private SQLiteHandler db;
    private SessionManager session;
    SQLiteHandler dbhelper;

    // Server user register url
    //public static String URL_REGISTER = "http://192.168.5.10/androidlogin/update/update.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());


        firstname = (EditText)findViewById(R.id.ed_JeFname);
        lastname = (EditText)findViewById(R.id.ed_JeLname);
        address = (EditText)findViewById(R.id.ed_JecAddress);
        mobile = (EditText)findViewById(R.id.ed_Jemob);
        dob = (EditText)findViewById(R.id.ed_JeDob);
        email = (EditText)findViewById(R.id.ed_Jeemail);
        username = (EditText)findViewById(R.id.ed_Jeusername);
        password= (EditText)findViewById(R.id.ed_JePassword);
        conformPassword=(EditText)findViewById(R.id.ed_JeConformPassword);
        nationality = (Spinner) findViewById(R.id.JeNationalitySpiner);
        fuctionalarea = (Spinner) findViewById(R.id.JefuncationSpinner);
        salary = (Spinner) findViewById(R.id.JeSalarySpinner);
        rg=(RadioGroup)findViewById(R.id.genderJeRadioB);
        rbtnmale=(RadioButton)findViewById(R.id.rbtn_Jemale);
        rg.check(rbtnmale.getId());


        edit= (Button)findViewById(R.id.btn_Jeedit);
        edit.setOnClickListener(new View.OnClickListener() {
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
                String Gender=rbutton.getText().toString();
                if (!name.isEmpty() && !emailAddress.isEmpty() && !passWord.isEmpty()&&!FirstName.isEmpty()&&!LastName.isEmpty()
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
        });


        disPaly();
    }
    public void disPaly(){
        dbhelper = new SQLiteHandler(getApplicationContext());

        // Fetching user details from sqlite
        HashMap<String, String> user = dbhelper.getUserDetails();
        firstname.setText(user.get("FirstName"));
        lastname.setText(user.get("LastName"));
        mobile.setText(user.get("mobile"));
        address.setText(user.get("CurrentAddress"));
        dob.setText(user.get("DOB"));
        email.setText(user.get("email"));
        username.setText(user.get("name"));

    }
    private void registerUser(final String name, final String email1,final String password1) {
        final ProgressDialog loading = ProgressDialog.show(this, "Updating...", "Please wait...", false, false);
        StringRequest strReq = new StringRequest(Request.Method.POST,AppConfig.URL_Edit_Jobseeker, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Volley Respone Error", "Register Response: " + response.toString());
                Toast.makeText(getApplicationContext(), "User successfully Updated! Please Try Login" , Toast.LENGTH_LONG).show();
              loading.dismiss();
                // logging out the user annd Launch login activity
                logoutUser();

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
    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false,"","","");
        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(getApplicationContext(), Welcome.class);
        startActivity(intent);
        finish();
    }
}
