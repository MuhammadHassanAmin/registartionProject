package com.goprogs.riphahportalquiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
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
    DrawerLayout mDrawerLayout;
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



        // Create Navigation drawer and inlfate layout
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);


        // Adding menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            VectorDrawableCompat indicator
                    = VectorDrawableCompat.create(getResources(), R.drawable.ic_menu, getTheme());
            indicator.setTint(ResourcesCompat.getColor(getResources(),R.color.white,getTheme()));
            supportActionBar.setHomeAsUpIndicator(indicator);
            supportActionBar.setDisplayHomeAsUpEnabled(true);

        }

        // Set behavior of Navigation drawer
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    // This method will trigger on item Click of navigation menu
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // Set item in checked state
                        menuItem.setChecked(true);

                        // TODO: handle navigation
                        int id = menuItem.getItemId();

                        if (id == R.id.nav_home) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        } else if (id == R.id.nav_take_quiz) {
                            Intent intent = new Intent(getApplicationContext(), ChooseQuizActivity.class);
                            startActivity(intent);
                        } else if (id == R.id.nav_profile) {
                            Intent intent = new Intent(getApplicationContext(), Profile.class);
                            startActivity(intent);

                        }
                        else if (id == R.id.nav_logout) {
                            Intent intent = new Intent(getApplicationContext(), logout.class);
                            startActivity(intent);
                        }
                        else
                        {
                            return true;
                        }
                        // Closing drawer on item click
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }


        return super.onOptionsItemSelected(item);
    }

}
