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

public class ExperienceView extends AppCompatActivity implements View.OnClickListener {
    DrawerLayout drawer;
    ActionBarDrawerToggle mToggler;
    NavigationView navigationView;
    private SQLiteHandler db;
    TextView line;

    ImageView nav_img;
    TextView nav_firstname;
    CircleImageView imgVc;
    TextView nav_email;

    private Button addPost, viewPost;
    private ListView lv_experience;
    ArrayList<HashMap<String, String>> ExperienceList;
    private  SessionManager session;
    String  COMPANYNAME,JOBTitle,DOW,FuncationalAreas;
    private Dialog dialogbox;
    private  int pos1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experience_view);
        addPost = (Button) findViewById(R.id.btn_add_Experience);
        viewPost = (Button) findViewById(R.id.btn_View_Experience);
        lv_experience = (ListView) findViewById(R.id.lv_View_Experience);
        line=(TextView)findViewById(R.id.line1);
        addPost.setOnClickListener(this);
        viewPost.setOnClickListener(this);
        ExperienceList=new ArrayList<>();
        session =new SessionManager(getApplicationContext());

        ///for navigations
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer=(DrawerLayout)findViewById(R.id.drawer_layout_experince);
        mToggler=new ActionBarDrawerToggle(this,drawer,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(mToggler);
        mToggler.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_experince);
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


                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_experince);
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

                        lv_experience.setAdapter(adapter);
                        lv_experience.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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


    @Override
    public void onClick(View v) {
        if(v==addPost)
        {
            Intent in=new Intent(getApplicationContext(),Experince.class);
            startActivity(in);
        }

        else if(v==viewPost)
        {
            checkLogin();
            ExperienceList.clear();
            line.setVisibility(View.VISIBLE);

            //  listClicked();

        }


    }
    public void listClicked(int pos){
       /*         lv_experience.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Intent i=new Intent(getApplicationContext(),Experince.class);
                // startActivity(i);*/

        dialogbox = new Dialog(ExperienceView.this);


        dialogbox.setContentView(R.layout.experience_list_dialog);
        final   EditText CompanyNameD = (EditText) dialogbox
                .findViewById(R.id.JexDCompanyN);

        final EditText JobTitleD = (EditText) dialogbox
                .findViewById(R.id.JexDJobTitle);
        final EditText DurationOfWorkD = (EditText) dialogbox
                .findViewById(R.id.JexDDuratonOw);
        final Spinner FunctionAreaD = (Spinner) dialogbox
                .findViewById(R.id.JExDSpinerWA);         //  edamt kata kat use vacha  */
        Button JExDUpdate = (Button) dialogbox.findViewById(R.id.JExDUpdate);
        Button JExDDelete = (Button) dialogbox
                .findViewById(R.id.JExDDelete);
        Button close = (Button) dialogbox
                .findViewById(R.id.JExDClose);

        //for dialog box close
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogbox.dismiss();
                checkLogin();
                ExperienceList.clear();
            }
        });
        JExDDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });

        JExDUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                COMPANYNAME = CompanyNameD.getText().toString();
                JOBTitle=JobTitleD.getText().toString();
                DOW=DurationOfWorkD.getText().toString();
                FuncationalAreas=FunctionAreaD.getSelectedItem().toString();
                update();
                Toast.makeText(getApplicationContext(),"update sucessfully, Please Refresh",Toast.LENGTH_SHORT).show();
            }


        });
        for (int i=0;i<=ExperienceList.size();i++){
            if(pos==i){
                CompanyNameD.setText(ExperienceList.get(i).get("CompanyName"));
                JobTitleD.setText(ExperienceList.get(i).get("JobTitle"));
                DurationOfWorkD.setText(ExperienceList.get(i).get("DurationOfWork"));



            }
        }

        dialogbox.show();


    }

    public void update() {
        // Tag used to cancel the request
        final StringRequest strReq = new StringRequest(Request.Method.POST,AppConfig.URL_Jobseeker_Experience_Update, new Response.Listener<String>() {

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

                for (int i=0;i<=ExperienceList.size();i++) {
                    if(pos1==i){ params.put("id", ExperienceList.get(i).get("id"));}

                }
                params.put("CompanyName",COMPANYNAME);
                params.put("JobTitle",JOBTitle);
                params.put("DurationOfWork",DOW);
                params.put("FuctionArea",FuncationalAreas);
                Log.d("company name",COMPANYNAME);

                return params;
            }
        };

        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }
    public void delete() {
        // Tag used to cancel the request
        final StringRequest strReq = new StringRequest(Request.Method.POST,AppConfig.URL_Jobseeker_Experience_Delete, new Response.Listener<String>() {

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
                for (int i=0;i<=ExperienceList.size();i++) {
                    if(i==pos1){ params.put("id", ExperienceList.get(i).get("Id"));}
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
           // nav_img.setImageBitmap(b);
            imgVc.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }


}
