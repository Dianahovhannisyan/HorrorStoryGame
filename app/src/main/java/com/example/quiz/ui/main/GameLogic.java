package com.example.quiz.ui.main;


import android.content.Context;
import android.transition.Scene;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameLogic {
    private Map<String, Scene> sceneMap;
    private String currentSceneId;
    private int score;

    public GameManager(Context context) {
        score = 0;
        loadScenesFromJSON(context);
        currentSceneId = "1"; // Стартовая сцена
    }


    public Scene getCurrentScene() {
        return sceneMap.get(currentSceneId);
    }


    public String processChoice(int choiceIndex) {
        Scene currentScene = sceneMap.get(currentSceneId);
        if (currentScene == null || choiceIndex >= currentScene.getChoices().size()) {
            return "";
        }
        Choice choice = currentScene.getChoices().get(choiceIndex);
        score += choice.getPoints();
        String resultText = choice.getNextText();
        currentSceneId = choice.getNextScene();
        return resultText;
    }


    public int getScore() {
        return score;
    }


    public boolean hasNextScene() {
        return sceneMap.containsKey(currentSceneId);
    }


    private void loadScenesFromJSON(Context context) {
        sceneMap = new HashMap<>();
        try {
            InputStream is = context.getAssets().open("story.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String jsonStr = new String(buffer, "UTF-8");

            JSONObject jsonObj = new JSONObject(jsonStr);
            JSONArray scenesArray = jsonObj.getJSONArray("scenes");

            for (int i = 0; i < scenesArray.length(); i++) {
                JSONObject sceneObj = scenesArray.getJSONObject(i);
                String id = sceneObj.getString("id");
                String title = sceneObj.getString("title");
                String text = sceneObj.getString("text");
                List<Choice> choices = new ArrayList<>();

                JSONArray choicesArray = sceneObj.getJSONArray("choices");
                for (int j = 0; j < choicesArray.length(); j++) {
                    JSONObject choiceObj = choicesArray.getJSONObject(j);
                    String choiceText = choiceObj.getString("text");
                    String nextText = choiceObj.getString("nextText");
                    int points = choiceObj.getInt("points");
                    String nextScene = choiceObj.get("nextScene").toString();
                    choices.add(new Choice(choiceText, nextText, points, nextScene));
                }

                Scene scene = new Scene(id, title, text, choices);
                sceneMap.put(id, scene);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}

