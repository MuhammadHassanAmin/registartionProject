package com.goprogs.riphahportalquiz;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

// Firebase

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChooseQuizActivity extends AppCompatActivity {
    Activity currentActivity = this;
    ProgressDialog progressDialog;
DrawerLayout mDrawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_quiz);

        SharedPreferences pref = getSharedPreferences("user_details", MODE_PRIVATE);

        FirebaseDatabase rootRef = FirebaseDatabase.getInstance();
        DatabaseReference quizRef = rootRef.getReference().child("quizTopic");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

                // Get a reference to our posts

        DatabaseReference ref = quizRef;

        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<quizTopicModel> quizTopicList = new ArrayList<quizTopicModel>();
                CustomAdapter_ChooseQuizTopic adapter_chooseQuizTopic = new CustomAdapter_ChooseQuizTopic(currentActivity, quizTopicList);
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    quizTopicModel quiztopic = postSnapshot.getValue(quizTopicModel.class);
                    quizTopicList.add(quiztopic);
                    adapter_chooseQuizTopic.notifyDataSetChanged();
                    progressDialog.dismiss();
                }



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
                        }else if (id == R.id.nav_notificaitons) {
                            Intent intent = new Intent(getApplicationContext(), Notifications.class);
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
                        }else if (id == R.id.nav_leaderboard) {
                            Intent intent = new Intent(getApplicationContext(), LeaderBoard.class);
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
