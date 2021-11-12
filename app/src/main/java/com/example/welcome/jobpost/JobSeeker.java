
package com.example.welcome.jobpost;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class
JobSeeker extends AppCompatActivity  {
    //yasko layoutvaneko jobseeker_profilepage
    TextView jpfirstname, jplastname, jpdob, jpgender, jpnationality, jpphoneNum, jpaddress, jpfunctionalArea,
            jpeducationalDetail, jpexpectedSalary, jppreviousExp, jpskillLable, jpsummary;
   // private SQLiteHandler db;
    DrawerLayout drawer;
    ActionBarDrawerToggle mToggler;

    //NavigationView navigationView;
    Button edit;

    ImageView nav_img;
    TextView nav_firstname;
    CircleImageView imgVc;
    TextView nav_email;

    SQLiteHandler dbhelper;
    SessionManager session;
    private ListView list_experience,list_education;

   ArrayList<HashMap<String, String>> ExperienceList;
    ArrayList<HashMap<String, String>> EducationList;

    //for image upload
    private Button buttonChoose, buttonUpload;
    private ImageView imageView;

    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;
    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";
    File directory;
    String imagePath="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jobseeker_profilepage);

        //for navigation bar

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer=(DrawerLayout)findViewById(R.id.drawer_layout_profile);
         setSupportActionBar(toolbar);

        mToggler=new ActionBarDrawerToggle(this,drawer,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(mToggler);
        mToggler.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_profile);
                     navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( MenuItem item) {
                // Handle navigation view item clicks here.
                int id = item.getItemId();

                if (id == R.id.nav_dashboard) {
                    // Handle the camera action
                    Intent in=new Intent(getApplicationContext(),JobLogedInPage.class);
                    startActivity(in);
                }
                if (id == R.id.nav_profile) {
                    // Handle the camera action
                    Intent in=new Intent(getApplicationContext(),JobSeeker.class);
                    startActivity(in);
                }else if (id == R.id.nav_education) {
                    Intent in=new Intent(getApplicationContext(),EducationView.class);
                    startActivity(in);

                } else if (id == R.id.nav_experince) {
                    Intent in=new Intent(getApplicationContext(),ExperienceView.class);
                    startActivity(in);

                }
                else if (id == R.id.nav_skills) {
                    Intent in=new Intent(getApplicationContext(),Skills.class);
                    startActivity(in);

                }else if (id == R.id.nav_cv) {
                    Intent in=new Intent(getApplicationContext(),CV.class);
                    startActivity(in);

                } else if (id == R.id.nav_jobs) {
                    Intent in=new Intent(getApplicationContext(),Jobs.class);
                    startActivity(in);

                } else if (id == R.id.nav_logout) {
                   logoutUser();


                }


                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_profile);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        View header=navigationView.getHeaderView(0);
        nav_firstname=(TextView)header.findViewById(R.id.nav_username);
        nav_email=(TextView) header.findViewById(R.id.textView);
        nav_img=(ImageView)header.findViewById(R.id.imageView);
        imgVc=(CircleImageView)header.findViewById(R.id.imgjlc);




        jpfirstname = (TextView) findViewById(R.id.txt_jpFirstName);
        jplastname = (TextView) findViewById(R.id.txt_jplastName);
        jpdob = (TextView) findViewById(R.id.txt_jpDOB);
        jpgender = (TextView) findViewById(R.id.txt_jpGender);
        jpnationality = (TextView) findViewById(R.id.txt_jpNationnality);
        jpphoneNum = (TextView) findViewById(R.id.txt_jpPhonN);
        jpaddress = (TextView) findViewById(R.id.txt_jpAdress);
        jpfunctionalArea = (TextView) findViewById(R.id.txt_jpFuctionalA);
        jpexpectedSalary = (TextView) findViewById(R.id.txt_jpExpectedS);

        edit=(Button)findViewById(R.id.btn_jpEditP);
       // list_education=(ListView)findViewById(R.id.list_education);
        list_experience=(ListView)findViewById(R.id.list_experience);
        session=new SessionManager(getApplicationContext());

       // EducationList=new ArrayList<>();
       // ExperienceList=new ArrayList<>();

        //for upload image
        buttonChoose=(Button)findViewById(R.id.btn_chooseImage);
        buttonUpload=(Button)findViewById(R.id.btn_UploadImage);
        imageView = (ImageView) findViewById(R.id.imgJp);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
                buttonUpload.setVisibility(View.VISIBLE);


            }
        });
        buttonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
                buttonUpload.setVisibility(View.VISIBLE);
            }
        });
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(getApplicationContext(),EditProfile.class);
                startActivity(in);
            }
        });

