package com.goprogs.riphahportalquiz;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter_usersWins extends ArrayAdapter<userWins> {

    Activity activity;
    LayoutInflater layoutInflater;
    List<userWins> dataModels;
    userWins singleUser;
    //Data To Populate



    public CustomAdapter_usersWins(Activity activity, ArrayList<userWins> dataModels) {

        super(activity, R.layout.user_rv_item_leaderboard, dataModels);
        this.activity = activity;
        this.dataModels = dataModels;
        layoutInflater = activity.getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = layoutInflater.inflate(R.layout.user_rv_item_leaderboard, parent, false);
        }
       singleUser = dataModels.get(position);
        v.setTag(singleUser.getUserID());
        TextView tvUserName= v.findViewById(R.id.tv_userName_leaderboard);
        TextView tvWinsCount = v.findViewById(R.id.tv_totalWins_leaderboard);
        tvWinsCount.setText(String.valueOf(singleUser.getWinCount()));
        tvUserName.setText(singleUser.getUserName());
        ImageView img = v.findViewById(R.id.iv_userDp_leaderboard);
        Picasso.get().load(singleUser.getDp()).resize(85, 80).into(img);

        return v;
    }
}
