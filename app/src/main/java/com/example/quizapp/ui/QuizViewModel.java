package com.example.quizapp.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.quizapp.model.FirebaseRepository;
import com.example.quizapp.model.OnFireStoreTaskCompleted;
import com.example.quizapp.model.QuizListModel;

import java.util.List;

public class QuizViewModel extends ViewModel implements OnFireStoreTaskCompleted {
    private MutableLiveData<List<QuizListModel>> mutableLiveData = new MutableLiveData();
    FirebaseRepository firebaseRepository = new FirebaseRepository(this);


    public MutableLiveData<List<QuizListModel>> getMutableLiveData() {
        return mutableLiveData;
    }


    public QuizViewModel(){
        firebaseRepository.getQuizData();
    }

    @Override
    public void quizListDataAdded(List<QuizListModel> quizListModelList) {
        mutableLiveData.setValue(quizListModelList);
    }

    @Override
    public void taskError(String error) {

    }
}
