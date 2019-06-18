package com.goprogs.riphahportalquiz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import javax.xml.transform.Result;

public class MatchResult extends AppCompatActivity {
    TextView result,opponentPoints,yourPoints;
    DrawerLayout mDrawerLayout;
    String matchID;
    FirebaseDatabase database;
    DatabaseReference reference;
    MatchModel matchModel;
    SharedPreferences pref;
    ProgressDialog progressDialog;
    Button btnProfile;
    ImageView yourDP,oponentDP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_result);
        //intent
        Intent intent = getIntent();
        matchID  = intent.getStringExtra("match_id");
        pref = getSharedPreferences("user_details" , MODE_PRIVATE);
        //initializing views
         result = findViewById(R.id.tv_result_Match_Result);
         opponentPoints = findViewById(R.id.tv_opponentPoints_Match_Result);
        yourPoints = findViewById(R.id.tv_yourPoints_Match_Result);
        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        btnProfile= findViewById(R.id.btn_profile_match_finish);
       yourDP = findViewById(R.id.yourDP);
       oponentDP = findViewById(R.id.opDP);

       Picasso.get().load(pref.getString("dp" ,"")).into(yourDP);

        //getting match data
        progressDialog.show();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("matches").child(matchID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    matchModel = dataSnapshot.getValue(MatchModel.class);

                onDataReceive(matchModel);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Click Listeners
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextIntent;
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
                        } else if (id == R.id.nav_notificaitons) {
                            Intent intent = new Intent(getApplicationContext(), Notifications.class);
                            startActivity(intent);
                        }else if (id == R.id.nav_take_quiz) {
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


    private void onDataReceive(MatchModel matchModel){


            if (pref.getInt("userID",0) == (matchModel.getCompetitor_ID()))
            {
                // User is a competitor
                //getting OpponentData
                getUserData(matchModel.getOpponent_ID());
                //won or lost?
                if (matchModel.getCompetitor_Points() == matchModel.getOpponent_Points())
                {
                    result.setText("Draw");
                }else if (matchModel.getCompetitor_Points() > matchModel.getOpponent_Points()){
                    result.setText("Won");

                }else
                {
                    result.setText("Lose");
                }
                //setting user points
                yourPoints.setText(Integer.toString(matchModel.getCompetitor_Points()));
                opponentPoints.setText(Integer.toString(matchModel.getOpponent_Points()));
            }
            else {

                // User is a opponent
                //getting OpponentData
                getUserData(matchModel.getCompetitor_ID());
                //won or lost?
                if (matchModel.getCompetitor_Points() == matchModel.getOpponent_Points())
                {
                    result.setText("Draw");
                }else if (matchModel.getOpponent_Points() > matchModel.getCompetitor_Points()){
                    result.setText("Won");

                }else
                {
                    result.setText("Lose");
                }
                //setting user points

                yourPoints.setText(Integer.toString(matchModel.getOpponent_Points()));
                opponentPoints.setText(Integer.toString(matchModel.getCompetitor_Points()));

            }
        progressDialog.dismiss();
    }
    void getUserData(int ID){



// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://riphahportal.com/API/user_api_handler.php?action=fetch_single&id="+ID;

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        String userDpForIV;
                        try {
                            JSONObject obj = new JSONObject(response);
                            userDpForIV = "https://riphahportal.com/storage/profiles/"+obj.getString("picture");



                            if (obj.getString("picture").equals("nopic"))
                            {
                                userDpForIV="https://riphahportal.com/images/default-profile.jpg";
                            }

                           Picasso.get().load(userDpForIV).into(oponentDP);
                        } catch (Throwable t) {
                            Log.e("My App", "Could not parse malformed JSON: \"" + response + "\"");
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error finding Opponent DP",Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }
}
