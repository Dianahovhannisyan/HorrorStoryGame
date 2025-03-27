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

public class MainActivity extends AppCompatActivity {

    private TextView storyTextView;
    private LinearLayout choicesContainer;

    private int currentSceneId = 1;  // Начинаем с первой сцены
    private JSONObject storyData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        storyTextView = findViewById(R.id.storyTextView);
        choicesContainer = findViewById(R.id.choicesContainer);

        // Загрузка данных из story.json
        String storyJson = getStoryJson();  // Для теста создадим строку JSON
        try {
            storyData = new JSONObject(storyJson);
            loadScene(currentSceneId);  // Загружаем первую сцену
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

                        choicesContainer.addView(choiceButton);  // Добавляем кнопку выбора
                    }
                }
                break;
            }
        }
    }

    private String getStoryJson() {
        return "{\n" +
                "  \"scenes\": [\n" +
                "    {\n" +
                "      \"id\": 1,\n" +
                "      \"text\": \"Ты просыпаешься на жесткой койке в заброшенной клинике и слышишь странный шум...\",\n" +
                "      \"choices\": [\n" +
                "        {\n" +
                "          \"text\": \"Осмотреть комнату\",\n" +
                "          \"nextScene\": 2\n" +
                "        },\n" +
                "        {\n" +
                "          \"text\": \"Попробовать открыть дверь и выйти\",\n" +
                "          \"nextScene\": 3\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 2,\n" +
                "      \"text\": \"Ты находишь ключ от двери и одну из записок...\",\n" +
                "      \"choices\": []\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 3,\n" +
                "      \"text\": \"Ты выходишь в коридор и видишь пациента...\",\n" +
                "      \"choices\": [\n" +
                "        {\n" +
                "          \"text\": \"Подойти и поговорить с ним\",\n" +
                "          \"nextScene\": 4\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";
    }
}
