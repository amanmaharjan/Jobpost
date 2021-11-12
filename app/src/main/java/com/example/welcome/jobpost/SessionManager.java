package com.example.welcome.jobpost;

/**
 * Created by Aman on 4/22/2017.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;

    // Shared preferences file name
    private static final String PREF_NAME = "PortalLogIn";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    public SessionManager (Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn, String email, String user,String Username) {

        editor.putBoolean("Login",isLoggedIn);
        editor.putString("email",email);
        editor.putString("user",user);
        editor.putString("username",Username);
        editor.commit();
        Log.d("SAVE DATA", "User login session modified!");
    }
    public void setLogout()
    {
        editor.putBoolean("Login",false);
        editor.putString("email","");
        editor.putString("user","");
        editor.putString("username","");
        editor.putString("path","");
        editor.putString("JobseekerEmail","");
        editor.putString("FirstName","");
        editor.putString("LastName","");
        editor.commit();
        Log.d("DELETE DATA", "User login session modified!");
    }
    public void setJobseelerEmail(String JobseekerEmail)
    {
        editor.putString("JobseekerEmail",JobseekerEmail);
        editor.commit();

    }

    public  void setFirstname(String Firstname, String LastName){
        editor.putString("FirstName",Firstname);
        editor.putString("LastName",LastName);
        editor.commit();
    }




    public void setImagePath(String path) {

        editor.putString("path",path);
        editor.commit();
        Log.d("SAVE DATA", "User login session modified!");
    }

    public boolean isLoggedIn(){

        Boolean LogStattue=pref.getBoolean("Login",false);
        return LogStattue;
    }
    public String getUser()
    {
        return pref.getString("user","");
    }
    public String getEmail(){return pref.getString("email",""); }
    public String getUserName(){return pref.getString("username","");}
    public String getImagePath(){return pref.getString("path","");}
    public String getJobseekerEmail(){return pref.getString("JobseekerEmail","");}
  public String getFirstName(){return pref.getString("FirstName","");}
    public String getLastName(){return pref.getString("LastName","");}

}


