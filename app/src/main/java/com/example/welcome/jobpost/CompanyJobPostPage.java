package com.example.welcome.jobpost;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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

public class CompanyJobPostPage extends AppCompatActivity implements View.OnClickListener {
    DrawerLayout drawer;
    ActionBarDrawerToggle mToggler;
    NavigationView navigationView;
    private  SQLiteHandler db;
    public Dialog dialogbox;
    int pos1;



    Button addPost, viewPost;
    ListView lv_jobs;
    ArrayList<HashMap<String, String>> JObList;
   private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_job_post_page);
        addPost = (Button) findViewById(R.id.btn_add);
        viewPost = (Button) findViewById(R.id.btn_View);
        lv_jobs = (ListView) findViewById(R.id.lv_View);
        addPost.setOnClickListener(this);
        viewPost.setOnClickListener(this);
        JObList=new ArrayList<>();
        session=new SessionManager(getApplicationContext());


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer=(DrawerLayout)findViewById(R.id.drawer_layout_company_jobpost);
        mToggler=new ActionBarDrawerToggle(this,drawer,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(mToggler);
        mToggler.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_company_jobpost);
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
                    Intent in=new Intent(getApplicationContext(),CompanyJobPostPage.class);
                    startActivity(in);

                } else if (id == R.id.nav_logout_company) {
                  //  logoutUser();

                } else if (id == R.id.nav_education_Applied_Candidate) {
                    Intent in=new Intent(getApplicationContext(),SelectedCandidates.class);
                    startActivity(in);

                }


                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_company_jobpost);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });



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
        final StringRequest strReq = new StringRequest(Request.Method.GET,AppConfig.URL_Jobseeker_Jobs, new Response.Listener<String>() {

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
                        String email=c.getString("CompanyEmail");
                        String CompanyName=c.getString("CompanyName");
                        String JobTitle = c.getString("JobTitle");
                        String JobDescription = c.getString("JobDescription");
                        String SeatsAvailable = c.getString("SeatsAvailable");
                        String ExperiencedRequried = c.getString("ExperiencedRequried");
                        String JobCategorySpinner = c.getString("JobCategorySpinner");
                        String SalarSpinner = c.getString("SalarSpinner");
                        String MinimuEducation = c.getString("MinimuEducation");
                        String PartTime = c.getString("PartTime");
                        String PostOn = c.getString("created_at");
                        //String Display = c.getString("Display");

                        if (session.getEmail().contentEquals(email)) {

                            HashMap<String, String> joblists = new HashMap<>();
                            joblists.put("CompanyName", CompanyName);
                            joblists.put("JobTitle", JobTitle);
                            joblists.put("JobDescription", JobDescription);
                            joblists.put("SeatsAvailable", SeatsAvailable);
                            joblists.put("ExperiencedRequried", ExperiencedRequried);
                            joblists.put("JobCategorySpinner", JobCategorySpinner);
                            joblists.put("SalarSpinner", SalarSpinner);
                            joblists.put("MinimuEducation", MinimuEducation);
                            joblists.put("PartTime", PartTime);
                            joblists.put("PostOn", PostOn);
                           // joblists.put("Display", Display);
                            //adding to  the array list
                            JObList.add(joblists);
                        }




                        ListAdapter adapter = new SimpleAdapter(
                                getApplicationContext(), JObList,
                                R.layout.listview_jobs, new String[]{"CompanyName","JobTitle", "JobDescription","SeatsAvailable",
                                "ExperiencedRequried","JobCategorySpinner","SalarSpinner","MinimuEducation","PartTime","PostOn"}, new int[]{R.id.txt_CJCompanyName,R.id.txt_CJJobTitle,
                                R.id.txt_CJJobDescription, R.id.txt_CJSeatsAvailable,R.id.txt_CJExperienceReq,R.id.txt_CJspinnerJobC,R.id.txt_CJspinnerOsalary,
                                R.id.txt_CJ0spinnerMinEdu,R.id.txt_CJJobType,R.id.txt_CJpostDate});

                        lv_jobs.setAdapter(adapter);

                       /* lv_jobs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                listClicked(position);
                            }
                        });
                        */
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
    public void listClicked(int pos) {
        dialogbox = new Dialog(CompanyJobPostPage.this);
        dialogbox.setContentView(R.layout.dialogbox_delete_jobpost);
        final Button yes = (Button) dialogbox.findViewById(R.id.btn_yes_delete);
        final Button no = (Button) dialogbox.findViewById(R.id.btn_no_delete);


        dialogbox.show();
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Thank you for Apply!!!", Toast.LENGTH_LONG).show();
                dialogbox.dismiss();
               // registerUser1();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogbox.dismiss();
            }
        });

    }

    @Override
    public void onClick(View v) {
        if(v==addPost)
        {
            Intent in=new Intent(getApplicationContext(),CompanyJobs.class);
            startActivity(in);
        }

        else if(v==viewPost)
        {
            JObList.clear();
            checkLogin();

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
                params.put("email",session.getEmail());
                return params;
            }

        };

        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }
   }