// SqLite database handler
        dbhelper = new SQLiteHandler(getApplicationContext());
        // Displaying the user details on the screen
        HashMap<String, String> user = dbhelper.getUserDetails();
        jpfirstname.setText(user.get("FirstName"));

        jplastname.setText(user.get("LastName"));
        jpphoneNum.setText(user.get("mobile"));
        jpgender.setText(user.get("Gender"));
        jpnationality.setText(user.get("Nationality"));
        jpaddress.setText(user.get("CurrentAddress"));
        jpfunctionalArea.setText(user.get("FunctionalArea"));
        jpexpectedSalary.setText(user.get("Salary"));
        jpdob.setText(user.get("DOB"));

      //nav_firstname.setText(user.get("FirstName")+" "+user.get("LastName"));
      nav_firstname.setText(session.getFirstName()+" "+session.getLastName());
        nav_email.setText(session.getEmail());

        loadImageFromStorage(session.getImagePath());


        //loadImageFromStorage();
      // education1();
        //experience1();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (mToggler.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        //bmp.compress(Bitmap.CompressFormat.PNG, 90, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage() {
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.UPLOAD_IMAGE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(getApplicationContext(),
                                "No internet Connection ...Please connect to network", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap);
                //Getting Image Name
               // String name = editTextName.getText().toString().trim();
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();
                //Adding parameters
                params.put(KEY_IMAGE, image);
                params.put("email", session.getEmail().toString());
               params.put(KEY_NAME, session.getUserName().toString());

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
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
                imageView.setImageBitmap(bitmap);
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
    private void loadImageFromStorage(String path)
    {

        try {
            File f=new File(path, session.getEmail()+"_profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            imageView.setImageBitmap(b);
           // nav_img.setImageBitmap(b);
            imgVc.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }


    public void education1() {

        // Tag used to cancel the request
        final StringRequest strReq = new StringRequest(Request.Method.GET,AppConfig.URL_Jobseeker_Education, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("VOLLEY RESPONNSE", "Login Response: " + response.toString());

                try {
                    JSONObject jsonObj = new JSONObject(response);

                    // Getting JSON Array node
                    JSONArray Data = jsonObj.getJSONArray("Data");

                    // looping through All Contacts
                    for (int i = 0; i <Data.length(); i++) {
                        JSONObject c = Data.getJSONObject(i);

                        String Id = c.getString("Id");
                        String email=c.getString("email");
                        String InstituteName = c.getString("InstituteName");
                        String PassedYear = c.getString("PassedYear");
                        String Grade = c.getString("Grade");
                        String EducationlQualification = c.getString("EducationlQualification");

                        if (session.getEmail().contentEquals(email)){

                            HashMap<String, String> Educationlists = new HashMap<>();
                            Educationlists.put("Id", Id);
                            Educationlists.put("InstituteName", InstituteName);
                            Educationlists.put("PassedYear", PassedYear);
                            Educationlists.put("Grade", Grade);
                            Educationlists.put("EducationlQualification", EducationlQualification);

                            //adding to  the array list
                            EducationList.add(Educationlists);}
                        ListAdapter adapter = new SimpleAdapter(
                                getApplicationContext(), EducationList,
                                R.layout.edu_view_listview, new String[]{"InstituteName", "PassedYear","Grade","EducationlQualification"}, new int[]{R.id.txt_JEdInstituteName,
                                R.id.txt_JEdPassedYear, R.id.txt_JEdGrade,R.id.txt_JEdEducationlQualification});

                        list_education.setAdapter(adapter);




                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY ERROR", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "No internet Connection ...Please connect to network", Toast.LENGTH_LONG).show();

            }
        });

        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);

    }

    public void experience1() {
        // Tag used to cancel the request
        final StringRequest strReq = new StringRequest(Request.Method.GET,AppConfig.URL_Jobseeker_Experience, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("VOLLEY RESPONNSE", "Login Response: " + response.toString());

                try {
                    JSONObject jsonObj = new JSONObject(response);

                    // Getting JSON Array node
                    JSONArray Data = jsonObj.getJSONArray("Data");

                    // looping through All Contacts
                    for (int i = 0; i <Data.length(); i++) {
                        JSONObject c = Data.getJSONObject(i);

                        String id = c.getString("id");
                        String email=c.getString("email");
                        final String CompanyName = c.getString("CompanyName");
                        String JobTitle = c.getString("JobTitle");
                        String DurationOfWork = c.getString("DurationOfWork");
                        String FuctionArea = c.getString("FuctionArea");

                        if (session.getEmail().contentEquals(email)){


                            HashMap<String, String> Experiencelists = new HashMap<>();
                            Experiencelists.put("id", id);
                            Experiencelists.put("CompanyName", CompanyName);
                            Experiencelists.put("JobTitle", JobTitle);
                            Experiencelists.put("DurationOfWork", DurationOfWork);
                            Experiencelists.put("FuctionArea", FuctionArea);

                            //adding to  the array list
                            ExperienceList.add(Experiencelists);
                        }




                        ListAdapter adapter = new SimpleAdapter(
                                getApplicationContext(), ExperienceList,
                                R.layout.exp_view_listview, new String[]{"CompanyName", "JobTitle","DurationOfWork",
                                "FuctionArea"}, new int[]{R.id.txt_JExCompanyName,
                                R.id.txt_JExJobTitle, R.id.txt_JExDurationOfWork,R.id.txt_JExFunctionArea});

                       list_experience.setAdapter(adapter);

                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY ERROR", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "No internet Connection ...Please connect to network", Toast.LENGTH_LONG).show();

            }
        });

        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }

    private void logoutUser() {
        session.setLogout();
        dbhelper.deleteUsers();
        // Launching the login activity
        Intent intent = new Intent(getApplicationContext(), Welcome.class);
        startActivity(intent);
        finish();
    }


}
