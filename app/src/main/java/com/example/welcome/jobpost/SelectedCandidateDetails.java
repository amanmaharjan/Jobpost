package com.example.welcome.jobpost;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
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

public class SelectedCandidateDetails extends AppCompatActivity {
    //yasko layoutvaneko jobseeker_profilepage
    TextView jpfirstname, jplastname, jpdob, jpgender, jpnationality, jpphoneNum, jpaddress, jpfunctionalArea,
             jpexpectedSalary;
    Button downlaod;
    SessionManager session;
    DrawerLayout drawer;
    ActionBarDrawerToggle mToggler;
    NavigationView navigationView;
    private  SQLiteHandler db;

    ImageView nav_img;
    TextView nav_firstname;
    TextView nav_email;
    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;
    private ListView list_experience,list_education;

    ArrayList<HashMap<String,String>> candidateList;
    ArrayList<HashMap<String, String>> ExperienceList;
    ArrayList<HashMap<String, String>> EducationList;

    //for image upload
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
        setContentView(R.layout.activity_selected_candidate_details);
        jpfirstname = (TextView) findViewById(R.id.txt_jpFirstName);
        jplastname = (TextView) findViewById(R.id.txt_jplastName);
        jpdob = (TextView) findViewById(R.id.txt_jpDOB);
        jpgender = (TextView) findViewById(R.id.txt_jpGender);
        jpnationality = (TextView) findViewById(R.id.txt_jpNationnality);
        jpphoneNum = (TextView) findViewById(R.id.txt_jpPhonN);
        jpaddress = (TextView) findViewById(R.id.txt_jpAdress);
        jpfunctionalArea = (TextView) findViewById(R.id.txt_jpFuctionalA);
        jpexpectedSalary = (TextView) findViewById(R.id.txt_jpExpectedS);

        downlaod=(Button)findViewById(R.id.btn_jpEditP);
        list_education=(ListView)findViewById(R.id.list_education);
        list_experience=(ListView)findViewById(R.id.list_experience);
        session=new SessionManager(getApplicationContext());
        candidateList=new ArrayList<>();
        EducationList=new ArrayList<>();
        ExperienceList=new ArrayList<>();

        imageView = (ImageView) findViewById(R.id.imgJp);
        viewCandidate();
        education();
        experience();
        requestStoragePermission();
        downlaod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPdfs();

            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer=(DrawerLayout)findViewById(R.id.drawer_layout_company_AppliedCandidateDetails);
        mToggler=new ActionBarDrawerToggle(this,drawer,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(mToggler);
        mToggler.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_company_AppliedCandidateDetails);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle navigation view item clicks here.
                int id = item.getItemId();

