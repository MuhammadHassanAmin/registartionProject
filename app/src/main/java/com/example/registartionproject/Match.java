package com.example.registartionproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class Match extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        SharedPreferences pref = getSharedPreferences("user_details", MODE_PRIVATE);
        Intent intent = getIntent();
        Toast.makeText(this, intent.getStringExtra("opponent_uid"),Toast.LENGTH_SHORT).show();

        /*intent.getStringExtra("opponent_email");
        intent.getStringExtra("opponent_name");
        intent.getStringExtra("opponent_dp");
        intent.getStringExtra("quizTopic");*/
    }
}
