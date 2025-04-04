package com.example.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quiz.R;
import com.example.quiz.ui.main.Choice;
import com.example.quiz.ui.main.GameLogic;
import com.example.quiz.ui.main.StoryScene;

import java.util.List;


public class GameActivity extends AppCompatActivity {
    private TextView tvTitle, tvText;
    private Button btnChoice1, btnChoice2, btnChoice3;
    private GameLogic gameLogic;

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
        updateScene();
    }

    private void updateScene() {
        StoryScene scene = gameLogic.getCurrentScene();
        if (scene == null) return;

        tvTitle.setText(scene.getTitle());
        tvText.setText(scene.getText());

        List<Choice> choices = scene.getChoices();
        if (choices.size() > 0) {
            btnChoice1.setText(choices.get(0).getText());
            btnChoice1.setVisibility(View.VISIBLE);
            btnChoice1.setOnClickListener(v -> goToScene(choices.get(0).getNextScene()));
        } else {
            btnChoice1.setVisibility(View.GONE);
        }

        if (choices.size() > 1) {
            btnChoice2.setText(choices.get(1).getText());
            btnChoice2.setVisibility(View.VISIBLE);
            btnChoice2.setOnClickListener(v -> goToScene(choices.get(1).getNextScene()));
        } else {
            btnChoice2.setVisibility(View.GONE);
        }

        if (choices.size() > 2) {
            btnChoice3.setText(choices.get(2).getText());
            btnChoice3.setVisibility(View.VISIBLE);
            btnChoice3.setOnClickListener(v -> goToScene(choices.get(2).getNextScene()));
        } else {
            btnChoice3.setVisibility(View.GONE);
        }
    }

    private void goToScene(int sceneId) {
        gameLogic.goToNextScene(sceneId);
        updateScene();
    }
}
