package com.goprogs.riphahportalquiz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import static java.lang.Integer.parseInt;

    public class pastMatches_RCAdapter_Profile extends RecyclerView.Adapter<pastMatches_RCAdapter_Profile.ViewHolder> {


        Context context;
        List<PastMatch_RC_Model> pastMatches;



        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvOppnentName ,quizTopic, quizResult;
            ImageView ivOppnentDp;
            public ViewHolder(View itemView) {

                super(itemView);
                ivOppnentDp = itemView.findViewById(R.id.IV_choose_opponent_rc_item);
                tvOppnentName = itemView.findViewById(R.id.TV_choose_opponent_rc_item);
                quizTopic= itemView.findViewById(R.id.tvQuizTopic_RC_PostMatches);
                quizResult= itemView.findViewById(R.id.tvResultText_RC_PostMatches);
            }
        }

        public pastMatches_RCAdapter_Profile(Context context,List<PastMatch_RC_Model> TempList) {
            this.context=context;
            this.pastMatches = TempList;
        }

        @Override
        public pastMatches_RCAdapter_Profile.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.past_match_rc_view_item_profile, parent, false);
            pastMatches_RCAdapter_Profile.ViewHolder viewHolder = new pastMatches_RCAdapter_Profile.ViewHolder(view);

            return viewHolder;
        }
        @Override
        public void onBindViewHolder(pastMatches_RCAdapter_Profile.ViewHolder holder, int position) {

            final PastMatch_RC_Model pastMatch = pastMatches.get(position);

            holder.tvOppnentName.setText(pastMatch.getOpponentName());
            holder.quizResult.setText(pastMatch.getResult());
            holder.quizTopic.setText(pastMatch.getQuizTopic());
            Picasso.get().load(pastMatch.getOpponentDp()).resize(43, 42)
                    .centerCrop().into(holder.ivOppnentDp);


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO
/*


                    Intent intent = new Intent(context,StartMatch.class);
                    intent.putExtra("opponent_uid",userDetails.getId());
                    intent.putExtra("opponent_email",userDetails.getEmail());
                    intent.putExtra("opponent_name",userDetails.getName());
                    intent.putExtra("opponent_dp",userDetails.getDp());
                    intent.putExtra("quizTopic", quizTopic);
                    context.startActivity(intent);
*/


                }
            });
        }


        @Override
        public int getItemCount() {

            return pastMatches.size();
        }



    }

