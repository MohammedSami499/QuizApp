package com.example.quizapp.model;

import java.util.List;

public interface OnFireStoreTaskCompleted {
    void quizListDataAdded(List<QuizListModel> quizListModelList);
    void taskError(String error);
}
