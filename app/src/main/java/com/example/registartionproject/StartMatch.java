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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_match);
        SharedPreferences pref = getSharedPreferences("user_details", MODE_PRIVATE);
        Intent intent = getIntent();
        String opponent_id = intent.getStringExtra("opponent_id");
        TextView tvTopicHeading= (TextView) findViewById(R.id.tv_QuizTopicHeading);
        tvTopicHeading.setText(intent.getStringExtra("quizTopic"));
        Button btnStartMatch = findViewById(R.id.btnStartMatch);
        btnStartMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = getIntent();
                    startActivity(intent);
            }
        });
    }
}
