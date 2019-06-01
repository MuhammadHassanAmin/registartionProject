package com.goprogs.riphahportalquiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class DefaultActivity extends AppCompatActivity {
    SharedPreferences pref;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default);
         pref = getSharedPreferences("user_details", MODE_PRIVATE);
        if (pref.contains("userID") && pref.contains("userName")) {
            intent = new Intent(DefaultActivity.this,LoginActivity.class);
            startActivity(intent);
        }


    }

    public void defaultloginaction(View view){
        intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }
    public void defaultSignupaction(View view){
        intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

}
