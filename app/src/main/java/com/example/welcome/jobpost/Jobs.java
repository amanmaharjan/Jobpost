package com.example.welcome.jobpost;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.renderscript.Long4;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.welcome.jobpost.AppConfig;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.welcome.jobpost.R.id.email;

public class Jobs extends AppCompatActivity {
    // public static String URL_LOGIN ="http://192.168.5.10/androidlogin/companyjob/companyjobs.php";
    DrawerLayout drawer;
    ActionBarDrawerToggle mToggler;
    NavigationView navigationView;
    private SQLiteHandler db;
    ArrayList<HashMap<String, String>> JObList;
    ListView lv_jobs;
    public Dialog dialogbox;
    private int pos1;
    private SessionManager session;

    ImageView nav_img;
    CircleImageView imgVc;
    TextView nav_firstname;
    TextView nav_email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);
        lv_jobs = (ListView) findViewById(R.id.listview_JJobs);
        JObList = new ArrayList<>();
        session=new SessionManager(this);
        checkLogin();

        ///for navigations
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer=(DrawerLayout)findViewById(R.id.drawer_layout_jobs);
        mToggler=new ActionBarDrawerToggle(this,drawer,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(mToggler);
        mToggler.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_jobs);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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


                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_jobs);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });


        //chinako navigation ko contain
        View header=navigationView.getHeaderView(0);
        nav_firstname=(TextView)header.findViewById(R.id.nav_username);
        nav_email=(TextView) header.findViewById(R.id.textView);
        nav_img=(ImageView)header.findViewById(R.id.imageView);
        imgVc=(CircleImageView)header.findViewById(R.id.imgjlc);
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

    public void checkLogin() {
        // Tag used to cancel the request
        final StringRequest strReq = new StringRequest(Request.Method.GET, AppConfig.URL_Jobseeker_Jobs, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("VOLLEY RESPONNSE", "Login Response: " + response.toString());

                try {
                    JSONObject jsonObj = new JSONObject(response);

                    // Getting JSON Array node
                    JSONArray Data = jsonObj.getJSONArray("Data");

                    // looping through All Contacts
                    for (int i = 0; i < Data.length(); i++) {
                        JSONObject c = Data.getJSONObject(i);

                        String id = c.getString("id");
                        String CompanyEmail=c.getString("CompanyEmail");
                        String CompanyName=c.getString("CompanyName");
                        String JobTitle = c.getString("JobTitle");
                        String JobDescription = c.getString("JobDescription");
                        String SeatsAvailable = c.getString("SeatsAvailable");
                        String ExperiencedRequried = c.getString("ExperiencedRequried");
                        String JobCategorySpinner = c.getString("JobCategorySpinner");
                        String SalarSpinner = c.getString("SalarSpinner");
                        String MinimuEducation = c.getString("MinimuEducation");
                        String PartTime = c.getString("PartTime");
                        String PostedOn=c.getString("created_at");
                       // String Display = c.getString("Display");
                        Log.d("DATA AAYO",CompanyEmail);

                        HashMap<String, String> joblists = new HashMap<>();
                        joblists.put("id", id);
                        joblists.put("CompanyEmail", CompanyEmail);
                        joblists.put("CompanyName", CompanyName);
                        joblists.put("JobTitle", JobTitle);
                        joblists.put("JobDescription", JobDescription);
                        joblists.put("SeatsAvailable", SeatsAvailable);
                        joblists.put("ExperiencedRequried", ExperiencedRequried);
                        joblists.put("JobCategorySpinner", JobCategorySpinner);
                        joblists.put("SalarSpinner", SalarSpinner);
                        joblists.put("MinimuEducation", MinimuEducation);
                        joblists.put("PartTime", PartTime);
                        joblists.put("PostedOn", PostedOn);
                        //joblists.put("Display", Display);
                        //adding to  the array list
                        JObList.add(joblists);


                        ListAdapter adapter = new SimpleAdapter(
                                getApplicationContext(), JObList,
                                R.layout.listview_jobs, new String[]{"CompanyName","JobTitle", "JobDescription", "SeatsAvailable",
                                "ExperiencedRequried", "JobCategorySpinner", "SalarSpinner", "MinimuEducation", "PartTime","PostedOn"}, new int[]{R.id.txt_CJCompanyName,R.id.txt_CJJobTitle,
                                R.id.txt_CJJobDescription, R.id.txt_CJSeatsAvailable, R.id.txt_CJExperienceReq, R.id.txt_CJspinnerJobC, R.id.txt_CJspinnerOsalary,
                                R.id.txt_CJ0spinnerMinEdu, R.id.txt_CJJobType,R.id.txt_CJpostDate});

                        lv_jobs.setAdapter(adapter);
                        lv_jobs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                pos1 = position;
                                listClicked(position);
                                /*String id_Selected =(JObList.get(0).get("JobTitle"));
                                Log.d("abc",id_Selected);*/
                                itemIntoSelectedCandidate();


                            }
                        });

                        Log.d(JobDescription, "ram" + JobTitle);
                    }
                } catch (final JSONException e) {
                    Log.e("jhdhhdhjh", "Json parsing error: " + e.getMessage());
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

    public void listClicked(int pos) {
        dialogbox = new Dialog(Jobs.this);
        dialogbox.setContentView(R.layout.dilogbox_job_apply);
        final Button yes = (Button) dialogbox.findViewById(R.id.btn_yes);
        final Button no = (Button) dialogbox.findViewById(R.id.btn_no);


        dialogbox.show();
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Thank you for Apply!!!", Toast.LENGTH_LONG).show();
                dialogbox.dismiss();
                registerUser1();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogbox.dismiss();
            }
        });

    }

    public void itemIntoSelectedCandidate() {
        for (int i = 0; i <= JObList.size(); i++) {
            if (pos1 == i) {
                String id_Selected = (JObList.get(i).get("id"));
                String CompanyName = (JObList.get(i).get("CompanyName"));
                String CompanyEmail = (JObList.get(i).get("CompanyEmail"));
                String JobTitle_Selected = (JObList.get(i).get("JobTitle"));
                String JobDescription_Selected = (JObList.get(i).get("JobDescription"));
                String SeatsAvailable_Selected = (JObList.get(i).get("SeatsAvailable").toString());
                String ExperiencedRequried_Selected = (JObList.get(i).get("ExperiencedRequried"));
                String JobCategorySpinner_Selected = (JObList.get(i).get("JobCategorySpinner"));
                String SalarSpinner_Selected = (JObList.get(i).get("SalarSpinner"));
                String MinimuEducation_Selected = (JObList.get(i).get("MinimuEducation"));
                String PartTime_Selected = (JObList.get(i).get("PartTime"));
                //String Display_Selected = (JObList.get(i).get("Display"));
                Log.d("id_s", id_Selected);
                Log.d("CompanyEmail", CompanyEmail);
                Log.d("abcd", JobTitle_Selected);
                Log.d("abcd", JobDescription_Selected);
                Log.d("abcd", SeatsAvailable_Selected);
                Log.d("abcd", ExperiencedRequried_Selected);
                Log.d("abcd", JobCategorySpinner_Selected);
                Log.d("abcd", SalarSpinner_Selected);
                Log.d("abcd", MinimuEducation_Selected);
                Log.d("abcd", PartTime_Selected);
                //Log.d("abcd", Display_Selected);

            }
        }


    }

    private void registerUser1() {
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_Company_SelectedCandidate, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Volley Respone Error", "Register Response: " + response.toString());
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    String message=jsonObj.getString("error_msg");
                    Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Launch login activity
               // Toast.makeText(getApplicationContext(),"monkey",Toast.LENGTH_LONG).show();

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
                Log.d("Sekker Error",session.getEmail());


                Map<String, String> params = new HashMap<String, String>();
                for (int i = 0; i <= JObList.size(); i++) {
                    if (pos1 == i) {
                        // params.put("id_Selected", (JObList.get(i).get("id")));
                        params.put("JobseekerEmail", session.getEmail());
                        params.put("CompanyEmail",JObList.get(i).get("CompanyEmail"));
                        params.put("CompanyName",JObList.get(i).get("CompanyName"));
                        params.put("JobTitle_Selected", (JObList.get(i).get("JobTitle")));
                        params.put("JobDescription_Selected", (JObList.get(i).get("JobDescription")));
                        params.put("SeatsAvailable_Selected", JObList.get(i).get("SeatsAvailable").toString());
                        params.put("ExperiencedRequried_Selected", (JObList.get(i).get("ExperiencedRequried")));
                        params.put("JobCategorySpinner_Selected", (JObList.get(i).get("JobCategorySpinner")));
                        params.put("SalarSpinner_Selected", (JObList.get(i).get("SalarSpinner")));
                        params.put("MinimuEducation_Selected", (JObList.get(i).get("MinimuEducation")));
                        params.put("PartTime_Selected", (JObList.get(i).get("PartTime")));
                        //params.put("Display_Selected", (JObList.get(i).get("Display")));



                    }
                }
                return params;
            }

        };

        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }
    private void logoutUser() {
        //session.setLogin(false,"","","");
        session.setLogout();
        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(getApplicationContext(), Welcome.class);
        startActivity(intent);
        finish();
    }
    private void loadImageFromStorage(String path)
    {

        try {
            File f=new File(path, session.getEmail()+"_profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            //imageView.setImageBitmap(b);
            //nav_img.setImageBitmap(b);
            imgVc.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }

}

