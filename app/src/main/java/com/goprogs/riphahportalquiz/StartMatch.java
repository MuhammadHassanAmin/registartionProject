package com.goprogs.riphahportalquiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import static java.lang.Integer.parseInt;

public class StartMatch extends AppCompatActivity {


    Intent intent1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_match);
        final SharedPreferences pref = getSharedPreferences("user_details", MODE_PRIVATE);
        Intent intent1 = getIntent();
        setIntent1(intent1);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef= db.getReference().child("matches");


        ImageView userImg = findViewById(R.id.ivUserImg_Profile);
        ImageView opponentImg = findViewById(R.id.ivOpponentImg_StartMacth);

        Picasso.get().load(pref.getString("dp","test")).resize(250, 250)
                .centerCrop().into(userImg);
        Picasso.get().load(intent1.getStringExtra("opponent_dp")).resize(250, 250)
                .centerCrop().into(opponentImg);




        TextView tvTopicHeading= (TextView) findViewById(R.id.tvYourScoreText);
        tvTopicHeading.setText(intent1.getStringExtra("quizTopic"));
        Button btnStartMatch = findViewById(R.id.btnGoToProfile_MatchFinish);
        btnStartMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MatchModel matchModel = new MatchModel();
                matchModel.competitor_ID = pref.getInt("userID",0);
                matchModel.opponent_ID = parseInt(getintent().getStringExtra("opponent_uid"));
                matchModel.topic_Name = getintent().getStringExtra("quizTopic");

                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference ref = db.getReference().child("matches");
                DatabaseReference newRef = ref.push();
                String match_id = newRef.getKey();
                MatchQuestions test = new MatchQuestions();
                test.setQuestionID("empty");
                test.setCompetitor_Answer("empty");
                test.setOpponent_Answer("empty");
                matchModel.matchQuestions.put("question1",test );
                matchModel.matchQuestions.put("question2",test );
                matchModel.matchQuestions.put("question3",test );
                matchModel.matchQuestions.put("question4",test );

                newRef.setValue(matchModel);

                Intent intent = new Intent(getApplicationContext(),Match.class);
                intent.putExtras(getintent().getExtras());
                intent.putExtra("match_id",match_id);

                intent.putExtra("userType","competitor");

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
