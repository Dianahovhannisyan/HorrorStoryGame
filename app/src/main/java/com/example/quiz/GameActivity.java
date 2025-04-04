package com.example.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quiz.ui.main.Choice;
import com.example.quiz.ui.main.GameLogic;
import com.example.quiz.ui.main.StoryScene;

import java.util.List;

public class GameActivity extends AppCompatActivity {

    private TextView tvTitle, tvText;
    private Button btnChoice1, btnChoice2, btnChoice3;

    private GameLogic gameLogic;
    private StoryScene currentScene;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        tvTitle = findViewById(R.id.tvTitle);
        tvText = findViewById(R.id.tvText);
        btnChoice1 = findViewById(R.id.btnChoice1);
        btnChoice2 = findViewById(R.id.btnChoice2);
        btnChoice3 = findViewById(R.id.btnChoice3);

        gameLogic = new GameLogic(this);
        showScene("start");  // первая сцена
    }

    private void showScene(String sceneId) {
        currentScene = gameLogic.getScene(sceneId);
        if (currentScene == null) return;

        tvTitle.setText(currentScene.getTitle());
        tvText.setText(currentScene.getText());

        List<Choice> choices = currentScene.getChoices();

        btnChoice1.setVisibility(View.GONE);
        btnChoice2.setVisibility(View.GONE);
        btnChoice3.setVisibility(View.GONE);

        if (choices.size() > 0) {
            btnChoice1.setVisibility(View.VISIBLE);
            btnChoice1.setText(choices.get(0).getText());
            btnChoice1.setOnClickListener(v -> showScene(choices.get(0).getNextSceneId()));
        }
        if (choices.size() > 1) {
            btnChoice2.setVisibility(View.VISIBLE);
            btnChoice2.setText(choices.get(1).getText());
            btnChoice2.setOnClickListener(v -> showScene(choices.get(1).getNextSceneId()));
        }
        if (choices.size() > 2) {
            btnChoice3.setVisibility(View.VISIBLE);
            btnChoice3.setText(choices.get(2).getText());
            btnChoice3.setOnClickListener(v -> showScene(choices.get(2).getNextSceneId()));
        }

        if (sceneId.equals("end_game")) {
            finish(); // или: startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
