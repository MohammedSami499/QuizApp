
package com.example.quizapp.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.quizapp.R;
import com.example.quizapp.model.QuestionsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

    public class QuizFragment extends Fragment implements View.OnClickListener {

        private static final String TAG = "QuizFragment";

        //Firebase hooks
        private FirebaseFirestore firebaseFirestore;
        private String document_Id;

        //firebase Data
        private List<QuestionsModel> allQuestionsList = new ArrayList<>();
        private List<QuestionsModel> aQuestionToAnswer = new ArrayList<>();
        private int allQuestionsNumber;

        //UI hooks
        private TextView quizPageTitle;
        private TextView quizQuestionCounter;
        private TextView quizQuestionsTimeToAnswer;
        private TextView quizQuestionsTV;
        private TextView quizFetchingDataTV;
        private TextView quizVerifiedAnswerTV;
        private ProgressBar quizProgressBar;
        private ImageView quizCloseTabImg;
        private Button option_1_BTN;
        private Button option_2_BTN;
        private Button option_3_BTN;
        private Button nextQuestionBTN;
        private CountDownTimer countDownTimer;
        private boolean enableToAnswer = false;
        private int questionNum =0;
        private int correctAnswer = 0;
        private int wrongAnswer = 0;
        private int currentQuestion =0;
        private int totalAnsweredQuestions = 0;
        private int missedQuestions= 0;

        private NavController navController;
        private int questionCounter = 1;

        public QuizFragment() {
            // Required empty public constructor
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //hooks identifier
        quizPageTitle = view.findViewById(R.id.quiz_title);
        quizQuestionsTV =view.findViewById(R.id.quiz_questions);
        quizQuestionCounter = view.findViewById(R.id.question_counter);
        quizFetchingDataTV = view.findViewById(R.id.fetchingData_title);
        quizVerifiedAnswerTV = view.findViewById(R.id.verifiedAnswer);
        quizCloseTabImg = view.findViewById(R.id.close_tab_img);
        quizProgressBar = view.findViewById(R.id.quiz_progressBar);
        option_1_BTN = view.findViewById(R.id.first_option);
        option_2_BTN = view.findViewById(R.id.second_option);
        option_3_BTN = view.findViewById(R.id.third_option);
        nextQuestionBTN = view.findViewById(R.id.nextQuestion_btn);
        quizQuestionsTimeToAnswer = view.findViewById(R.id.question_time_to_answer);
        //getting the navController
        navController = Navigation.findNavController(view);

        //Getting the passed args
        document_Id = QuizFragmentArgs.fromBundle(getArguments()).getQuizId();
        allQuestionsNumber = QuizFragmentArgs.fromBundle(getArguments()).getAllQuestionsNumber();

        //firebase
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("quizList").document(document_Id)
                .collection("Questions").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            allQuestionsList = task.getResult().toObjects(QuestionsModel.class);
                            quizPageTitle.setText("Welcome...");
                            pickQuestion();
                            LoadUI();
                        } else {
                            quizPageTitle.setText("Error happened...");

                        }
                    }
                });

        //create onClick listener 
        option_1_BTN.setOnClickListener(this);
        option_2_BTN.setOnClickListener(this);
        option_3_BTN.setOnClickListener(this);

        nextQuestionBTN.setOnClickListener(this);


    }

        private void LoadUI() {

            //Make the option buttons enable
            EnableOptionBTNs();

            //Make the basic TVs visible
            quizQuestionsTV.setVisibility(View.VISIBLE);
            quizQuestionCounter.setVisibility(View.VISIBLE);
            quizQuestionCounter.setText("1");

            //Load a question to the option button
            LoadQuestion(currentQuestion);
        }



        private void startTimer(int questionNumber) {
            //enable progressBar
            quizProgressBar.setVisibility(View.VISIBLE);
            
            //enable to answer
            enableToAnswer = true;
            
            Long timer = aQuestionToAnswer.get(questionNumber).getTimer();
            quizQuestionsTimeToAnswer.setText(timer.toString());

           countDownTimer =  new CountDownTimer(timer * 1000, 10) {
                @Override
                public void onTick(long millisUntilFinished) {
                    quizQuestionsTimeToAnswer.setText(millisUntilFinished/1000 + "");
                    Long quizProgress = (millisUntilFinished/10)/timer ;
                    quizProgressBar.setProgress(quizProgress.intValue());
                }
                @Override
                public void onFinish() {
                    missedQuestions ++;
                    enableToAnswer = false;
                    totalAnsweredQuestions ++;
                    answerResult("Not Answered ! " + "\n the correct is: " + aQuestionToAnswer.get(questionNum).getAnswer());
                    quizVerifiedAnswerTV.setTextColor(getResources().getColor(R.color.colorAccent , null));
                }
            };
           countDownTimer.start();
        }
        
        private void EnableOptionBTNs() {
            //Show all Option buttons
            option_1_BTN.setVisibility(View.VISIBLE);
            option_2_BTN.setVisibility(View.VISIBLE);
            option_3_BTN.setVisibility(View.VISIBLE);

            //Enable all option buttons
            option_1_BTN.setEnabled(true);
            option_2_BTN.setEnabled(true);
            option_3_BTN.setEnabled(true);

            //Enable the fetching data tv
            quizFetchingDataTV.setVisibility(View.VISIBLE);

            //Hide the next button and feedback
            nextQuestionBTN.setVisibility(View.INVISIBLE);
            quizVerifiedAnswerTV.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

        }

        private void pickQuestion(){
        for(int i=0 ; i<allQuestionsNumber; i++){
            int random = getRandomInteger(allQuestionsList.size() ,0);
            aQuestionToAnswer.add(allQuestionsList.get(random));
            allQuestionsList.remove(random);

        }
    }

    private int getRandomInteger(int max , int min){
        return ((int)(Math.random()*(max - min) + min));
    }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.first_option:
                    checkTheAnswer(option_1_BTN.getText() , v);
                    break;
                case R.id.second_option:
                    checkTheAnswer(option_2_BTN.getText(), v);
                    break;
                case R.id.third_option:
                    checkTheAnswer(option_3_BTN.getText(), v);
                    break;
                case R.id.nextQuestion_btn:
                        quizQuestionCounter.setText(++questionCounter +"");
                    if(totalAnsweredQuestions<10){
                        //get a new question
                        loadNewQuestion(++currentQuestion);
                        break;
                    }else{
                        QuizFragmentDirections.ActionQuizFragmentToResultFragment action = QuizFragmentDirections.actionQuizFragmentToResultFragment();
                        action.setCorrectAnswer(correctAnswer);
                        action.setMissedQuestion(missedQuestions);
                        action.setWrongAnswer(wrongAnswer);
                        navController.navigate(action);
                    }

            }
        }


        private void loadNewQuestion(int i) {
            LoadQuestion(i);
            //Clear every thing to start again
            resetAllUI();
        }
        private void LoadQuestion(int questionNumber) {

            //UnEnabling the title and cancel img
            quizPageTitle.setVisibility(View.INVISIBLE);
            quizCloseTabImg.setVisibility(View.INVISIBLE);

            //Load options
            option_1_BTN.setText(aQuestionToAnswer.get(questionNumber).getOption_a());
            option_2_BTN.setText(aQuestionToAnswer.get(questionNumber ).getOption_b());
            option_3_BTN.setText(aQuestionToAnswer.get(questionNumber ).getOption_c());

            //Load question text
            quizFetchingDataTV.setText(aQuestionToAnswer.get(questionNumber).getQuestion());

            //start question timer
            startTimer(questionNumber);
            questionNum = questionNumber ;

        }

        private void resetAllUI() {
            countDownTimer.start();
            nextQuestionBTN.setVisibility(View.INVISIBLE);
            nextQuestionBTN.setEnabled(false);
            quizVerifiedAnswerTV.setVisibility(View.INVISIBLE);
            option_1_BTN.setBackground(ContextCompat.getDrawable(getContext() ,R.drawable.outline_light_bg_btn));
            option_2_BTN.setBackground(ContextCompat.getDrawable(getContext() ,R.drawable.outline_light_bg_btn));
            option_3_BTN.setBackground(ContextCompat.getDrawable(getContext() ,R.drawable.outline_light_bg_btn));

        }

        private void checkTheAnswer(CharSequence btnText, View v) {
            if(enableToAnswer){
                if(aQuestionToAnswer.get(questionNum).getAnswer().equals(btnText)){

                    //Action after answering
                    answerResult("Correct Answer");
                    v.setBackground(ContextCompat.getDrawable(getContext() ,R.drawable.outline_correct_answer_bg_btn));
                    quizVerifiedAnswerTV.setTextColor(getResources().getColor(R.color.colorPrimary , null));

                    correctAnswer ++;
                }else{

                    //action after answer
                    answerResult("Answer wrong ! " + "\n the correct is: " + aQuestionToAnswer.get(questionNum).getAnswer());
                    v.setBackground(ContextCompat.getDrawable(getContext() ,R.drawable.outline_wronge_answer_bg_btn));
                    quizVerifiedAnswerTV.setTextColor(getResources().getColor(R.color.colorAccent , null));

                    wrongAnswer ++;
                }
                totalAnsweredQuestions ++;
                countDownTimer.cancel();
                enableToAnswer = false;

            }
        }


        private void answerResult(String answer_status) {
            nextQuestionBTN.setVisibility(View.VISIBLE);
            nextQuestionBTN.setEnabled(true);
            quizVerifiedAnswerTV.setVisibility(View.VISIBLE);
            quizVerifiedAnswerTV.setText(answer_status);
        }
    }