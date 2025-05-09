package com.example.quiz.ui.main;

public class Choice {
    private String text;
    private String nextSceneId;
    private String miniGame;

    public Choice(String text, String nextSceneId, String miniGame) {
        this.text = text;
        this.nextSceneId = nextSceneId;
        this.miniGame = miniGame;
    }

    public String getText() { return text; }
    public String getNextSceneId() { return nextSceneId; }
    public String getMiniGame() { return miniGame; }
}