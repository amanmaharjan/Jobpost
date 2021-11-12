package com.example.welcome.jobpost;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
Thread th = new Thread(){
    public void  run(){
        try {
            Thread.sleep(3000);
           // Intent i = new Intent(SplashScreen.this,Welcome.class);
            Intent i = new Intent(SplashScreen.this,Welcome.class);
            startActivity(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

};   th.start();
    }

    @Override
    protected void onPause() {
        finish();
        super.onPause();
    }
}
