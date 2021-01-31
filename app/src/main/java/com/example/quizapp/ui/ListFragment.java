package com.example.quizapp.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import com.example.quizapp.R;
import com.example.quizapp.adapters.QuizListAdapter;
import com.example.quizapp.model.QuizListModel;

import java.util.ArrayList;
import java.util.List;


public class ListFragment extends Fragment implements QuizListAdapter.OnQuizListItemClicked {
    private RecyclerView quizList;
    QuizViewModel quizViewModel;
    QuizListAdapter quizListAdapter;
    ProgressBar progressBarList;
    Animation animationIn;
    Animation animationOut;
    NavController navController;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        quizListAdapter = new QuizListAdapter(this);
        quizList = view.findViewById(R.id.recyclerView_list);
        quizList.setLayoutManager(new LinearLayoutManager(getContext()));
        quizList.setHasFixedSize(true);
        quizList.setAdapter(quizListAdapter);
        progressBarList = view.findViewById(R.id.progressList);
        animationIn = AnimationUtils.loadAnimation(getContext() , R.anim.fade_in);
        animationOut = AnimationUtils.loadAnimation(getContext() , R.anim.fade_out);
        navController = Navigation.findNavController(view);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        quizViewModel = new ViewModelProvider(getActivity()).get(QuizViewModel.class);

        quizViewModel.getMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<QuizListModel>>() {
            @Override
            public void onChanged(List<QuizListModel> quizListModelList) {
                quizList.setAnimation(animationIn);
                progressBarList.setAnimation(animationOut);
                quizListAdapter.getListFromDataBase((ArrayList<QuizListModel>)quizListModelList);
            }
        });

    }

    @Override
    public void onItemClicked(int position) {
        ListFragmentDirections.ActionListFragmentToDetailsFragment action = ListFragmentDirections.actionListFragmentToDetailsFragment().setPosition(position);
        navController.navigate(action);
    }
}