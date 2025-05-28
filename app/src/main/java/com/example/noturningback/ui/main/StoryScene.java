package com.example.noturningback.ui.main;

import java.util.List;

public class StoryScene {
    private String id;
    private String title;
    private String text;
    private boolean isGameOver;
    private List<Choice> choices;
    private boolean isGameWin;


    public StoryScene(String id, String title, String text, boolean isGameOver, boolean isGameWin, List<Choice> choices) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.isGameOver = isGameOver;
        this.choices = choices;
        this.isGameWin = isGameWin;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getText() { return text; }
    public boolean isGameOver() { return isGameOver; }
    public List<Choice> getChoices() { return choices; }
    public boolean isGameWin() {
        return isGameWin;
    }
    public void setGameWin(boolean gameWin) {
        isGameWin = gameWin;
    }
}