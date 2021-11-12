package com.example.welcome.jobpost;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.welcome.jobpost.AppConfig;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONArray;
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
import java.util.UUID;



import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import de.hdodenhof.circleimageview.CircleImageView;

public class CV extends AppCompatActivity implements View.OnClickListener{

    DrawerLayout drawer;
    ActionBarDrawerToggle mToggler;
    NavigationView navigationView;
    private SQLiteHandler db;

    ImageView nav_img;
    TextView nav_firstname;
    CircleImageView imgVc;
    TextView nav_email;

    //Declaring views
    private Button buttonChoose;
    private Button buttonUpload;
    private  Button Download;
    private  SessionManager session;

   // public static final String UPLOAD_URL = "http://192.168.1.2/androidlogin/upload/uploadPdf.php";
   // public static final String PDF_FETCH_URL ="http://192.168.5.10/androidlogin/upload/downloadpdf.php";



    //Pdf request code
    private int PICK_PDF_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;


    //Uri to store the image uri
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cv);
        session=new SessionManager(getApplicationContext());

        //Initializing views
        buttonChoose = (Button) findViewById(R.id.btn_Jbrowser);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        Download=(Button)findViewById(R.id.btn_jDownload);


        //Setting clicklistener
        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
        Download.setOnClickListener(this);

        //Requesting storage permission
        requestStoragePermission();
        ///for navigations
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer=(DrawerLayout)findViewById(R.id.drawer_layout_cv);
        mToggler=new ActionBarDrawerToggle(this,drawer,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(mToggler);
        mToggler.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = (NavigationView) findViewById(R.id.nav_view_cv);
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


                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_cv);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

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

   /*
            * This is the method responsible for pdf upload
    * We need the full pdf path and the name for the pdf in this method
    * */

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void uploadMultipart() {
        //getting name for the image

        //getting the actual path of the image
        String path = FilePath.getPath(this, filePath);

        if (path == null) {

            Toast.makeText(this, "Please move your .pdf file to internal storage and retry", Toast.LENGTH_LONG).show();
        } else {
            //Uploading code
            try {
                String uploadId = UUID.randomUUID().toString();

                //Creating a multi part request
                new MultipartUploadRequest(this, uploadId, AppConfig.UPLOAD_URL)
                        .addFileToUpload(path, "pdf") //Adding file
                        .addParameter("name", session.getEmail()) //Adding text parameter to the request
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload(); //Starting the upload

            } catch (Exception exc) {
                Toast.makeText(getApplicationContext(),
                        "No internet Connection ...Please connect to network", Toast.LENGTH_LONG).show();
            }
        }
    }


    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST);
        buttonUpload.setVisibility(View.VISIBLE);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
        }
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
                Toast.makeText(getApplicationContext(), "Connect TO netwoerk", Toast.LENGTH_LONG).show();

            }        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        if (v == buttonChoose) {
            showFileChooser();
        }
        if (v == buttonUpload) {
            uploadMultipart();
        }
        if(v==Download){
            getPdfs();

        }

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
