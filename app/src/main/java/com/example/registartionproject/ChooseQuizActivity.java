package com.example.registartionproject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

// Firebase

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChooseQuizActivity extends AppCompatActivity {
    Activity currentActivity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_quiz);
        SharedPreferences pref = getSharedPreferences("user_details", MODE_PRIVATE);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://quizriphahportal.firebaseio.com/quizTopic");
        DatabaseReference quizRef = rootRef;




// Get a reference to our posts

        DatabaseReference ref = quizRef;

// Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<quizTopicModel> quizTopicList = new ArrayList<quizTopicModel>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    quizTopicModel quiztopic = postSnapshot.getValue(quizTopicModel.class);
                    quizTopicList.add(quiztopic);
                }
                CustomAdapter_ChooseQuizTopic adapter_chooseQuizTopic = new CustomAdapter_ChooseQuizTopic(currentActivity,quizTopicList);
            final ListView lv = findViewById(R.id.lvQuizTopics);
                lv.setAdapter(adapter_chooseQuizTopic);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView <?> parent, View view, int position, long id) {
                        Intent chooseOponent = new Intent(currentActivity,ChooseOpponent.class);
                        String name = quizTopicList.get(position).getName();
                        chooseOponent.putExtra("quizTopic",name);
                        startActivity(chooseOponent);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("test", Integer.toString(databaseError.getCode()));
            }
        });


    }

}
