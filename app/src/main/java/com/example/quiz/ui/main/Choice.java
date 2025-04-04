package com.example.quiz.ui.main;



public class Choice {
    private String text;
    private int nextScene;

    public Choice(String text, int nextScene) { // Изменили String на int
        this.text = text;
        this.nextScene = nextScene;
    }

    public String getText() {
        return text;
    }

    public int getNextScene() { // Теперь возвращает int
        return nextScene;
    }
}
