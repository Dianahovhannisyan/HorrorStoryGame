package com.example.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quiz.ui.main.Choice;
import com.example.quiz.ui.main.GameLogic;
import com.example.quiz.ui.main.GameOverActivity;
import com.example.quiz.ui.main.StartWindowActivity;
import com.example.quiz.ui.main.SearchKeyActivity;
import com.example.quiz.ui.main.StoryScene;

import java.util.List;

public class GameActivity extends AppCompatActivity {

    private TextView tvTitle, tvText;
    private Button btnChoice1, btnChoice2, btnChoice3;
    private GameLogic gameLogic;
    private StoryScene currentScene;
    private ImageView playBackIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        tvTitle = findViewById(R.id.tvTitle);
        tvText = findViewById(R.id.tvText);
        btnChoice1 = findViewById(R.id.btnChoice1);
        btnChoice2 = findViewById(R.id.btnChoice2);
        btnChoice3 = findViewById(R.id.btnChoice3);
        playBackIcon = findViewById(R.id.play_back_icon);

        gameLogic = new GameLogic(this);
        showScene("start");

        playBackIcon.setOnClickListener(v -> backToMainMenu());
    }

    private void showScene(String sceneId) {
        currentScene = gameLogic.getScene(sceneId);
        if (currentScene == null) return;

        tvTitle.setText(currentScene.getTitle());
        tvText.setText(currentScene.getText());

        List<Choice> choices = currentScene.getChoices();
        setupChoiceButtons(choices);

        if (sceneId.equals("end_game")) {
            goToGameOver();
        }
    }

    private void setupChoiceButtons(List<Choice> choices) {
        btnChoice1.setVisibility(View.GONE);
        btnChoice2.setVisibility(View.GONE);
        btnChoice3.setVisibility(View.GONE);

        Button[] buttons = {btnChoice1, btnChoice2, btnChoice3};

        for (int i = 0; i < choices.size() && i < buttons.length; i++) {
            Choice choice = choices.get(i);
            Button button = buttons[i];
            button.setVisibility(View.VISIBLE);
            button.setText(choice.getText());
            button.setOnClickListener(v -> handleChoice(choice));
        }
    }

    private void handleChoice(Choice choice) {
        if (choice.getMiniGame() != null) {
            startMiniGame(choice);
        } else {
            showScene(choice.getNextSceneId());
        }
    }

    private void startMiniGame(Choice choice) {
        Intent intent;
        switch (choice.getMiniGame()) {
            case "flashlight_key":
                intent = new Intent(this, SearchKeyActivity.class);
                break;
            default:
                // Добавь другие мини-игры здесь
                return;
        }

        intent.putExtra("nextSceneId", choice.getNextSceneId());
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String nextSceneId = data.getStringExtra("nextSceneId");
            showScene(nextSceneId);
        }
    }

    private void goToGameOver() {
        Intent intent = new Intent(this, GameOverActivity.class);
        startActivity(intent);
        finish();
    }

    private void backToMainMenu() {
        Intent intent = new Intent(this, StartWindowActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
