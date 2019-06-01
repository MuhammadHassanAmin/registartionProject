package com.goprogs.riphahportalquiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Assign ID's to textview and button.
        textView = (TextView)findViewById(R.id.TextViewUserEmail);

        // Receiving value into activity using intent.
        String TempHolder = getIntent().getStringExtra("Id");

        // Setting up received value into TextView.
        textView.setText(textView.getText() + TempHolder);

    }
}
