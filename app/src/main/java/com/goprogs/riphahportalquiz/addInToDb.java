package com.goprogs.riphahportalquiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class addInToDb extends AppCompatActivity {

    TextView qstat, op1,op2,op3,op4,correctopt,statment;
    Button btnSubmit;
    FirebaseDatabase database;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Map question = new HashMap();


        setContentView(R.layout.activity_add_in_to_db);

        statment = findViewById(R.id.q);

        op1 = findViewById(R.id.q2);
        op2 = findViewById(R.id.q3);
        op3 = findViewById(R.id.q4);
        op4 = findViewById(R.id.q5);

        correctopt = findViewById(R.id.q6);


        btnSubmit = findViewById(R.id.addQ);

        database= FirebaseDatabase.getInstance();
        reference=database.getReference().child("quizQuestions").child("Android");

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference newRef = reference.push();
                String newID = newRef.getKey();
                question.put("statement", statment.getText().toString());
                question.put("a", op1.getText().toString());
                question.put("b", op2.getText().toString());
                question.put("c", op3.getText().toString());
                question.put("d", op4.getText().toString());
                question.put("correctOpt", correctopt.getText().toString());
                newRef.setValue(question);

            }
        });

    }
}
