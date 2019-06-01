package com.goprogs.riphahportalquiz;

import java.util.List;

interface DataReceivedListener{
    void onDataReceived(List<questionModel> quizQuestions);
}