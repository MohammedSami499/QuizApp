package com.example.quizapp.model;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class FirebaseRepository {
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference quizRef = firebaseFirestore.collection("quizList");
    private OnFireStoreTaskCompleted onFireStoreTaskCompleted;

    public FirebaseRepository(OnFireStoreTaskCompleted onFireStoreTaskCompleted) {
        this.onFireStoreTaskCompleted= onFireStoreTaskCompleted;
    }

    public void getQuizData(){
        quizRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    onFireStoreTaskCompleted.quizListDataAdded(task.getResult().toObjects(QuizListModel.class));
                }else{
                    onFireStoreTaskCompleted.taskError(task.getException().getMessage());
                }
            }
        });
    }
}
