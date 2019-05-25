package com.example.registartionproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StartMatch extends AppCompatActivity {


    Intent intent1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_match);
        SharedPreferences pref = getSharedPreferences("user_details", MODE_PRIVATE);
        Intent intent1 = getIntent();
        setIntent1(intent1);
    /*    intent.getStringExtra("opponent_uid");
        intent.getStringExtra("opponent_email");
        intent.getStringExtra("opponent_name");
        intent.getStringExtra("opponent_dp");
        intent.getStringExtra("quizTopic");*/




        TextView tvTopicHeading= (TextView) findViewById(R.id.tv_QuizTopicHeading);
        tvTopicHeading.setText(intent1.getStringExtra("quizTopic"));
        Button btnStartMatch = findViewById(R.id.btnStartMatch);
        btnStartMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(),Match.class);
                    intent.putExtras(getintent());
                    startActivity(intent);
            }
        });
    }
    private Intent  getintent(){
        return this.intent1;
    }
    private void setIntent1(Intent intent1) {
        this.intent1 = intent1;
    }
}
