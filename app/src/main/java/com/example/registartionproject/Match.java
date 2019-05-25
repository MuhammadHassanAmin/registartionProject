package com.example.registartionproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Match extends AppCompatActivity {
    ArrayList<questionModel> quizQuestions = new ArrayList<questionModel>();

    TextView stat ;
        CheckBox opt1 ;
        CheckBox opt2 ;
        CheckBox opt3 ;
        CheckBox opt4;
        Button checkAnsbtn ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        SharedPreferences pref = getSharedPreferences("user_details", MODE_PRIVATE);
        Intent intent = getIntent();


        String quizTopic = intent.getStringExtra("quizTopic");

         stat = findViewById(R.id.tvStat);
         opt1 = findViewById(R.id.chkopt1);
         opt2 = findViewById(R.id.chkopt2);
         opt3 = findViewById(R.id.chkopt3);
         opt4 = findViewById(R.id.chkopt4);
        checkAnsbtn = (Button) findViewById(R.id.btnSubmitAns);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference().child("quizQuestions").child(quizTopic);
        quizQuestions = loadQuestionFromDB(ref);
       while (quizQuestions.isEmpty());
        Toast.makeText(this,quizQuestions.get(0).getA(),Toast.LENGTH_SHORT).show();

            /* startQuiz();*/

    }

   private  void startQuiz(){
        int count = 0;

        Toast.makeText(this,quizQuestions.get(count).getStatement(),Toast.LENGTH_SHORT).show();
        stat.setText(quizQuestions.get(count).getStatement());
        opt1.setText(quizQuestions.get(count).getA());
        opt2.setText(quizQuestions.get(count).getB());
        opt3.setText(quizQuestions.get(count).getC());
        opt4.setText(quizQuestions.get(count).getD());

    }

    private  ArrayList<questionModel> loadQuestionFromDB(final DatabaseReference ref){
        final ArrayList<questionModel> quizQuestions = new ArrayList<>();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i=0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    questionModel question = snapshot.getValue(questionModel.class);

                    quizQuestions.add(question);

                    if (i==4) {
                        break;
                    }
                    i++;
                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"The read failed: " + databaseError.getCode(),Toast.LENGTH_SHORT).show();
            }
        });
    return quizQuestions;
    }

}
