package com.example.welcome.jobpost;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by Aman on 4/20/2017.
 */

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "db_jobportal";

    // Login table name
    private static final String TABLE_USER = "mytable";
    private static final String TABLE_COMPANY = "tbl_company";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_FirstName = "FirstName";
    private static final String KEY_LastName = "LastName";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_CurrentAddress = "CurrentAddress";
    private static final String KEY_mobile = "mobile";
    private static final String KEY_DOB = "DOB";
    private static final String KEY_Nationality = "Nationality";
    private static final String KEY_FunctionalArea = "FunctionalArea";
    private static final String KEY_Salary = "Salary";
    private static final String KEY_Gender = "Gender";
    private static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at";


    //COmpany column Tables
    private static final String KEY_Company_ID = "id";
    private static final String KEY_CompanyName = "CompanyName";
    private static final String KEY_Address = "Address";
    private static final String KEY_USERNAME = "USERNAME";
    private static final String KEY_Company_EMAIL = "Email";
    private static final String KEY_CONTACT_PERSON_NAME = "PERSON_NAME";
    private static final String KEY_MOBILE = "Mobile";
    private static final String KEY_ABOUT = "About";


       public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        /*
       String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_FirstName + " TEXT, " + KEY_LastName + " TEXT, "
                + KEY_NAME + " TEXT, " + KEY_EMAIL + " TEXT, "
                + KEY_CurrentAddress + " TEXT, " + KEY_mobile + " TEXT, "
                + KEY_DOB + " TEXT, "+ KEY_Nationality + " TEXT, "
                + KEY_FunctionalArea + " TEXT, " + KEY_Salary + " TEXT, "
                + KEY_Gender + " TEXT, " + KEY_UID + " TEXT, "
                + KEY_CREATED_AT + " TEXT "+ ")";
                 db.execSQL(CREATE_LOGIN_TABLE);
                */
try{
    db.execSQL(" CREATE TABLE " + TABLE_USER + " ("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_FirstName + " TEXT, " + KEY_LastName + " TEXT, "
            + KEY_NAME + " TEXT, " + KEY_EMAIL + " TEXT, "
            + KEY_CurrentAddress + " TEXT, " + KEY_mobile + " TEXT, "
            + KEY_DOB + " TEXT, "+ KEY_Nationality + " TEXT, "
            + KEY_FunctionalArea + " TEXT, " + KEY_Salary + " TEXT, "
            + KEY_Gender + " TEXT, " + KEY_UID + " TEXT, "
            + KEY_CREATED_AT + " TEXT "+ " );");
    db.execSQL(" CREATE TABLE " + TABLE_COMPANY + " ("
            + KEY_Company_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_CompanyName + " TEXT, " + KEY_Address + " TEXT, "
            + KEY_USERNAME + " TEXT, " + KEY_Company_EMAIL + " TEXT, "
            + KEY_CONTACT_PERSON_NAME + " TEXT, "+ KEY_MOBILE + " TEXT, " + KEY_ABOUT + " TEXT "+ " );");

    Log.d(TAG, "Database tables created");

}
catch(Exception e){
    Log.d("EXCEPTION ON DATA BASE","ERROR !!!!!!!!!!!!!!!!!");

}

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPANY);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
           public void addUser1(String FirstName, String LastName,String name,String email,
                         String CurrentAddress, String mobile,String DOB, String Nationality,
                         String FunctionalArea, String Salary, String Gender,String uid, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FirstName, FirstName);
        values.put(KEY_LastName, LastName);
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_NAME, name); // Name
        values.put(KEY_CurrentAddress, CurrentAddress);
        values.put(KEY_mobile, mobile);
        values.put(KEY_DOB, DOB);
        values.put(KEY_Nationality, Nationality);
        values.put(KEY_FunctionalArea, FunctionalArea);
        values.put(KEY_Salary, Salary);
        values.put(KEY_Gender, Gender);
        values.put(KEY_UID, uid);
        values.put(KEY_CREATED_AT, created_at); // Created At

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id+name);
    }
    public void addCompanyData(String CompanyName, String Address,String email,String Contact, String Mobile, String About) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CompanyName, CompanyName);
        values.put(KEY_Address, Address);
        values.put(KEY_Company_EMAIL, email);
        values.put(KEY_CONTACT_PERSON_NAME, Contact);
        values.put(KEY_MOBILE, Mobile);
        values.put(KEY_ABOUT, About);

                // Inserting Row
        long id = db.insert(TABLE_COMPANY,null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id+CompanyName);
    }
        /**
     * Getting user data from database
     * */

        public HashMap<String, String> getUserDetails() {
            SQLiteDatabase db = this.getReadableDatabase();
            HashMap<String, String> user = new HashMap<String, String>();
            String selectQuery = "SELECT  * FROM " + TABLE_USER;
            Cursor cursor = db.rawQuery(selectQuery, null);
            // Move to first row
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                user.put("FirstName", cursor.getString(cursor.getColumnIndex("FirstName")));
                user.put("LastName", cursor.getString(cursor.getColumnIndex("LastName")));
                user.put("name", cursor.getString(cursor.getColumnIndex("name")));
                user.put("email", cursor.getString(cursor.getColumnIndex("email")));
                user.put("CurrentAddress", cursor.getString(cursor.getColumnIndex("CurrentAddress")));
                user.put("mobile", cursor.getString(cursor.getColumnIndex("mobile")));
                user.put("DOB", cursor.getString(cursor.getColumnIndex("DOB")));
                user.put("Nationality", cursor.getString(cursor.getColumnIndex("Nationality")));
                user.put("FunctionalArea", cursor.getString(cursor.getColumnIndex("FunctionalArea")));
                user.put("Salary", cursor.getString(cursor.getColumnIndex("Salary")));
                user.put("Gender", cursor.getString(cursor.getColumnIndex("Gender")));
                user.put("uid", cursor.getString(cursor.getColumnIndex("uid")));
                user.put("created_at", cursor.getString(cursor.getColumnIndex("created_at")));
                    }
            cursor.close();
            db.close();
            // return user
            Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

            return user;
        }

    public HashMap<String, String> getCompanyDetails() {
        SQLiteDatabase db = this.getReadableDatabase();
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_COMPANY;
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("CompanyName", cursor.getString(cursor.getColumnIndex("CompanyName")));
            user.put("Address", cursor.getString(cursor.getColumnIndex("Address")));
            user.put("UserName", cursor.getString(cursor.getColumnIndex("USERNAME")));
            user.put("email", cursor.getString(cursor.getColumnIndex("Email")));
            user.put("Contact Person", cursor.getString(cursor.getColumnIndex("PERSON_NAME")));
            user.put("mobile", cursor.getString(cursor.getColumnIndex("Mobile")));
            user.put("about", cursor.getString(cursor.getColumnIndex("About")));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }
    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);

        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }
    public void deleteCompany() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
                db.delete(TABLE_COMPANY, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

}

