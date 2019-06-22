package com.goprogs.riphahportalquiz;

import java.util.Comparator;

public class userWins  {
     int userID;
    int winCount;

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    String dp,userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getWinCount() {
        return winCount;
    }

    public void setWinCount(int winCount) {
        this.winCount = winCount;
    }


    public userWins(){};
    public userWins(int userID, int winCount) {
        this.userID = userID;
        this.winCount = winCount;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
