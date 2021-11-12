package com.example.welcome.jobpost;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class EditCompanyProfile extends AppCompatActivity {

    EditText CompanyName,Address,Email,ContactPerson,ContactPersonPhone,Username,Password,ConformPassword,About;
    Button Update,choose;
    ImageView img;
    private SQLiteHandler db;
    private SessionManager session;
    SQLiteHandler dbhelper;
    String  Companyname,CompanyAddress,emailAddress,ContactPersonName,Mobilenumber,passWord,conformpassword,username,about;

    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;
    File directory;
    String imagePath="";

    // Server user register url
   // public static String URL_REGISTER = "http://192.168.5.10/androidlogin/update_company/update.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_company_profile);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());


        //initializing the components
        CompanyName=(EditText)findViewById(R.id.ed_EdCname);
        Address=(EditText)findViewById(R.id.ed_EdCaddress);
        Email=(EditText)findViewById(R.id.ed_EdCemail);
        ContactPerson=(EditText)findViewById(R.id.ed_EdCcontactPerson);
        ContactPersonPhone=(EditText)findViewById(R.id.ed_EdCcontactPersonPhone);
        Username=(EditText)findViewById(R.id.ed_EdCusername);
        Password=(EditText)findViewById(R.id.ed_EdCPassword);
        ConformPassword=(EditText)findViewById(R.id.ed_EdCConfirmPassword);
        About=(EditText)findViewById(R.id.ed_EdCabout);
        Update=(Button)findViewById(R.id.btn_EdCUpdate);
        img=(ImageView)findViewById(R.id.img_logo);
        choose=(Button)findViewById(R.id.btn_Edchoosefile);
        //call the display function
        display();
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Companyname = CompanyName.getText().toString();
                 CompanyAddress=Address.getText().toString();
                 emailAddress = Email.getText().toString();
                 ContactPersonName = (ContactPerson.getText().toString());
                 Mobilenumber = (ContactPersonPhone.getText().toString());
                 passWord = Password.getText().toString();
                 conformpassword=ConformPassword.getText().toString();
                 username = (Username.getText().toString());
                 about = (About.getText().toString());

                if (!Companyname.isEmpty() && !CompanyAddress.isEmpty() && !emailAddress.isEmpty() && !ContactPersonName.isEmpty()
                        && !Mobilenumber.isEmpty() && !passWord.isEmpty() && !conformpassword.isEmpty() && !username.isEmpty()
                        && !about.isEmpty()) {
                    if(passWord.toString().contentEquals(conformpassword.toString())) {
                        if (img.getDrawable()==null) {
                            Toast.makeText(getApplicationContext(),"Please selsect the Company Logo",Toast.LENGTH_SHORT).show();

                        }
                        else {

                        //Toast.makeText(getApplicationContext(),"SIGN SUCESS",Toast.LENGTH_SHORT).show();
                        registerUser(emailAddress, passWord);
                        // uploadImage();
                        }
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



    private void display()
    {
        // SqLite database handler
        dbhelper = new SQLiteHandler(getApplicationContext());

        // Fetching user details from sqlite
        // Displaying the user details on the screen
        HashMap<String, String> user = dbhelper.getCompanyDetails();
        CompanyName.setText(user.get("CompanyName"));
        Address.setText(user.get("Address"));
        Email.setText(user.get("email"));
        ContactPerson.setText(user.get("Contact Person"));
        ContactPersonPhone.setText(user.get("mobile"));
        About.setText(user.get("about"));

    }
    // module for volley connetcion and its respone
    private void registerUser(final String email1,final String password1) {
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        StringRequest strReq = new StringRequest(Request.Method.POST,AppConfig.URL_Edit_Company, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Volley Respone Error", "Register Response: " + response.toString());
                loading.dismiss();
                Toast.makeText(getApplicationContext(), "User successfully Updated. Try login now!", Toast.LENGTH_LONG).show();
                        //Showing toast message of the response


                // Launch login activity
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
                String image = getStringImage(bitmap);
                Map<String, String> params = new HashMap<String, String>();
                params.put("Companyname", CompanyName.getText().toString());
                params.put("CompanyAddress",Address.getText().toString());
                params.put("emailAddress", Email.getText().toString());
                params.put("ContactPersonName",ContactPerson.getText().toString());
                params.put("Mobilenumber",ContactPersonPhone.getText().toString());
                params.put("password", Password.getText().toString());
                params.put("username",Username.getText().toString());
                params.put("about",About.getText().toString());
                params.put("image", image);
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

        db.deleteCompany();
        // Launching the login activity
        Intent intent = new Intent(getApplicationContext(), Welcome.class);
        startActivity(intent);
        finish();
    }
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                img.setImageBitmap(bitmap);
                imagePath= saveToInternalStorage(bitmap).toString();
                session.setImagePath(imagePath);
                //Toast.makeText(getApplicationContext(),imagePath,Toast.LENGTH_LONG).show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    //save the image to the local internal storage
    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,session.getEmail()+"_profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        //bmp.compress(Bitmap.CompressFormat.PNG, 90, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

   }
