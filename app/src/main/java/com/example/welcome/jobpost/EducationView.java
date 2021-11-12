package com.example.welcome.jobpost;

import android.app.Dialog;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class EducationView extends AppCompatActivity implements View.OnClickListener{
    DrawerLayout drawer;
    ActionBarDrawerToggle mToggler;
    NavigationView navigationView;
    SQLiteHandler db;
    TextView line;

    ImageView nav_img;
    TextView nav_firstname;
    CircleImageView imgVc;
    TextView nav_email;

    private Button addPost, viewPost;
    private ListView lv_Education;
    ArrayList<HashMap<String, String>> EducationList;
    private  SessionManager session;
    private  String  InstituteName,PassedYear,Grade,EducationlQualification;
    private Dialog dialogbox;
    private  int pos1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_view);
        addPost = (Button) findViewById(R.id.btn_add_Education);
        viewPost = (Button) findViewById(R.id.btn_View_Education);
        lv_Education = (ListView) findViewById(R.id.lv_View_Education);
        line=(TextView)findViewById(R.id.line);
        addPost.setOnClickListener(this);
        viewPost.setOnClickListener(this);
        EducationList=new ArrayList<>();
        session =new SessionManager(getApplicationContext());
        ///for navigations
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer=(DrawerLayout)findViewById(R.id.drawer_layout_education);
        mToggler=new ActionBarDrawerToggle(this,drawer,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(mToggler);
        mToggler.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_education);
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
              else  if (id == R.id.nav_profile) {
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


                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_education);
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

                        lv_Education.setAdapter(adapter);
                        lv_Education.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                listClicked(position);
                                pos1=position;

                            }
                        });



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
    public void listClicked(int pos){
        dialogbox = new Dialog(EducationView.this);
        dialogbox.setContentView(R.layout.dialogbox_education_update);
        final EditText InstituteNameD = (EditText) dialogbox.findViewById(R.id.Jedu_Update_IName);
        final EditText PassedYearD = (EditText) dialogbox.findViewById(R.id.Jedu_Update_PassedY);
        final EditText GradeD = (EditText) dialogbox.findViewById(R.id.Jedu_Update_Grade);
        final Spinner EduQualificationD = (Spinner) dialogbox
                .findViewById(R.id.J_Update_spinner);         //  edamt kata kat use vacha  */
        Button JExDUpdate = (Button) dialogbox.findViewById(R.id.JEduDUpdate);
        Button JExDDelete = (Button) dialogbox
                .findViewById(R.id.JEduDDelete);
        Button close = (Button) dialogbox
                .findViewById(R.id.JEduDClose);

        //for dialog box close
        JExDDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();


            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogbox.dismiss();
                EducationList.clear();
                checkLogin();


            }
        });

        JExDUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InstituteName = InstituteNameD.getText().toString();
                PassedYear=PassedYearD.getText().toString();
                Grade=GradeD.getText().toString();
                EducationlQualification=EduQualificationD.getSelectedItem().toString();
                update();
                Toast.makeText(getApplicationContext(),"update sucessfully, Please Refresh",Toast.LENGTH_SHORT).show();
                Log.d("my",InstituteNameD.getText().toString());
            }


        });
        for (int i=0;i<=EducationList.size();i++){
            if(pos==i){
                InstituteNameD.setText(EducationList.get(i).get("InstituteName"));
                PassedYearD.setText(EducationList.get(i).get("PassedYear"));
                GradeD.setText(EducationList.get(i).get("Grade"));
            }
        }

        dialogbox.show();


    }


    public void update() {
        // Tag used to cancel the request
        final StringRequest strReq = new StringRequest(Request.Method.POST,AppConfig.URL_Jobseeker_Education_Update, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("VOLLEY RESPONNSE", "Login Response: " + response.toString());


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY ERROR", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "No internet Connection ...Please connect to network", Toast.LENGTH_LONG).show();

            }
        }){

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                for (int i=0;i<=EducationList.size();i++) {
                    if(pos1==i){ params.put("id", EducationList.get(i).get("Id"));}
                }
                params.put("InstituteName",InstituteName);
                params.put("PassedYear",PassedYear);
                params.put("Grade",Grade);
                params.put("EducationlQualification",EducationlQualification);
                Log.d("Education VIEW",InstituteName);
                return params;
            }
        };

        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }
    public void delete() {
        // Tag used to cancel the request
        final StringRequest strReq = new StringRequest(Request.Method.POST,AppConfig.URL_Jobseeker_Education_Delete, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("VOLLEY RESPONNSE", "Login Response: " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    Toast.makeText(getApplicationContext(),obj.getString("message"), Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY ERROR", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "No internet Connection ...Please connect to network", Toast.LENGTH_LONG).show();

            }
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                for (int i=0;i<=EducationList.size();i++) {
                    if(i==pos1){ params.put("id", EducationList.get(i).get("Id"));}
                }

                return params;
            }
        };

        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }
    @Override
    public void onClick(View v) {
        if(v==addPost)
        {
            Intent in=new Intent(getApplicationContext(),Education.class);
            startActivity(in);
        }

        else if(v==viewPost)
        {
            checkLogin();
            EducationList.clear();
            line.setVisibility(View.VISIBLE);

        }

    }
    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        JobLogedInPage logout=new JobLogedInPage();
        logout.logoutUser();

//        //session.setLogin(false,"","","");
//        session.setLogout();
//        db.deleteUsers();
//
//        // Launching the login activity
//        Intent intent = new Intent(getApplicationContext(), Welcome.class);
//        startActivity(intent);
//        finish();
    }

    private void loadImageFromStorage(String path)
    {

        try {
            File f=new File(path, session.getEmail()+"_profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            //imageView.setImageBitmap(b);
            nav_img.setImageBitmap(b);
            imgVc.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }
}
