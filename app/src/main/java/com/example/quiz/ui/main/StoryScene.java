package com.example.quiz.ui.main;


import java.util.List;

public class StoryScene {
    private int id;
    private String title, text;
    private List<Choice> choices;

    public StoryScene(int id, String title, String text, List<Choice> choices) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.choices = choices;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public List<Choice> getChoices() {
        return choices;
    }
}