                if (id == R.id.nav_dashboard_company) {
                    // Handle the camera action
                    Intent in=new Intent(getApplicationContext(),CompanyLogedInPage.class);
                    startActivity(in);
                }
                else  if (id == R.id.nav_profile_company) {
                    // Handle the camera action
                    Intent in=new Intent(getApplicationContext(),CompanyProfilePage.class);
                    startActivity(in);
                }else if (id == R.id.nav_jobs_company) {
                    Intent in=new Intent(getApplicationContext(),CompanyJobs.class);
                    startActivity(in);

                } else if (id == R.id.nav_logout_company) {
                    //  logoutUser();

                } else if (id == R.id.nav_education_Applied_Candidate) {
                    Intent in=new Intent(getApplicationContext(),SelectedCandidates.class);
                    startActivity(in);

                }


                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_company_AppliedCandidateDetails);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        View header=navigationView.getHeaderView(0);
        nav_firstname=(TextView)header.findViewById(R.id.nav_username);
        nav_email=(TextView) header.findViewById(R.id.textView);
        nav_img=(ImageView)header.findViewById(R.id.imageView);

        nav_firstname.setText(session.getFirstName()+" "+session.getLastName());
        nav_email.setText(session.getEmail());
        loadImageFromStorage(session.getImagePath());

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







    public void viewCandidate() {

        // Tag used to cancel the request
        final StringRequest strReq = new StringRequest(Request.Method.GET,AppConfig.URL_Company_AppliedCandidate, new Response.Listener<String>() {

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

                        String FirstName=c.getString("FirstName");
                        String LastName=c.getString("LastName");
                        String email=c.getString("CompanyEmail");
                        String JobseekerEmail=c.getString("JobseekerEmail");
                        String mobile=c.getString("mobile");
                        String url = c.getString("url");
                        String Address=c.getString("CurrentAddress");
                        String DOB=c.getString("DOB");
                        String Nationality=c.getString("Nationality");
                        String FuncationalArea=c.getString("FunctionalArea");
                        String Gender=c.getString("Gender");
                        String Salary=c.getString("Salary");


                        if (session.getEmail().contentEquals(email)) {
                            if(session.getJobseekerEmail().contentEquals(JobseekerEmail))
                            {
                                jpfirstname.setText(FirstName);
                                jplastname.setText(LastName);
                                jpphoneNum.setText(mobile);
                                jpgender.setText(Gender);
                                jpnationality.setText(Nationality);
                                jpaddress.setText(Address);
                                jpfunctionalArea.setText(FuncationalArea);
                                jpexpectedSalary.setText(Salary);
                                jpdob.setText(DOB);



                            }
                            }
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
    public void education() {

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

                        if (session.getJobseekerEmail().contentEquals(email)){

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
    public void experience() {
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

                        if (session.getJobseekerEmail().contentEquals(email)){


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

    private void getPdfs() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.PDF_FETCH_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            // Toast.makeText(getApplicationContext(),obj.getString("message"), Toast.LENGTH_SHORT).show();

                            String pdfUrl = obj.getString("url");
                            // Toast.makeText(getApplicationContext(),pdfUrl,Toast.LENGTH_SHORT).show();
                            MyTask myTask = new MyTask();
                            myTask.execute(pdfUrl);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Please Check ur Network Connection",Toast.LENGTH_SHORT).show();

                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email",session.getJobseekerEmail());
                return params;
            }

        };

        RequestQueue request = Volley.newRequestQueue(this);
        request.add(stringRequest);

    }
    class MyTask extends AsyncTask<String, Integer, Boolean> {
        private int counter = 0;
        @Override
        protected void onPreExecute() {
            // downloadImagesProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean successful = false;
            URL downloadURL = null;
            HttpURLConnection connection = null;
            InputStream inputStream = null;
            FileOutputStream fileOutputStream = null;
            File file = null;
            try {
                downloadURL = new URL(params[0]);
                connection = (HttpURLConnection) downloadURL.openConnection();
                inputStream = connection.getInputStream();
                file = new File(Environment.getExternalStoragePublicDirectory
                        (Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()
                        + "/" + Uri.parse(params[0]).getLastPathSegment());
                fileOutputStream = new FileOutputStream(file);


                int read;
                byte[] buffer = new byte[1024];
                while (( read= inputStream.read(buffer)) != -1) {

                    fileOutputStream.write(buffer, 0, read);
                    counter = counter +read;
                    publishProgress(counter);


                }
                successful=true;

            } catch (MalformedURLException e) {
                // L.m(e+"");
            }
            catch (IOException e) {
                //L.m(e + "");
            } finally {

                if (connection != null) {
                    connection.disconnect();
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                if(fileOutputStream!=null){
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return successful;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //calculatedProgress= (int)(( (double) values[0]/contentLength)*100);
            // downloadImagesProgress.setProgress(calculatedProgress);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {

            //downloadImagesProgress.setVisibility(View.GONE);
            if(aBoolean) {

                Toast.makeText(getApplicationContext(), "Download Successful", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Connect To Internet", Toast.LENGTH_LONG).show();

            }        }
    }
    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void loadImageFromStorage(String path)
    {

        try {
            File f=new File(path, session.getEmail()+"_profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            nav_img.setImageBitmap(b);

            File f1=new File(path, session.getJobseekerEmail()+"_profile.jpg");
            Bitmap b1 = BitmapFactory.decodeStream(new FileInputStream(f));
            imageView.setImageBitmap(b1);

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }


}



