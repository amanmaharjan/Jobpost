package com.example.welcome.jobpost;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.Button;
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
import org.w3c.dom.Text;

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

import static java.security.AccessController.getContext;

public class CompanyProfilePage extends AppCompatActivity {
    TextView CompanyName,Address,Email,ContactPerson,Mobile,About;
    Button editProfile,resetPassword;
    SQLiteHandler dbhelper;
    ImageView img;
    private Bitmap bitmap;

    DrawerLayout drawer;
    ActionBarDrawerToggle mToggler;
    NavigationView navigationView;
    SessionManager session;

    ImageView nav_img;
    CircleImageView imgVc;
    TextView nav_firstname;
    TextView nav_email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_profile_page);
        CompanyName=(TextView)findViewById(R.id.txt_Cname);
        Address=(TextView)findViewById(R.id.txt_Caddress);
        Email=(TextView)findViewById(R.id.txt_Cemail);
        ContactPerson=(TextView) findViewById(R.id.txt_CcontactPerson);
        Mobile=(TextView)findViewById(R.id.txt_CcontactPersonPhone);
        About=(TextView)findViewById(R.id.txt_Cabout);
        img=(ImageView)findViewById(R.id.imgcl);
        editProfile=(Button)findViewById(R.id.btn_cpEditP);
        // SqLite database handler
        dbhelper = new SQLiteHandler(getApplicationContext());
        session =new SessionManager(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer=(DrawerLayout)findViewById(R.id.drawer_layout_company_profile);
        mToggler=new ActionBarDrawerToggle(this,drawer,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(mToggler);
        mToggler.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_company_profile);
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


                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_company_profile);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });



        // Fetching user details from sqlite
        // Displaying the user details on the screen
        HashMap<String, String> user = dbhelper.getCompanyDetails();
        CompanyName.setText(user.get("CompanyName"));
        Address.setText(user.get("Address"));
        Email.setText(user.get("email"));
        ContactPerson.setText(user.get("Contact Person"));
        Mobile.setText(user.get("mobile"));
        About.setText(user.get("about"));

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(getApplicationContext(),EditCompanyProfile.class);
                startActivity(in);
            }
        });

       // loadImageFromStorage();

        View header=navigationView.getHeaderView(0);
        nav_firstname=(TextView)header.findViewById(R.id.nav_username);
        nav_email=(TextView) header.findViewById(R.id.textView);
        nav_img=(ImageView)header.findViewById(R.id.imageView);
        imgVc=(CircleImageView)header.findViewById(R.id.imgjlc);
        nav_firstname.setText(session.getFirstName()+" "+session.getLastName());
        nav_email.setText(session.getEmail());
        loadImageFromStorage1(session.getImagePath());
       // loadImageFromStorage();
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
    //save the image to the local internal storage

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
                File images=new File(directory, session.getEmail()+"_profile.jpg");
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(images));
                img.setImageBitmap(b);
               // nav_img.setImageBitmap(b);
                imgVc.setImageBitmap(b);

            }
            else {
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                img.setImageBitmap(b);
               // nav_img.setImageBitmap(b);
                imgVc.setImageBitmap(b);

            }

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }

    /*
    public boolean isFilePresent(String fileName) {
        String path = getContext().getFilesDir().getAbsolutePath() + "/" + fileName;
        File file = new File(path);
        return file.exists();
    }

    */
   /*
    public boolean fileExistance(String fname){
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }

    */
    private void getImage() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_Download_Logo,

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

                Toast.makeText(getApplicationContext(), "Download Successful", Toast.LENGTH_LONG).show();

            }
            else{
                Toast.makeText(getApplicationContext(), "Connect TO netwoerk", Toast.LENGTH_LONG).show();


            }        }
    }

    private void loadImageFromStorage1(String path)
    {

        try {
            File f=new File(path, session.getEmail()+"_profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            //imageView.setImageBitmap(b);
           // nav_img.setImageBitmap(b);
            img.setImageBitmap(b);
            imgVc.setImageBitmap(b);

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }

}
