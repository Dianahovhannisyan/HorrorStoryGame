package com.example.noturningback.ui.main;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameLogic {
    private HashMap<String, StoryScene> scenes;

    public GameLogic(Context context) {
        scenes = new HashMap<>();
        loadScenes(context);
    }

    private void loadScenes(Context context) {
        try {
            InputStream is = context.getAssets().open("story.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            JSONObject jsonObject = new JSONObject(json);
            JSONArray sceneArray = jsonObject.getJSONArray("scenes");

            for (int i = 0; i < sceneArray.length(); i++) {
                JSONObject sceneObj = sceneArray.getJSONObject(i);
                String id = sceneObj.getString("id");
                String title = sceneObj.optString("title", "");
                String text = sceneObj.getString("text");
                boolean isGameOver = sceneObj.optBoolean("isGameOver", false);
                boolean isGameWin = sceneObj.optBoolean("isGameWin", false);

                List<Choice> choices = new ArrayList<>();
                JSONArray choicesArray = sceneObj.getJSONArray("choices");
                for (int j = 0; j < choicesArray.length(); j++) {
                    JSONObject choiceObj = choicesArray.getJSONObject(j);

                    String textChoice = choiceObj.getString("text");
                    String nextSceneId = choiceObj.optString("nextSceneId", null);
                    String miniGame = choiceObj.optString("miniGame", null);

                    Choice choice = new Choice(textChoice, nextSceneId, miniGame);
                    choices.add(choice);

                    Log.d("GameLogic", "Choice loaded: \"" + textChoice + "\" → " + nextSceneId +
                            (miniGame != null ? " [miniGame: " + miniGame + "]" : ""));
                }

                StoryScene scene = new StoryScene(id, title, text, isGameOver, isGameWin, choices);
                scenes.put(id, scene);
                Log.d("GameLogic", "Scene loaded: " + id + " with " + choices.size() + " choices");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("GameLogic", "Ошибка загрузки JSON: " + e.getMessage());
        }
    }

    public StoryScene getScene(String sceneId) {
        return scenes.get(sceneId);
    }
}
