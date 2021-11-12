package com.example.welcome.jobpost;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class SelectedCandidates extends AppCompatActivity {
    DrawerLayout drawer;
    ActionBarDrawerToggle mToggler;
    NavigationView navigationView;
    private  SQLiteHandler db;

    ImageView nav_img;
    CircleImageView imgVc;
    TextView nav_firstname;
    TextView nav_email;

    ListView selectedListView;
    ArrayList<HashMap<String,String>> candidateList;
    SessionManager session;
    private  int pos1;

    static  String FirstName,LastName,email,JobseekerEmail,mobile,url,Address,DOB,Nationality,FuncationalArea,Gender;
    static  String JobTitle_Selected,JobDescription_Selected,JobCategorySpinner_Selected,SeatsAvailable_Selected,ExperiencedRequried_Selected;
    static  String SalarSpinner_Selected,MinimuEducation_Selected,PartTime_Selected,Display_Selected;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_candidates);
        selectedListView=(ListView)findViewById(R.id.lv_selsectCandidte);

        //refernceing the arrar hash list
        candidateList=new ArrayList<>();

        //seesion maneger
        session=new SessionManager(getApplicationContext());
        viewCandidate();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer=(DrawerLayout)findViewById(R.id.drawer_layout_company_AppliedCandidate);
        mToggler=new ActionBarDrawerToggle(this,drawer,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(mToggler);
        mToggler.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_company_AppliedCandidate);
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


                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_company_AppliedCandidate);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        View header=navigationView.getHeaderView(0);
        nav_firstname=(TextView)header.findViewById(R.id.nav_username);
        nav_email=(TextView) header.findViewById(R.id.textView);
        nav_img=(ImageView)header.findViewById(R.id.imageView);
        imgVc=(CircleImageView) header.findViewById(R.id.imgjlc);
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

                        FirstName=c.getString("FirstName");
                         LastName=c.getString("LastName");
                         email=c.getString("CompanyEmail");
                         JobseekerEmail=c.getString("JobseekerEmail");
                         mobile=c.getString("mobile");
                         url = c.getString("url");
                         Address=c.getString("CurrentAddress");
                         DOB=c.getString("DOB");
                         Nationality=c.getString("Nationality");
                         FuncationalArea=c.getString("FunctionalArea");
                         Gender=c.getString("Gender");
                         JobTitle_Selected = c.getString("JobTitle_Selected");
                         JobDescription_Selected = c.getString("JobDescription_Selected");
                         SeatsAvailable_Selected = c.getString("SeatsAvailable_Selected");
                         ExperiencedRequried_Selected = c.getString("ExperiencedRequried_Selected");
                         JobCategorySpinner_Selected = c.getString("JobCategorySpinner_Selected");
                         SalarSpinner_Selected = c.getString("SalarSpinner_Selected");
                         MinimuEducation_Selected = c.getString("MinimuEducation_Selected");
                         PartTime_Selected = c.getString("PartTime_Selected");
                         //Display_Selected = c.getString("Display_Selected");
                            //String cv ="button";


                        if (session.getEmail().contentEquals(email)) {
                            HashMap<String, String> Candidatelists = new HashMap<>();
                            Candidatelists.put("FirstName", FirstName);
                            Candidatelists.put("LastName", LastName);
                            Candidatelists.put("mobile", mobile);
                            Candidatelists.put("email",email);
                            Candidatelists.put("JobseekerEmail",JobseekerEmail);
                            Candidatelists.put("url", url);
                            Candidatelists.put("Address",Address);
                            Candidatelists.put("DOB",DOB);
                            Candidatelists.put("Nationality",Nationality);
                            Candidatelists.put("FuncationalArea",FuncationalArea);
                            Candidatelists.put("Gender",Gender);

                            Candidatelists.put("JobTitle_Selected", JobTitle_Selected);
                            Candidatelists.put("JobDescription_Selected", JobDescription_Selected);
                            Candidatelists.put("SeatsAvailable_Selected", SeatsAvailable_Selected);
                            Candidatelists.put("ExperiencedRequried_Selected", ExperiencedRequried_Selected);
                            Candidatelists.put("JobCategorySpinner_Selected", JobCategorySpinner_Selected);
                            Candidatelists.put("SalarSpinner_Selected", SalarSpinner_Selected);
                            Candidatelists.put("MinimuEducation_Selected", MinimuEducation_Selected);
                            Candidatelists.put("PartTime_Selected", PartTime_Selected);
                            //Candidatelists.put("Display_Selected", Display_Selected);

                          //  Candidatelists.put("cv_download",cv);

                            //adding to  the array list
                            candidateList.add(Candidatelists);

                        }
                        }
                        ListAdapter adapter = new SimpleAdapter(
                                getApplicationContext(), candidateList,
                                R.layout.viewcadidate_listview,
                                new String[]{"FirstName", "LastName","mobile","JobseekerEmail","JobTitle_Selected",
                                        "JobDescription_Selected","SeatsAvailable_Selected","ExperiencedRequried_Selected","JobCategorySpinner_Selected","SalarSpinner_Selected",
                                        "MinimuEducation_Selected","PartTime_Selected"},
                                new int[]{R.id.candidate_FirstName, R.id.candidate_LastName,R.id.candidate_Contact,
                                        R.id.candidate_email,R.id.txt_CJJobTitle,R.id.txt_CJJobDescription,
                                        R.id.txt_CJSeatsAvailable,R.id.txt_CJExperienceReq,R.id.txt_CJspinnerJobC,
                                        R.id.txt_CJspinnerOsalary,R.id.txt_CJ0spinnerMinEdu,R.id.txt_CJJobType});

                        selectedListView.setAdapter(adapter);
                        selectedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                session.setJobseelerEmail(candidateList.get(position).get("JobseekerEmail"
                                ));
                                //Toast.makeText(getApplicationContext(),session.getJobseekerEmail(),Toast.LENGTH_LONG).show();
                                        pos1=position;
                                listClicked(position);

                            }
                        });



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

    public  void listClicked(int pos){
       //Toast.makeText(getApplicationContext(),"download",Toast.LENGTH_LONG).show();
        HashMap<String, String> Candidatelists = new HashMap<>();
        Bundle basket = new Bundle();
        basket.putString("FirstName", candidateList.get(pos).get(FirstName));
        Intent in=new Intent(getApplicationContext(),SelectedCandidateDetails.class);
        in.putExtras(basket);
        startActivity(in);

    }


    private void loadImageFromStorage(String path)
    {

        try {
            File f=new File(path, session.getEmail()+"_profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            //nav_img.setImageBitmap(b);
            imgVc.setImageBitmap(b);

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }


    }
