package com.example.welcome.jobpost;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class JobLogedInPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawer;
    ActionBarDrawerToggle mToggler;
    NavigationView navigationView;
    private SQLiteHandler db;
    ImageView imgV;
    GridView gV;

    ImageView nav_img;
    CircleImageView imgVc;
    TextView nav_firstname;
    TextView nav_email;
    ProgressDialog loading;

    //private SessionManager session;
    private SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_loged_in_page);

////        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_jobseeker);
//        setSupportActionBar(toolbar);


        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
       // session = new SessionManager(getApplicationContext());
        session = new SessionManager(getApplicationContext());


        if (!session.isLoggedIn()) {
            logoutUser();
        }

        //initalzing the component
        imgV=(ImageView)findViewById(R.id.imgjl);
        gV=(GridView)findViewById(R.id.gvJL);

        //populating the gridview with data
        gV.setAdapter(new MyGridAdapter(this));

        //Setting item click if GridView
        gV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), "Item Clicked", Toast.LENGTH_SHORT).show();

            if (position==0){
                Intent in=new Intent(getApplicationContext(),JobSeeker.class);
                startActivity(in);
            }
            else if(position==1){
                Intent in=new Intent(getApplicationContext(),EducationView.class);
                startActivity(in);
            }
            else if(position==2){
                Intent in=new Intent(getApplicationContext(),ExperienceView.class);
                startActivity(in);
            }
            else if(position==3){
                Intent in=new Intent(getApplicationContext(),Skills.class);
                startActivity(in);
            }
            else if(position==4){
                Intent in=new Intent(getApplicationContext(),CV.class);
                startActivity(in);
            }
            else if(position==5){
                Intent in=new Intent(getApplicationContext(),Jobs.class);
                startActivity(in);

            }
            else {
                logoutUser();

            }
            }
        });

        ///for navigations
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer=(DrawerLayout)findViewById(R.id.drawer_layout_jobseeker);
        mToggler=new ActionBarDrawerToggle(this,drawer,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(mToggler);
        mToggler.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_jobseeker);

        navigationView.setNavigationItemSelectedListener(this);



        //chinako navigation ko contain
        View header=navigationView.getHeaderView(0);
        nav_firstname=(TextView)header.findViewById(R.id.nav_username);
        nav_email=(TextView) header.findViewById(R.id.textView);
        nav_img=(ImageView)header.findViewById(R.id.imageView);
        imgVc=(CircleImageView)header.findViewById(R.id.imgjlc);
        nav_firstname.setText(session.getFirstName()+" "+session.getLastName());
        nav_email.setText(session.getEmail());
        //loadImageFromStorage(session.getImagePath());
        loading = ProgressDialog.show(this, "Content Loading...", "Please wait...", false, false);
        loadImageFromStorage();



    }
    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    public void logoutUser() {
        //session.setLogin(false,"","","");
        session.setLogout();
        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(getApplicationContext(), Welcome.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_jobseeker);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            // Handle the camera action
            Intent in=new Intent(getApplicationContext(),Welcome.class);
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


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_jobseeker);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void loadImageFromStorage(String path)
    {

        try {
            File f=new File(path, session.getEmail()+"_profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            //imageView.setImageBitmap(b);
            nav_img.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }
    private void loadImageFromStorage()
    {

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        //File mypath=new File(directory,session.getEmail()+"_profile.jpg");

        try {
            File f=new File(directory, session.getEmail()+"_profile.jpg");
            if(!f.exists())
            {
                getImage();

                session.setImagePath(directory.toString());
                File images=new File(directory, session.getEmail()+"_profile.jpg");
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(images));
                //img.setImageBitmap(b);
               // nav_img.setImageBitmap(b);
                imgVc.setImageBitmap(b);
                loading.dismiss();

            }
            else {

                session.setImagePath(directory.toString());
                Log.i("ImagePath",f.toString());
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                // img.setImageBitmap(b);
                nav_img.setImageBitmap(b);
                imgVc.setImageBitmap(b);
                loading.dismiss();

            }

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }

    private void getImage() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_Download_Profile,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            // Toast.makeText(getApplicationContext(),obj.getString("message"), Toast.LENGTH_SHORT).show();

                            String pdfUrl = obj.getString("url");
                            // Toast.makeText(getApplicationContext(),pdfUrl,Toast.LENGTH_SHORT).show();
                            Log.e("CmpanyProfilePAge",pdfUrl);

                            if(pdfUrl==null)
                            {
                                loading.dismiss();
                                Toast.makeText(getApplicationContext(),
                                        "No internet Connection ...Please connect to network", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                               MyTask myTask = new MyTask();
                                myTask.execute(pdfUrl);

                            }


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
                params.put("email",session.getEmail());
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
//                file = new File(Environment.getExternalStoragePublicDirectory
//                        (Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()
//                        + "/" + Uri.parse(params[0]).getLastPathSegment());
                ContextWrapper cw = new ContextWrapper(getApplicationContext());
                // path to /data/data/yourapp/app_data/imageDir
                File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                // Create imageDir
                //File mypath=new File(directory,session.getEmail()+"_profile.jpg");
                file=new File(directory, session.getEmail()+"_profile.jpg");
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
                loading.dismiss();


                //Toast.makeText(getApplicationContext(), "Download Successful", Toast.LENGTH_LONG).show();

            }
            else{
                loading.dismiss();
                Toast.makeText(getApplicationContext(), "Connect TO netwoerk", Toast.LENGTH_LONG).show();


            }        }
    }
}
