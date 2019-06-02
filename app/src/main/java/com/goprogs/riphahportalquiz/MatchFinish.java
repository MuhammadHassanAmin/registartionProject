package com.goprogs.riphahportalquiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MatchFinish extends AppCompatActivity {
    Intent intent;
    FirebaseDatabase database;
    DatabaseReference dbRef;
    String userType;
    int score;
    TextView tvScore ;
    Button btnProfile;
    Intent nextIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_finish);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //iniatialize Views
        tvScore=findViewById(R.id.tvYourScoreText);
        btnProfile = findViewById(R.id.btnGoToProfile_MatchFinish);
        //Getting shared pref and intent
        final SharedPreferences pref = getSharedPreferences("user_details", MODE_PRIVATE);
        intent = getIntent();

        score = intent.getIntExtra("score", 0);
        tvScore.setText(Integer.toString(score));

        ImageView userImg = findViewById(R.id.ivUserImg_Profile);
        Picasso.get().load(pref.getString("dp","test")).resize(250, 250)
                .centerCrop().into(userImg);
         //Database
        database= FirebaseDatabase.getInstance();
        dbRef=database.getReference().child("matches");



        //Click Listeners
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextIntent = new Intent(getApplicationContext(),Profile.class);
                startActivity(nextIntent);
            }
        });
    }

}
