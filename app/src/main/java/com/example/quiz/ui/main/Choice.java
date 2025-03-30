package com.example.quiz.ui.main;



public class Choice {
    private String text;
    private String nextText;
    private int points;
    private String nextScene;

    public Choice(String text, String nextText, int points, String nextScene) {
        this.text = text;
        this.nextText = nextText;
        this.points = points;
        this.nextScene = nextScene;
    }

    public String getText() {
        return text;
    }

    public String getNextText() {
        return nextText;
    }

    public int getPoints() {
        return points;
    }

    public String getNextScene() {
        return nextScene;
    }
}
