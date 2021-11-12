package com.example.welcome.jobpost;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DatabaseShowTime extends AppCompatActivity {
    private  ArrayList<JobseekerProductionModule> list2 =new ArrayList<>();
    TextView name,addres,mobile,email,username,password,nationality;
    JobseekerDatabaseHelper dbhelper;
    JobseekerProductionModule pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_show_time);

        name=(TextView)findViewById(R.id.txtname);
        addres=(TextView)findViewById(R.id.txtaddress);
        email=(TextView)findViewById(R.id.txtemail);
        username=(TextView)findViewById(R.id.txtusername);
        password=(TextView)findViewById(R.id.txtpassword);
        nationality=(TextView)findViewById(R.id.txtnationality);



        dbhelper=new JobseekerDatabaseHelper(getApplicationContext());
        dbhelper.getWritableDatabase();
        pd=new JobseekerProductionModule();
        ArrayList<JobseekerProductionModule> mylist=dbhelper.getProductsphp();
        list2.clear();
        for (int i= 0;i<mylist.size();i++){

            String n = mylist.get(i).getpFirstName();
            pd.setpFirstName(n);

            list2.add(pd);
            name.setText(list2.get(i).getpFirstName());
        }

        dbhelper.close();




    }




   /* public class spinner extends AppCompatActivity {
        String[] list;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_spinner);

            list = getResources().getStringArray(R.array.Nationality);

            //set first time hint here
            nationailty.setPrompt("Select a planet");


            final ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,list);

            arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            nationality.setAdapter(arrayAdapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //change your hint here
                    nationality.setPrompt(list[position]);
                    Toast.makeText(getApplicationContext(),"You have selected "+list[position],Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
    }*/






}

