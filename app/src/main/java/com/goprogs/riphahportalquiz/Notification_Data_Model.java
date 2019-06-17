package com.goprogs.riphahportalquiz;

public class Notification_Data_Model {
    String challengerDP;
    String Name;
    String MatchID;
    String topicName;
    Boolean isMatchFinished;


    public Boolean getMatchFinished() {
        return isMatchFinished;
    }

    public void setMatchFinished(Boolean matchFinished) {
        isMatchFinished = matchFinished;
    }


    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public  Notification_Data_Model(){};
    public String getChallengerDP() {
        return challengerDP;
    }

    public void setChallengerDP(String challengerDP) {
        this.challengerDP = challengerDP;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMatchID() {
        return MatchID;
    }

    public void setMatchID(String matchID) {
        MatchID = matchID;
    }
}
