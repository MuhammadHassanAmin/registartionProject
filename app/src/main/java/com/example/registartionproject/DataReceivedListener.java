package com.example.registartionproject;

import java.util.List;

interface DataReceivedListener{
    void onDataReceived(List<questionModel> quizQuestions);
}