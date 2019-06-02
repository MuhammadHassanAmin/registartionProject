package com.goprogs.riphahportalquiz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChooseOpponent extends AppCompatActivity {
    DatabaseReference databaseReference;

    ProgressDialog progressDialog;

    List<UserModel> list = new ArrayList<>();

    RecyclerView recyclerView;

    RecyclerView.Adapter adapter ;

    private String quizTopic;



    // Storing server url into String variable.
    String HttpUrl = "https://riphahportal.com/API/user_login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_opponent);
        Intent intent = getIntent();
        quizTopic = intent.getStringExtra("quizTopic");

        recyclerView = (RecyclerView) findViewById(R.id.RVChooseOpponent);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        progressDialog = new ProgressDialog(ChooseOpponent.this);

        fetchUserData();



    }
    public  void  setRCadpter(List list){
        adapter = new ChooseOpponent_RCAdapter(this,list,quizTopic);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
    protected void fetchUserData(){




            // Showing progress dialog
            progressDialog.setMessage("Please Wait");
            progressDialog.show();



        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(ChooseOpponent.this);
        String url = "https://riphahportal.com/API/user_api_handler.php?action=fetch_all";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                        try {

                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++)
                            {

                                JSONObject jsonObj = jsonArray.getJSONObject(i);
                                    String id = jsonObj.getString("id").toString();
                                String name = jsonObj.getString("name").toString();
                                String email= jsonObj.getString("email").toString();


                                String picture = jsonObj.getString("picture").toString();

                                if (picture.equals("nopic"))
                                    picture="https://riphahportal.com/images/default-profile.jpg";
                                else
                                    picture="https://riphahportal.com/storage/profiles/"+picture;


                                UserModel temp = new UserModel(id,name,email,picture);

                                list.add(temp);
                            }
                            progressDialog.dismiss();
                            setRCadpter(list);



                        } catch (Throwable t) {
                            Log.e("My App", "Could not parse malformed JSON: \"" + response + "\"");
                        }
                    }
                    }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ChooseOpponent.this,"Error finding user data",Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);



    }
    public void setList(List list){
        this.list=list;
    }


}
