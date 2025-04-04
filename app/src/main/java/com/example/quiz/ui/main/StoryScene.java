package com.example.quiz.ui.main;


import java.util.List;

public class StoryScene {
    private String id;
    private String title;
    private String text;
    private List<Choice> choices;

    public StoryScene(String id, String title, String text, List<Choice> choices) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.choices = choices;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getText() { return text; }
    public List<Choice> getChoices() { return choices; }
}
