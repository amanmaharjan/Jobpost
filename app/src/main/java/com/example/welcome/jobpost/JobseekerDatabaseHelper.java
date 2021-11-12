package com.example.welcome.jobpost;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by welcome on 4/10/2017.
 */

public class JobseekerDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = SQLiteHandler.class.getSimpleName();

   //refernece the components
    private ArrayList<JobseekerProductionModule> list = new ArrayList<JobseekerProductionModule>();
    public  static final String DATABASE_NAME="db_androidlogin" ;
    public  static  final String TABLE_MAIN= "user";
    public static final String TABLE_ID= "id";
    public  static final String TABLE_FirstName ="firstName";
    public  static final String TABLE_LastName="secondName";
    public  static final String TABLE_CurrentAddress="currentAdress";
    public  static final String TABLE_Mobilenumber="mobileNumber";
    public  static final String TABLE_DOB="dob";
    public  static final String TABLE_Email="email";
    public  static final String TABLE_UserName="userName";
    public  static final String TABLE_Password="password";
    public  static final String TABLE_ConformPassword="conformPassword";
    public  static final String TABLE_NationalitySpinner="nationality";
    public  static final String TABLE_FunctonalAreaSpinner="functionalArea";
    public  static final String TABLE_SalarySpinner="salary";
    public  static final String TABLE_GenderRadioB="gender";

    public JobseekerDatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table user"+"(id integer primary key,firstName text ,secondName text,currentAdress text,mobileNumber text," +
                "dob text,email text,userName text,password text,conformPassword text,nationality text,functionalArea text,salary text,gender text)");// mathi ko key ho
        Log.d(TAG, "Database tables created");

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_MAIN );//mathi ko key
        onCreate(db);

    }

public boolean insertSignUpData(JobseekerProductionModule pd)
{
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("firstName", pd.pFirstName);
    contentValues.put("secondName", pd.pLastName);
    contentValues.put("currentAdress", pd.pCurrentAddress);
    contentValues.put("mobileNumber", pd.pMobilenumber);
    contentValues.put("dob", pd.pDOB);
    contentValues.put("email", pd.pEmail);
    contentValues.put("userName", pd.pUserName);
    contentValues.put("password",pd.pPassword);
    contentValues.put("conformPassword",pd.pConformPassword);
    contentValues.put("nationality",pd.pNationalitySpinner);
    contentValues.put("functionalArea",pd.pFunctonalAreaSpinner);
    contentValues.put("salary",pd.pSalarySpinner);
    contentValues.put("gender",pd.pGenderRadioB);
    db.insert("user", null, contentValues);
    db.close();
    Log.d(TAG, "New user inserted into sqlite: ");
    return true;
}
    public boolean download(String name, String email,  String uid, String created_at )
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("firstName", name);
        contentValues.put("email", email);
        db.close();
        Log.d(TAG, "New user inserted into sqlite: "+name);
        return true;
    }
    public String getSinlgeEntry(String loginName)
    {
        SQLiteDatabase db = this.getReadableDatabase();
/*


Cursor cursor = db.rawQuery("SELECT password FROM jobseekerTable WHERE userName=?"+loginName, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String password= cursor.getString(cursor.getColumnIndex("password"));
        cursor.close();
        return password;
 */

        Cursor cursor = db.rawQuery("SELECT password FROM user WHERE userName=?",new String[] {loginName + ""});
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String password= cursor.getString(cursor.getColumnIndex("password"));
        cursor.close();
        return password;
    }


    public ArrayList<JobseekerProductionModule> getProductsphp() {
        /*
		 * get data from database through cursor and pass to class Php to
		 * ProductModule  chaiyeko and arraylist
		 */
        list.clear();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from user", null);
        // Cursor cursor=db.rawQuery("Select student from mytable where id=1",null
        Log.d(TAG, "liyo");
        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {


                    JobseekerProductionModule item = new JobseekerProductionModule();
                    item.pFirstName = cursor.getString(cursor.getColumnIndex("firstName"));

                   /* item.pLastName = cursor.getString(cursor
                            .getColumnIndex("secondName"));
                    item.pCurrentAddress = cursor.getString(cursor
                            .getColumnIndex("currentAdress"));
                    item.pMobilenumber = cursor.getString(cursor
                            .getColumnIndex("mobileNumber"));
                    item.pDOB = cursor.getString(cursor
                            .getColumnIndex("dob"));
                    item.pEmail = cursor.getString(cursor
                            .getColumnIndex("email"));
                    item.pUserName = cursor.getString(cursor
                            .getColumnIndex("userName"));
                    item.pPassword = cursor.getString(cursor
                            .getColumnIndex("password"));
                    item.pConformPassword = cursor.getString(cursor
                            .getColumnIndex("conformPassword"));
                    item.pNationalitySpinner = cursor.getString(cursor
                            .getColumnIndex("nationality"));
                    item.pSalarySpinner = cursor.getString(cursor
                            .getColumnIndex("salary"));
                    item.pFunctonalAreaSpinner = cursor.getString(cursor
                            .getColumnIndex("functionalArea"));
                    item.pGenderRadioB = cursor.getString(cursor
                            .getColumnIndex("gender"));*/

                    list.add(item);
                } while (cursor.moveToNext());
            }

        }
        cursor.close();
        db.close();
        return list;

    }
}
