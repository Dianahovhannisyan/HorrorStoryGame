package com.example.quiz;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Scene> story;
    private TextView titleTextView, storyTextView;
    private LinearLayout choicesLayout;
    private int score = 0;
    private Scene currentScene;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titleTextView = findViewById(R.id.titleTextView);
        storyTextView = findViewById(R.id.storyTextView);
        choicesLayout = findViewById(R.id.choicesLayout);

        // Загружаем историю
        story = JsonHelper.loadStory(this);
        if (story != null && !story.isEmpty()) {
            showScene(story.get(0)); // Показать первую сцену
        }
    }

    private void showScene(Scene scene) {
        currentScene = scene;
        titleTextView.setText(scene.getTitle());
        storyTextView.setText(scene.getText());
        choicesLayout.removeAllViews();

        for (Choice choice : scene.getChoices()) {
            Button button = new Button(this);
            button.setText(choice.getText());
            button.setOnClickListener(view -> makeChoice(choice));
            choicesLayout.addView(button);
        }
    }

    private void makeChoice(Choice choice) {
        score += choice.getPoints();

        // Ищем следующую сцену
        for (Scene scene : story) {
            if (scene.getId().equals(choice.getNextScene())) {
                showScene(scene);
                return;
            }
        }
    }
}
