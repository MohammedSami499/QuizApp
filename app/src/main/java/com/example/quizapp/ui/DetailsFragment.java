package com.example.quizapp.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.quizapp.R;
import com.example.quizapp.model.QuizListModel;

import java.util.List;

public class DetailsFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "DetailsFragment";
    private ImageView details_img;
    private TextView details_title, details_dif, details_desc, details_total_questions , last_Score;
    private QuizViewModel quizViewModel;
    private int position;
    private String quiz_Id;
    private int allQuestionsNumber;
    private Button detailsButton;
    private NavController navController;

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        details_img = view.findViewById(R.id.details_img);
        details_title = view.findViewById(R.id.details_title);
        details_desc = view.findViewById(R.id.details_desc);
        details_dif = view.findViewById(R.id.details_defficulty_txt);
        last_Score =view.findViewById(R.id.details_scores_txt);
        details_total_questions = view.findViewById(R.id.details_questions_txt);
        detailsButton = view.findViewById(R.id.details_start_btn);
        navController = Navigation.findNavController(view);
        position = DetailsFragmentArgs.fromBundle(getArguments()).getPosition();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        quizViewModel = new ViewModelProvider(getActivity()).get(QuizViewModel.class);

        quizViewModel.getMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<QuizListModel>>() {
            @Override
            public void onChanged(List<QuizListModel> quizListModelList) {


                details_title.setText(quizListModelList.get(position).getName());
                details_desc.setText(quizListModelList.get(position).getDesc());
                details_dif.setText(quizListModelList.get(position).getLevel());
                details_total_questions.setText(String.valueOf(quizListModelList.get(position).getQuestions()));
                String imageUrl = quizListModelList.get(position).getImage();
                Glide
                        .with(getActivity())
                        .load(imageUrl)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder_image)
                        .into(details_img);
                quiz_Id= quizListModelList.get(position).getQuiz_id();
                allQuestionsNumber = (int)quizListModelList.get(position).getQuestions();
            }

        });
        detailsButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
            DetailsFragmentDirections.ActionDetailsFragmentToQuizFragment action = DetailsFragmentDirections.actionDetailsFragmentToQuizFragment();
            action.setAllQuestionsNumber(allQuestionsNumber);
            action.setQuizId(quiz_Id);
            navController.navigate(action);

    }
}