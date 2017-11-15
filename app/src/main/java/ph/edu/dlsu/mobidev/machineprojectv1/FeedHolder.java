<<<<<<< HEAD
package com.example.mobidev.machineproject;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.*;
import java.text.SimpleDateFormat;

/**
 * Created by Nikko on 11/15/2017.
 */

public class FeedHolder extends RecyclerView.ViewHolder {

    private final TextView tvFeedAchievementOwner;
    private final TextView tvFeedAchievementTitle;
    private final TextView tvFeedAchievementDesc;
    private final TextView tvFeedAchievementDateTime;

    public FeedHolder(View itemView) {
        super(itemView);

        tvFeedAchievementOwner = itemView.findViewById(R.id.tv_feedAchievement_owner);
        tvFeedAchievementTitle = itemView.findViewById(R.id.tv_feedAchievement_title);
        tvFeedAchievementDesc = itemView.findViewById(R.id.tv_feedAchievement_desc);
        tvFeedAchievementDateTime = itemView.findViewById(R.id.tv_feedAchievement_ts);
    }

    public void setOwner (String t) { tvFeedAchievementOwner.setText(t); }

    public void setTitle (String t){
        tvFeedAchievementTitle.setText(t);
    }

    public void setDesc (String t){
        tvFeedAchievementDesc.setText(t);
    }

    public void setTimestamp (java.sql.Timestamp ts){
        Date date = new Date(ts.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tvFeedAchievementDateTime.setText(sdf.format(date));
    }
=======
package ph.edu.dlsu.mobidev.machineprojectv1;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.*;
import java.text.SimpleDateFormat;

/**
 * Created by Nikko on 11/15/2017.
 */

public class FeedHolder extends RecyclerView.ViewHolder {

    private final TextView tvFeedAchievementOwner;
    private final TextView tvFeedAchievementTitle;
    private final TextView tvFeedAchievementDesc;
    private final TextView tvFeedAchievementDateTime;

    public FeedHolder(View itemView) {
        super(itemView);

        tvFeedAchievementOwner = itemView.findViewById(R.id.tv_feedAchievement_owner);
        tvFeedAchievementTitle = itemView.findViewById(R.id.tv_feedAchievement_title);
        tvFeedAchievementDesc = itemView.findViewById(R.id.tv_feedAchievement_desc);
        tvFeedAchievementDateTime = itemView.findViewById(R.id.tv_feedAchievement_ts);
    }

    public void setOwner (String t) { tvFeedAchievementOwner.setText(t); }

    public void setTitle (String t){
        tvFeedAchievementTitle.setText(t);
    }

    public void setDesc (String t){
        tvFeedAchievementDesc.setText(t);
    }

    public void setTimestamp (java.sql.Timestamp ts){
        Date date = new Date(ts.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tvFeedAchievementDateTime.setText(sdf.format(date));
    }
>>>>>>> 7c77e04fe3b28832fd0611a4f628924754453fa2
}