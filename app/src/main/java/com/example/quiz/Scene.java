package com.example.quiz;

import java.util.List;

public class Scene {
    private String id;
    private String title;
    private String text;
    private List<Choice> choices;

    // Геттеры
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getText() { return text; }
    public List<Choice> getChoices() { return choices; }
}
