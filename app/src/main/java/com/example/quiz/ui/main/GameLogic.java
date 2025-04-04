package com.example.quiz.ui.main;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.content.res.AssetManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GameLogic {
    private List<StoryScene> scenes;
    private int currentSceneId;

    public GameLogic(Context context) {
        scenes = new ArrayList<>();
        loadScenesFromJson(context);
        currentSceneId = 1; // Начинаем с первой сцены
    }

    private void loadScenesFromJson(Context context) {
        String jsonString = loadJSONFromAsset(context, "story.json");
        if (jsonString == null) return;
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray scenesArray = jsonObject.getJSONArray("scenes");
            for (int i = 0; i < scenesArray.length(); i++) {
                JSONObject sceneObj = scenesArray.getJSONObject(i);
                int id = sceneObj.getInt("id");
                String title = sceneObj.has("title") ? sceneObj.getString("title") : "";
                String text = sceneObj.getString("text");

                List<Choice> choices = new ArrayList<>();
                if (sceneObj.has("choices")) {
                    JSONArray choicesArray = sceneObj.getJSONArray("choices");
                    for (int j = 0; j < choicesArray.length(); j++) {
                        JSONObject choiceObj = choicesArray.getJSONObject(j);
                        String choiceText = choiceObj.getString("text");
                        int nextScene = choiceObj.getInt("nextScene");
                        choices.add(new Choice(choiceText, nextScene));
                    }
                }
                scenes.add(new StoryScene(id, title, text, choices));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String loadJSONFromAsset(Context context, String filename) {
        String json = null;
        try {
            AssetManager assetManager = context.getAssets();
            InputStream is = assetManager.open(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            json = sb.toString();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return json;
    }

    public StoryScene getCurrentScene() {
        for (StoryScene scene : scenes) {
            if (scene.getId() == currentSceneId) {
                return scene;
            }
        }
        return null;
    }

    public void goToNextScene(int sceneId) {
        currentSceneId = sceneId;
    }
}
