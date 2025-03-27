package com.example.quiz;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private TextView storyTextView;
    private LinearLayout choicesContainer;

    private int currentSceneId = 1;
    private JSONObject storyData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        storyTextView = findViewById(R.id.storyTextView);
        choicesContainer = findViewById(R.id.choicesContainer);


        try {
            storyData = new JSONObject(loadJSONFromAsset());
            loadScene(currentSceneId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("story.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    private void loadScene(int sceneId) throws JSONException {
        JSONArray scenes = storyData.getJSONArray("scenes");

        for (int i = 0; i < scenes.length(); i++) {
            JSONObject scene = scenes.getJSONObject(i);

            if (scene.getInt("id") == sceneId) {
                // Отображаем текст сцены
                String sceneText = scene.getString("text");
                storyTextView.setText(sceneText);

                // Отображаем кнопки для выбора
                choicesContainer.removeAllViews();  // Очищаем предыдущие кнопки
                if (scene.has("choices")) {
                    JSONArray choices = scene.getJSONArray("choices");
                    for (int j = 0; j < choices.length(); j++) {
                        JSONObject choice = choices.getJSONObject(j);
                        String choiceText = choice.getString("text");

                        Button choiceButton = new Button(this);
                        choiceButton.setText(choiceText);
                        choiceButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    // Переход к следующей сцене
                                    int nextSceneId = choice.getInt("nextScene");
                                    loadScene(nextSceneId);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        choicesContainer.addView(choiceButton);
                    }
                }
                break;
            }
        }
    }
}
