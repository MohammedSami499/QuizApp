package com.example.quizapp.model;

import com.google.firebase.firestore.DocumentId;

public class QuizListModel {

    @DocumentId
    private String quiz_id;
    private int questions;
    private String name , image, desc, level, visibility;

    public QuizListModel() {
    }

    public QuizListModel(String quiz_id, int questions, String name, String image, String desc, String level, String visibility) {
        this.quiz_id = quiz_id;
        this.questions = questions;
        this.name = name;
        this.image = image;
        this.desc = desc;
        this.level = level;
        this.visibility = visibility;
    }

    public String getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(String quiz_id) {
        this.quiz_id = quiz_id;
    }

    public long getQuestions() {
        return questions;
    }

    public void setQuestions(int questions) {
        this.questions = questions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }
}
