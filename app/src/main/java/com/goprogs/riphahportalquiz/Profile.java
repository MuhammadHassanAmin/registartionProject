package com.goprogs.riphahportalquiz;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity implements DataReceivedListener {
    ImageView userDP;
    TextView userName;
    FirebaseDatabase database;
    DatabaseReference dbref;
    MatchModel matchModel;
    PastMatch_RC_Model pastMatch_rc_model;
    List<PastMatch_RC_Model> pastMatchesRcAdapterProfileList = new ArrayList<>();
    SharedPreferences pref;
    String userNameForTV,userDpForIV;

    RecyclerView.Adapter adapter_profile ;


    RecyclerView recyclerView;


   int count;
   DataReceivedListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);
        //initializing viws
        userDP= findViewById(R.id.ivUserImg_Profile);
        userName = findViewById(R.id.tvUserName_Profile);
        recyclerView = findViewById(R.id.rcPastMatches_Profile);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        // initialization variables
        count=0;
        matchModel = new MatchModel();
        pastMatch_rc_model= new PastMatch_RC_Model();

         pref = getSharedPreferences("user_details",MODE_PRIVATE);
        Picasso.get().load(pref.getString("dp","test")).resize(250, 250)
                .centerCrop().into(userDP);

        userName.setText(pref.getString("userName","Not Found"));

        database = FirebaseDatabase.getInstance();
        dbref = database.getReference().child("matches");
        /*Query query = dbref.orderByKey().equalTo(pref.getString("userID","noKey"));*/
        loadPastMatches(dbref,this);



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

                        try {
                            JSONObject obj = new JSONObject(response);
                            userDpForIV = "https://riphahportal.com/storage/profiles/"+obj.getString("picture");
                            if (obj.getString("picture").equals("nopic"));
                            {
                                userDpForIV="https://riphahportal.com/images/default-profile.jpg";
                            }

                            pastMatchesRcAdapterProfileList.get(count).setOpponentDp(userDpForIV);
                            pastMatchesRcAdapterProfileList.get(count).setOpponentName(userNameForTV);

                        } catch (Throwable t) {
                            Log.e("My App", "Could not parse malformed JSON: \"" + response + "\"");
                        }
                   count++;
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


    private  void loadPastMatches(DatabaseReference ref,final DataReceivedListener listener){

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot pastMatch : dataSnapshot.getChildren()){
                    matchModel = pastMatch.getValue(MatchModel.class);
                    pastMatch_rc_model.setMatchID(pastMatch.getKey());
                    pastMatch_rc_model.setQuizTopic(matchModel.getTopic_Name());
                    //checking and setting Match Result
                    if (pref.getInt("userID",0) == matchModel.getCompetitor_ID()) {
                        //If user is a competitor
                        if (matchModel.getCompetitor_Points() == matchModel.getOpponent_Points()){
                            pastMatch_rc_model.setResult("Draw");

                        }else{
                            if (matchModel.getCompetitor_Points() > matchModel.getOpponent_Points())
                            {
                                // is competitor is the winner
                                pastMatch_rc_model.setResult("Won");
                            }
                            else{
                                pastMatch_rc_model.setResult("Lost");

                            }
                        }
                    }else{
                        if (matchModel.getOpponent_Points() == matchModel.getCompetitor_Points()){
                            pastMatch_rc_model.setResult("Draw");

                        }else{
                            //If user is a oppnent
                            if (matchModel.getOpponent_Points() > matchModel.getCompetitor_Points())
                            {
                                // is opponent is the winner
                                pastMatch_rc_model.setResult("Lose");
                            }
                            else{
                                pastMatch_rc_model.setResult("Win");

                            }
                        }
                    }
                    //getting User Data
                    if (pref.getInt("userID",0) == matchModel.getCompetitor_ID()){
                        getUserData(matchModel.getOpponent_ID());

                    }else{
                        getUserData(matchModel.getCompetitor_ID());
                    }


                    pastMatchesRcAdapterProfileList.add(pastMatch_rc_model);
                }
                listener.onDataReceived_PastMatches(pastMatchesRcAdapterProfileList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    @Override
    public void onDataReceived_PastMatches(List<PastMatch_RC_Model> PastMatches) {
        String name= pastMatchesRcAdapterProfileList.get(pastMatchesRcAdapterProfileList.size()-1).getOpponentName();

        Toast.makeText(this,"data reciveved",Toast.LENGTH_SHORT).show();
        adapter_profile = new pastMatches_RCAdapter_Profile(this,pastMatchesRcAdapterProfileList);

        recyclerView.setAdapter(adapter_profile);

        adapter_profile.notifyDataSetChanged();
    }
    @Override
    public  void onDataReceived(List<questionModel> quizQuestions){

    };


}
