package com.example.quiz.ui.main;



public class Choice {
    private String text;
    private String nextSceneId;

    public Choice(String text, String nextSceneId) {
        this.text = text;
        this.nextSceneId = nextSceneId;
    }

    public String getText() { return text; }
    public String getNextSceneId() { return nextSceneId; }
}
