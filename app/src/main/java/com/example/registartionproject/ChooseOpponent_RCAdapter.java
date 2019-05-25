package com.example.registartionproject;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class ChooseOpponent_RCAdapter extends RecyclerView.Adapter<ChooseOpponent_RCAdapter.ViewHolder> {


    Context context;
    List<UserModel> userModels;
    String quizTopic;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView userName;
        public ImageView userPicture;

        public ViewHolder(View itemView) {

            super(itemView);

            userPicture = (ImageView) itemView.findViewById(R.id.IV_choose_opponent_rc_item);

            userName = (TextView) itemView.findViewById(R.id.TV_choose_opponent_rc_item);
        }
    }

    public ChooseOpponent_RCAdapter(Context context,List<UserModel> TempList,String quizTopic) {
        this.context=context;
       this.userModels = TempList;
        this.quizTopic=quizTopic;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.choose_opponent_recycleview_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }
    @Override
    public void onBindViewHolder(ChooseOpponent_RCAdapter.ViewHolder holder, int position) {

        final UserModel userDetails = userModels.get(position);

        holder.userName.setText(userDetails.getName());
        Picasso.get().load(userDetails.getDp()).resize(100, 100)
                .centerCrop().into(holder.userPicture);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Intent intent = new Intent(context,StartMatch.class);
               intent.putExtra("opponent_uid",userDetails.getId());
                intent.putExtra("opponent_email",userDetails.getEmail());
                intent.putExtra("opponent_name",userDetails.getName());
                intent.putExtra("opponent_dp",userDetails.getDp());
                intent.putExtra("quizTopic", quizTopic);
                context.startActivity(intent);

            }
        });
    }


    @Override
    public int getItemCount() {

        return userModels.size();
    }



}