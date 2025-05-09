package com.example.quiz.ui.main;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
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
            String json = new String(buffer, "UTF-8");

            JSONObject jsonObject = new JSONObject(json);
            JSONArray sceneArray = jsonObject.getJSONArray("scenes");

            for (int i = 0; i < sceneArray.length(); i++) {
                JSONObject sceneObj = sceneArray.getJSONObject(i);
                String id = sceneObj.getString("id");
                String title = sceneObj.optString("title", "");
                String text = sceneObj.getString("text");
                boolean isGameOver = sceneObj.optBoolean("isGameOver", false);

                List<Choice> choices = new ArrayList<>();
                JSONArray choicesArray = sceneObj.getJSONArray("choices");
                for (int j = 0; j < choicesArray.length(); j++) {
                    JSONObject choiceObj = choicesArray.getJSONObject(j);
                    String miniGame = choiceObj.optString("miniGame", null);
                    choices.add(new Choice(
                            choiceObj.getString("text"),
                            choiceObj.getString("nextSceneId"),
                            miniGame
                    ));
                }

                StoryScene scene = new StoryScene(id, title, text, isGameOver, choices);
                scenes.put(id, scene);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public StoryScene getScene(String sceneId) {
        return scenes.get(sceneId);
    }
}