package com.goprogs.riphahportalquiz;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import static java.lang.Integer.parseInt;

public class StartMatch extends AppCompatActivity {

    DrawerLayout mDrawerLayout;

    Intent intent1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_match);
        final SharedPreferences pref = getSharedPreferences("user_details", MODE_PRIVATE);
        Intent intent1 = getIntent();
        setIntent1(intent1);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef= db.getReference().child("matches");


        ImageView userImg = findViewById(R.id.ivUserImg_Profile);
        ImageView opponentImg = findViewById(R.id.ivOpponentImg_StartMacth);

        Picasso.get().load(pref.getString("dp","test")).resize(250, 250)
                .centerCrop().into(userImg);
        Picasso.get().load(intent1.getStringExtra("opponent_dp")).resize(250, 250)
                .centerCrop().into(opponentImg);




        TextView tvTopicHeading= (TextView) findViewById(R.id.tvYourScoreText);
        tvTopicHeading.setText(intent1.getStringExtra("quizTopic"));
        Button btnStartMatch = findViewById(R.id.btnGoToProfile_MatchFinish);
        btnStartMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MatchModel matchModel = new MatchModel();
                matchModel.competitor_ID = pref.getInt("userID",0);
                matchModel.opponent_ID = parseInt(getintent().getStringExtra("opponent_uid"));
                matchModel.topic_Name = getintent().getStringExtra("quizTopic");
                matchModel.setIfFinished(false);
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference ref = db.getReference().child("matches");
                DatabaseReference newRef = ref.push();
                String match_id = newRef.getKey();

                MatchQuestions test = new MatchQuestions();
                test.setQuestionID("empty");
                test.setCompetitor_Answer("empty");
                test.setOpponent_Answer("empty");
                matchModel.matchQuestions.put("question1",test );
                matchModel.matchQuestions.put("question2",test );
                matchModel.matchQuestions.put("question3",test );
                matchModel.matchQuestions.put("question4",test );
                matchModel.matchQuestions.put("question5",test );


                newRef.setValue(matchModel);

                Intent intent = new Intent(getApplicationContext(),Match.class);
                intent.putExtras(getintent().getExtras());
                intent.putExtra("match_id",match_id);
                intent.putExtra("opponentID",matchModel.getOpponent_ID());
                intent.putExtra("userType","competitor");

                startActivity(intent);
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
        private Intent  getintent() {
            return this.intent1;
        }
    private void setIntent1(Intent intent1) {
        this.intent1 = intent1;
    }


}
