package com.goprogs.riphahportalquiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.FontRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Notifications extends AppCompatActivity {
    FirebaseDatabase database;
    Query query;
    DatabaseReference databaseReference,dbrefForNewNotificaions;
    MatchModel matchModel;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    Notification_Data_Model notification_data_model;
    List<Notification_Data_Model> notification_data_modelList ;
    SharedPreferences pref;
    DrawerLayout mDrawerLayout;
    int newMatchesCount;
    TextView tvNewNotifications;
    int count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        database = FirebaseDatabase.getInstance();

        notification_data_modelList = new ArrayList<>();
        //init view
         tvNewNotifications = findViewById(R.id.tv_Card_NewNotificaitions_Count_Notifications);

        count =0;
        pref = getSharedPreferences("user_details", MODE_PRIVATE);

        recyclerView = (RecyclerView) findViewById(R.id.rcNewNotifications_Notifications);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        adapter = new Notifications_RV_Adapter(this,notification_data_modelList);

        recyclerView.setAdapter(adapter);


        matchModel = new MatchModel();

        setTvNewNotificationsCount();

        databaseReference=database.getReference().child("matches");
        Query query = databaseReference.orderByChild("opponent_ID").equalTo(pref.getInt("userID",0));



        query .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot match : dataSnapshot.getChildren()){
                        notification_data_model = new Notification_Data_Model();
                        matchModel = match.getValue(MatchModel.class);
                        matchModel.setMatch_id(match.getKey());
                        LoadUserData(matchModel.competitor_ID);
                        notification_data_model.setTopicName(matchModel.getTopic_Name());
                        notification_data_model.setMatchID(matchModel.getMatch_id());
                        notification_data_model.setMatchFinished(matchModel.isIfFinished());
                        notification_data_modelList.add(notification_data_model);
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                        }  else if (id == R.id.nav_take_quiz) {
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

    public void LoadUserData(int userID){


// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://riphahportal.com/API/user_api_handler.php?action=fetch_single&id="+userID;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                        try {
                            JSONObject obj = new JSONObject(response);
                         if (obj.getString("picture").equals("nopic"))
                             notification_data_modelList.get(count).setChallengerDP("https://riphahportal.com/images/default-profile.jpg");
                         else
                            notification_data_modelList.get(count).setChallengerDP("https://riphahportal.com/storage/profiles/"+obj.getString("picture"));
                            notification_data_modelList.get(count).setName(obj.getString("name"));
                            count++;
                            adapter.notifyDataSetChanged();
                        } catch (Throwable t) {
                            Log.e("My App", "Could not parse malformed JSON: \"" + response + "\"");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error finding user data",Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private  void  setTvNewNotificationsCount(){
        newMatchesCount=0;
        dbrefForNewNotificaions = database.getReference();
        query = dbrefForNewNotificaions.child("matches").orderByChild("opponent_ID").equalTo(pref.getInt("userID",0));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot count : dataSnapshot.getChildren())
                {
                    matchModel = count.getValue(MatchModel.class);

                    if (! matchModel.isIfFinished())
                        newMatchesCount++;


                }
                //set tvView
                tvNewNotifications.setText(Integer.toString(newMatchesCount));
                tvNewNotifications.setTextSize(52);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
