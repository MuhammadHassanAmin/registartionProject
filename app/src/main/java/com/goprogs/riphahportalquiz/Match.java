package com.goprogs.riphahportalquiz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.sip.SipSession;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Match extends AppCompatActivity implements DataReceivedListener {
    List<questionModel> quizQuestions = new ArrayList<questionModel>();
    TextView tvTimer;
    TextView stat;
    RadioButton opt1;
    RadioButton opt2;
    RadioButton opt3;
    Long quizTimeRemaining;
    Context context;
    RadioButton opt4;
    Button checkAnsbtn;
    CountDownTimer quizTimer;
    String match_id;
    String[] questionAnswers = new String[5];
    boolean isQuizFinished;
    RadioGroup RGQuizQuestions;
    String userType;
    int userPoints;
    boolean isComingFromPause;
    SharedPreferences quizTimePref;
    SharedPreferences pref;
    ProgressDialog progressDialog;
    int questionCount;
    FirebaseDatabase database;
    int userID;
    long quizTimeLimit;
    String quizTopic;
    Map<String, MatchQuestions> hashMatchQuestions;
    int opponentID;
    DatabaseReference matchRef;
    boolean returnResult, isAllQuestionsFromDBReceived, isCompetitorUniqueQuestionsReceived, isOpponentUniqueQuestionsReceived = false;
    int count = 1;
    List<questionModel> allQuestionsFromDB;

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }

    DataReceivedListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);


        if (isQuizFinished) {
            Intent intent = new Intent(this, ChooseQuizActivity.class);
            startActivity(intent);
        }
        pref = getSharedPreferences("user_details", MODE_PRIVATE);
        userID = pref.getInt("userID", 0);
        quizTimePref = getSharedPreferences("quizTime", MODE_PRIVATE);
        //Intent from last activity
        Intent intent = getIntent();
        match_id = intent.getStringExtra("match_id");
        opponentID = intent.getIntExtra("opponentID", 0);

        quizTopic = intent.getStringExtra("quizTopic");
        userType = intent.getStringExtra("userType");
        quizTimeLimit = 10000;// in milliseconds

        hashMatchQuestions = new HashMap<String, MatchQuestions>();
        userPoints = 0;
        //Intializing Views
        context = this;
        progressDialog = new ProgressDialog(Match.this);
        // Showing progress dialog
        progressDialog.setMessage("Loading Questions");
        progressDialog.show();
        stat = findViewById(R.id.tvStat);
        opt1 = findViewById(R.id.rbQuestion1);
        opt2 = findViewById(R.id.rbQuestion2);
        opt3 = findViewById(R.id.rbQuestion3);
        opt4 = findViewById(R.id.rbQuestion4);
        RGQuizQuestions = findViewById(R.id.RGQuizQuestions);
        opt1.setTag("A");
        opt2.setTag("B");
        opt3.setTag("C");
        opt4.setTag("D");
        tvTimer = findViewById(R.id.tvTimer);
        checkAnsbtn = (Button) findViewById(R.id.btnSubmitAns);
        //Database ref for quiz Questions
        database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference().child("quizQuestions").child(quizTopic);
        matchRef = database.getReference().child("matches").child(match_id);
        if (userType.equals("opponent")) {
            loadMatchQuestionsForOpponent();


        } else {
            loadQuestionFromDB(ref, this);
        }
    }


    private void loadMatchQuestionsForOpponent() {

        DatabaseReference databaseReference = database.getReference().child("matches").child(match_id);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                MatchModel matchModel = new MatchModel();
                matchModel = dataSnapshot.getValue(MatchModel.class);
                hashMatchQuestions = matchModel.getMatchQuestions();
                MatchQuestions q1 = new MatchQuestions();
                for (MatchQuestions questions : hashMatchQuestions.values()) {
                    q1 = questions;
                    String key = q1.getQuestionID();
                    DatabaseReference databaseReference1 = database.getReference().child("quizQuestions").child(quizTopic).child(key);
                    databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            questionModel questionModel = new questionModel();
                            questionModel = dataSnapshot.getValue(questionModel.class);
                            questionModel.setQuestionID(dataSnapshot.getKey());
                            quizQuestions.add(questionModel);
                            if (quizQuestions.size() == 5) {
                                progressDialog.dismiss();
                               Collections.reverse(quizQuestions);
                               quizQuestions=quizQuestions;
                                loadNextQuestion();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        isComingFromPause = true;
        if (!isQuizFinished) {
            SharedPreferences.Editor ed = quizTimePref.edit();
            ed.putLong("time", System.currentTimeMillis());
            ed.putLong("timeRemaining", quizTimeRemaining);
            ed.commit();
            quizTimer.cancel();

        }
    }

    @Override
    protected void onResume() {
        long start, end;
        super.onResume();
        if (isComingFromPause) {
            end = System.currentTimeMillis();

            quizTimePref = getSharedPreferences("quizTime", MODE_PRIVATE);
            start = quizTimePref.getLong("time", 0);

            long timeSpent = end - start;
            timeSpent = timeSpent / 1000;
            quizTimeRemaining = quizTimeRemaining - timeSpent;
            quizTimeLimit = quizTimeRemaining * 1000;
            //if time remaining
            if (quizTimeRemaining > 0)
                loadNextQuestion();
            else {
                quizTimer.onFinish();

            }
            isComingFromPause = false;
        }

  /*      Long tempRemainingTime;
        quizTimePref.getLong("time",-0);
        tempRemainingTime = quizTimePref.getLong("timeRemaing", -0);

        Toast.makeText(this,"Time Remaing:" +tempRemainingTime , Toast.LENGTH_SHORT ).show();
*/
    }

    private void finishQuiz() {
        isQuizFinished = true;
        if (userType.equals("competitor")) {
            matchRef.child("competitor_Points").setValue(userPoints);
            Intent finishIntent = new Intent(this, MatchFinish.class);
            finishIntent.putExtras(getIntent().getExtras());
            finishIntent.putExtra("score", userPoints);
            startActivity(finishIntent);
        } else {
            matchRef.child("ifFinished").setValue(true);
            matchRef.child("opponent_Points").setValue(userPoints);
            Intent finishIntent = new Intent(this, MatchResult.class);
            finishIntent.putExtras(getIntent().getExtras());
            finishIntent.putExtra("score", userPoints);
            startActivity(finishIntent);
        }

        //TODO
        Toast.makeText(getApplicationContext(), "Quiz End", Toast.LENGTH_SHORT).show();


    }

    private void loadNextQuestion() {
        populateUIwithQuestions(getQuestionCount());
        quizTimer = new CountDownTimer(quizTimeLimit, 1000) {
            public void onTick(long millisUntilFinished) {
                quizTimeRemaining = millisUntilFinished / 1000;
                tvTimer.setText("Seconds Remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                quizTimeLimit = 10000;
                checkAnswer(getQuestionCount());

            }
        }.start();
        checkAnsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quizTimer.cancel();
                quizTimer.onFinish();
            }
        });


    }

    void checkAnswer(int questionCount) {

        int selectedID = RGQuizQuestions.getCheckedRadioButtonId();// No Opt Selected Condition
        //setting question ID in DB Match table
        matchRef.child("matchQuestions")
                .child("question" + (questionCount + 1))
                .child("questionID")
                .setValue(quizQuestions.get(questionCount).getQuestionID());

        if (selectedID == -1) {
            MatchQuestions questions = new MatchQuestions();
            if (userType.equals("competitor"))
                questions.setCompetitor_Answer("noItemSelected");
            else
                questions.setOpponent_Answer("noItemSelected");

            Toast.makeText(getApplicationContext(), "Wrong Answer", Toast.LENGTH_SHORT).show();

            DatabaseReference newRef;
            if (userType.equals("competitor")) {
                newRef = matchRef.child("matchQuestions").child("question" + (questionCount + 1)).child("competitor_Answer");

            } else {
                newRef = matchRef.child("matchQuestions").child("question" + (questionCount + 1)).child("opponent_Answer");
            }
            newRef.setValue("noItemSelected");

            setQuestionCount(getQuestionCount() + 1);

            if (getQuestionCount() < 5)
                loadNextQuestion();
            else {
                finishQuiz();
                checkAnsbtn.setVisibility(View.GONE);
            }

        } else {
            RadioButton selected = findViewById(selectedID);
            String optSelected = selected.getTag().toString();
            String correctOpt = quizQuestions.get(questionCount).getCorrectOpt().toUpperCase();
            MatchQuestions questions = new MatchQuestions();

            // Setting user choise in db
            if (userType.equals("competitor"))
                questions.setCompetitor_Answer(optSelected);
            else
                questions.setOpponent_Answer(optSelected);

            DatabaseReference newRef;
            if (userType.equals("competitor")) {
                newRef = matchRef.child("matchQuestions").child("question" + (questionCount + 1)).child("competitor_Answer");

            } else {
                newRef = matchRef.child("matchQuestions").child("question" + (questionCount + 1)).child("opponent_Answer");

            }
            newRef.setValue(optSelected);

            if (optSelected.equals(correctOpt)) {
                userPoints++;
                Toast.makeText(getApplicationContext(), "Correct Answer", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Wrong Answer", Toast.LENGTH_SHORT).show();
            }
            setQuestionCount(getQuestionCount() + 1);

            if (getQuestionCount() < 5)
                loadNextQuestion();
            else {
                finishQuiz();
                checkAnsbtn.setVisibility(View.GONE);
            }
        }

    }

    void populateUIwithQuestions(int count) {
        RGQuizQuestions.clearCheck();
        stat.setText(quizQuestions.get(count).getStatement());
        opt1.setText(quizQuestions.get(count).getA());
        opt2.setText(quizQuestions.get(count).getB());
        opt3.setText(quizQuestions.get(count).getC());
        opt4.setText(quizQuestions.get(count).getD());
    }

    private void loadQuestionFromDB(DatabaseReference ref, final DataReceivedListener listener) {
        final List<questionModel> allQuestions = new ArrayList<>();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    questionModel question = snapshot.getValue(questionModel.class);
                    question.setQuestionID(snapshot.getKey());
                    allQuestions.add(question);
                }

                listener.onDataReceived(allQuestions);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "The read failed: " + databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onDataReceived(List<questionModel> allQuestions) {


        Toast.makeText(this, "All Questions Received From Database", Toast.LENGTH_SHORT).show();
        this.allQuestionsFromDB = allQuestions;
        findUniqueQuestionsCompetitor(allQuestionsFromDB);

    }

    public void findUniqueQuestionsCompetitor(final List<questionModel> allQuestionsFromDB) {
        //Finding uniques Quesitons of competitor
        final Query allMatches = database.getReference().child("matches").orderByChild("competitor_ID").equalTo(userID);
        allMatches.addListenerForSingleValueEvent(new ValueEventListener() {
            MatchModel matchModel = new MatchModel();

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    matchModel = snapshot.getValue(MatchModel.class);

                    int count = 1;
                    while (count < 6) {
                        String questionID;
                        questionID = matchModel.getMatchQuestions().get("question" + count).getQuestionID();
                        // Got Question ID now will match if the
                        for (int i = 0; i < allQuestionsFromDB.size(); i++) {
                            if (allQuestionsFromDB.get(i).getQuestionID().equals(questionID)) {
                                allQuestionsFromDB.remove(allQuestionsFromDB.get(i));
                                break;
                            }
                        }
                        count++;
                    }

                }
                // Here we have the list with with questions competitor has not taken
                //now we want to make sure the list doesnt not contain questions that opponent has taken
                findUniqueQuestionsOpponent();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void findUniqueQuestionsOpponent() {

        //Finding uniques Quesitons of opponent
        final Query allMatches = database.getReference().child("matches").orderByChild("opponent_ID").equalTo(opponentID);

        allMatches.addListenerForSingleValueEvent(new ValueEventListener() {
            MatchModel matchModel = new MatchModel();

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    matchModel = snapshot.getValue(MatchModel.class);
                    int count = 1;
                    while (count < 6) {
                        String questionID;
                        questionID = matchModel.getMatchQuestions().get("question" + count).getQuestionID();
                        // Got Question ID now will match if the
                        for (int i = 0; i < allQuestionsFromDB.size(); i++) {
                            if (allQuestionsFromDB.get(i).getQuestionID().equals(questionID)) {
                                allQuestionsFromDB.remove(allQuestionsFromDB.get(i));
                                break;
                            }
                        }
                        count++;
                    }

                }

                // now we have the uniques questions list e.g allquestionfromDB


                Toast.makeText(context, "Opponent Questions Filtered", Toast.LENGTH_SHORT).show();
                if (allQuestionsFromDB.size() >= 4) {
                    quizQuestions = allQuestionsFromDB;
                    progressDialog.dismiss();

                    loadNextQuestion();
                } else {
                    // dont have enough questions
                    progressDialog.dismiss();
                    Toast.makeText(context, "no new q found", Toast.LENGTH_SHORT).show();
                    //Add Old Quetions Now
                    DatabaseReference ref = database.getReference().child("quizQuestions").child(quizTopic);
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            questionModel questionModel = new questionModel();

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                                if (allQuestionsFromDB.size() == 0) {
                                    break;
                                }

                                int i = 0;

                                while (i < allQuestionsFromDB.size()) {
                                    if (!snapshot.getKey().equals(allQuestionsFromDB.get(i).getQuestionID())) {
                                        questionModel = snapshot.getValue(questionModel.class);
                                        allQuestionsFromDB.add(questionModel);
                                        break;
                                    }
                                    i++;
                                }


                                if (allQuestionsFromDB.size() == 5)
                                    break;
                            }
                            // if no unique question is found lets fill it with the old one's
                            if (allQuestionsFromDB.size() == 0) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                    questionModel = snapshot.getValue(questionModel.class);
                                    allQuestionsFromDB.add(questionModel);


                                    if (allQuestionsFromDB.size() == 5)
                                        break;
                                }
                            }
                            quizQuestions = allQuestionsFromDB;

                            loadNextQuestion();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onDataReceived_PastMatches(List<PastMatch_RC_Model> quizQuestions) {
    }

}













