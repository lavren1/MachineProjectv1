package com.example.mobidev.machineproject;

import java.sql.Timestamp;

/**
 * Created by Nikko on 11/12/2017.
 */

public class Achievement {
    String title;
    String description;
    com.example.mobidev.machineproject.Timestamp timestamp;
    String username;
    String achievement_id;

    public String getAchievementId() {
        return achievement_id;
    }

    public void setAchievementId(String achievement_id) {
        this.achievement_id = achievement_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Achievement(String title, String description, com.example.mobidev.machineproject.Timestamp timestamp, String username, String achievement_id) {
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
        this.username = username;
        this.achievement_id = achievement_id;
    }

    public Achievement(String title, String description, com.example.mobidev.machineproject.Timestamp timestamp, String username) {
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
        this.username = username;
    }

    public Achievement(String title, String description){
        this.title = title;
        this.description = description;
    }
    public Achievement(){}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public com.example.mobidev.machineproject.Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(com.example.mobidev.machineproject.Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}