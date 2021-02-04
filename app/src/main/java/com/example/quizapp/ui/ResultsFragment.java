package com.example.quizapp.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.quizapp.R;

import static android.content.ContentValues.TAG;


public class ResultsFragment extends Fragment implements View.OnClickListener {

    //define the variable passed
    private int correctAnswer;
    private int wrongAnswer;
    private int missedQuestion;

    // define the layout tvs
    private TextView correctResult;
    private TextView wrongResult;
    private TextView missedResult;
    private TextView resultPercentage;
    private ProgressBar resultProgressBar;
    private Button resultHomeBtn;

    NavController navController;
    float percent ;

    public ResultsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_results, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //create the hooks
        correctResult = view.findViewById(R.id.correct_answers_result);
        wrongResult = view.findViewById(R.id.wrong_answers_result);
        missedResult = view.findViewById(R.id.question_missed_result);
        resultProgressBar = view.findViewById(R.id.resultProgressBar);
        resultHomeBtn = view.findViewById(R.id.details_to_home_btn);
        resultPercentage = view.findViewById(R.id.resultPercentage);

        //get the value from the safeArgs
        correctAnswer = ResultsFragmentArgs.fromBundle(getArguments()).getCorrectAnswer();
        wrongAnswer = ResultsFragmentArgs.fromBundle(getArguments()).getWrongAnswer();
        missedQuestion = ResultsFragmentArgs.fromBundle(getArguments()).getMissedQuestion();

        navController = Navigation.findNavController(view);
        percent = correctAnswer*10;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        correctResult.setText(correctAnswer+"");
        wrongResult.setText(wrongAnswer+"");
        missedResult.setText(missedQuestion+"");
        resultProgressBar.setVisibility(View.INVISIBLE);

        Log.d(TAG, "onActivityCreated: " + percent);
        resultPercentage.setText(percent + "%");

        resultHomeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        navController.navigate(R.id.action_resultFragment_to_listFragment);
    }
}