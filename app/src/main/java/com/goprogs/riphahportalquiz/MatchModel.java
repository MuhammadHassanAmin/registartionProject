package com.goprogs.riphahportalquiz;

import java.util.HashMap;
import java.util.Map;

public class MatchModel {
    Map<String, MatchQuestions> matchQuestions= new HashMap<String, MatchQuestions>();


    private MatchQuestions matchQuestion;

    public String getMatch_id() {
        return match_id;
    }

    public void setMatch_id(String match_id) {
        this.match_id = match_id;
    }

    public String getTopic_Name() {
        return topic_Name;
    }

    public void setTopic_Name(String topic_Name) {
        this.topic_Name = topic_Name;
    }

    public int getCompetitor_ID() {
        return competitor_ID;
    }

    public void setCompetitor_ID(int competitor_ID) {
        this.competitor_ID = competitor_ID;
    }

    public int getCompetitor_Points() {
        return competitor_Points;
    }

    public void setCompetitor_Points(int competitor_Points) {
        this.competitor_Points = competitor_Points;
    }

    public int getOpponent_ID() {
        return opponent_ID;
    }

    public void setOpponent_ID(int opponent_ID) {
        this.opponent_ID = opponent_ID;
    }

    public int getOpponent_Points() {
        return opponent_Points;
    }

    public void setOpponent_Points(int opponent_Points) {
        this.opponent_Points = opponent_Points;
    }

    String match_id,topic_Name;
    int   competitor_ID,competitor_Points,opponent_ID,opponent_Points;


}