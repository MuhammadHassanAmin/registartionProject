package com.goprogs.riphahportalquiz;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ListView;
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

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class LeaderBoard extends AppCompatActivity {

    ListView listView;
    ArrayList<userWins> userWins = new ArrayList<userWins>();
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    int count=-1;
    DrawerLayout mDrawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        listView = findViewById(R.id.lv_leaderboard);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("matches");



        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            MatchModel matchModel = new MatchModel();

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    matchModel = snapshot.getValue(MatchModel.class);
                    if (matchModel.isIfFinished()) {
                        if (matchModel.getCompetitor_Points() > matchModel.getOpponent_Points()) {
                            addWin(matchModel.getCompetitor_ID());

                        } else if (matchModel.getCompetitor_Points() < matchModel.getOpponent_Points()) {
                            addWin(matchModel.getOpponent_ID());
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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


    void addWin(int userID) {
        int i = 0;
        boolean exist = false;
        while (i < userWins.size()) {
            if (userWins.get(i).getUserID() == userID) {
                userWins.get(i).setWinCount(userWins.get(i).getWinCount()+1);
                exist = true;

                break;
            }
            i++;
        }
        if (!exist){
            userWins userWinsobj = new userWins();

            userWinsobj.setUserID(userID);
            userWinsobj.setWinCount(1);

            userWins.add(userWinsobj);
            getUserData(userID);

        }
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
                        count++;

                        try {
                            JSONObject obj = new JSONObject(response);
                            userWins.get(count).setDp("https://riphahportal.com/storage/profiles/"+obj.getString("picture"));

                            userWins.get(count).setUserName(obj.getString("name"));

                            if (obj.getString("picture").equals("nopic"))
                            {
                                userWins.get(count).setDp("https://riphahportal.com/images/default-profile.jpg");
                            }
                        } catch (Throwable t) {
                            Log.e("My App", "Could not parse malformed JSON: \"" + response + "\"");
                        }


                        if (count>=(userWins.size()-1)){

                            setAdapter();
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
    public void setAdapter() {
        ArrayList<userWins> tempList = new ArrayList<>();
        int i = 0;
        int max=0;
        while (i<userWins.size())
        {
            int maxCount = userWins.get(i).getWinCount();
            max=i;
            int j = i+1;
            while (j<userWins.size()){
                if (userWins.get(j).getWinCount() > maxCount)
                max=j;
                j++;
            }
            tempList.add(userWins.get(max));
            userWins.remove(max);

        }

        CustomAdapter_usersWins adapter_usersWins = new CustomAdapter_usersWins(this,tempList);
        listView.setAdapter(adapter_usersWins);

    }
}
