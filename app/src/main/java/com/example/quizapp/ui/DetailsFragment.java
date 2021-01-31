package com.example.quizapp.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.quizapp.R;
import com.example.quizapp.model.QuizListModel;

import java.util.List;

public class DetailsFragment extends Fragment {

    private static final String TAG = "ResultsFragment";
    ImageView details_img;
    TextView details_title, details_dif, details_desc,details_questions;
    QuizViewModel quizViewModel;
    int position;

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
        details_dif = view.findViewById(R.id.details_difficulty);
        details_questions = view.findViewById(R.id.details_questions);

        position = DetailsFragmentArgs.fromBundle(getArguments()).getPosition();
        Log.d(TAG, "sami onViewCreated: "+ position);
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
                details_questions.setText(String.valueOf(quizListModelList.get(position).getQuestions()));

                String imageUrl = quizListModelList.get(position).getImage();
                Glide
                        .with(getActivity())
                        .load(imageUrl)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder_image)
                        .into(details_img);
            }
        });

    }
}