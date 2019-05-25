package com.example.registartionproject;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter_ChooseQuizTopic extends ArrayAdapter<quizTopicModel> {

    Activity activity;
    LayoutInflater layoutInflater;
    ArrayList<quizTopicModel>  dataModels;
    quizTopicModel singleQuiz;
    //Data To Populate



    public CustomAdapter_ChooseQuizTopic(Activity activity, ArrayList<quizTopicModel> dataModels) {

        super(activity, R.layout.choose_topic_list_view_item, dataModels);
        this.activity = activity;
        this.dataModels = dataModels;
        layoutInflater = activity.getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = layoutInflater.inflate(R.layout.choose_topic_list_view_item, parent, false);
        }
        singleQuiz = dataModels.get(position);
        v.setTag(singleQuiz.getName());
        TextView tv = v.findViewById(R.id.tv_choose_topic_item);
        tv.setText(singleQuiz.getName());
        return v;
    }
};

