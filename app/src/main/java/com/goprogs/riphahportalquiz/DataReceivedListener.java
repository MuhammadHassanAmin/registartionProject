package com.goprogs.riphahportalquiz;

import java.util.List;

interface DataReceivedListener{
    void onDataReceived(List<questionModel> quizQuestions);



    void onDataReceived_PastMatches(List<PastMatch_RC_Model> pastMatch_rc_models_List);
}