package com.goprogs.riphahportalquiz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class Notifications_RV_Adapter  extends RecyclerView.Adapter<Notifications_RV_Adapter.ViewHolder> {


    Context context;

    List<Notification_Data_Model> notificaitonsList;
    


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView challengerName,quizTopic;
        public ImageView challengerDp;

        public ViewHolder(View itemView) {

            super(itemView);

            challengerDp = (ImageView) itemView.findViewById(R.id.iv_ChallengerDP_Notification);

            challengerName = (TextView) itemView.findViewById(R.id.tv_Challenger_Name_Notificaitons);
            quizTopic = (TextView) itemView.findViewById(R.id.tv_Quiz_Title_Notificaitons);

        }
    }

    public Notifications_RV_Adapter(Context context,List<Notification_Data_Model> TempList) {
        this.context=context;
        this.notificaitonsList = TempList;
  
    }


    @Override
    public Notifications_RV_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_recycleview_item, parent, false);
        Notifications_RV_Adapter.ViewHolder viewHolder = new Notifications_RV_Adapter.ViewHolder(view);

        return viewHolder;
    }
    @Override
    public void onBindViewHolder(final Notifications_RV_Adapter.ViewHolder holder, final int position) {

        final Notification_Data_Model notification_data_model= notificaitonsList.get(position);

        holder.challengerName.setText(notification_data_model.getName());
        Picasso.get().load(notification_data_model.getChallengerDP()).resize(100, 100)
                .centerCrop().into(holder.challengerDp);
        holder.quizTopic.setText(notification_data_model.getTopicName());

            holder.itemView.setTag(notification_data_model.getMatchID());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (notificaitonsList.get(position).getMatchFinished()){

                    intent = new Intent(context, MatchResult.class);

                }else{
                    intent = new Intent(context,Match.class);
                    intent.putExtra("userType","opponent");
                }
                String id = holder.itemView.getTag().toString();

                intent.putExtra("match_id",holder.itemView.getTag().toString());
                intent.putExtra("quizTopic",notification_data_model.getTopicName());

                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {

        return notificaitonsList.size();
    }


}
