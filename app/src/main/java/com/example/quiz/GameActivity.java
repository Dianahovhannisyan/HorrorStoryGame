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
import com.example.quiz.ui.main.SearchKeyActivity;
import com.example.quiz.ui.main.DoorChoiceActivity; // Добавляем импорт
import com.example.quiz.ui.main.StartWindowActivity;
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

        tvText.setAlpha(0f);
        tvText.animate().alpha(1f).setDuration(500).start();

        btnChoice1.setVisibility(View.GONE);
        btnChoice2.setVisibility(View.GONE);
        btnChoice3.setVisibility(View.GONE);

        if (currentScene.isGameOver()) {
            tvText.setClickable(true);
            tvText.setOnClickListener(v -> {
                Intent intent = new Intent(GameActivity.this, GameOverActivity.class);
                startActivity(intent);
                finish();
            });

            tvText.setAlpha(0f);
            tvText.animate()
                    .alpha(1f)
                    .setDuration(1000)
                    .withEndAction(() -> tvText.animate()
                            .alpha(0.5f)
                            .setDuration(1000)
                            .withEndAction(() -> tvText.animate()
                                    .alpha(1f)
                                    .setDuration(1000)
                                    .start())
                            .start())
                    .start();
            return;
        } else {
            tvText.setClickable(false);
            tvText.setOnClickListener(null);
        }

        List<Choice> choices = currentScene.getChoices();
        if (choices.size() > 0) setupChoiceButton(btnChoice1, choices.get(0));
        if (choices.size() > 1) setupChoiceButton(btnChoice2, choices.get(1));
        if (choices.size() > 2) setupChoiceButton(btnChoice3, choices.get(2));
    }

    private void setupChoiceButton(Button button, Choice choice) {
        button.setText(choice.getText());
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(v -> handleChoice(choice));
        animateButtonAppear(button);
    }

    private void animateButtonAppear(Button button) {
        button.setAlpha(0f);
        button.animate().alpha(1f).setDuration(500).start();
    }

    private void handleChoice(Choice choice) {
        if (choice.getMiniGame() != null) {
            Intent intent;
            if ("flashlight_key".equals(choice.getMiniGame())) {
                intent = new Intent(this, SearchKeyActivity.class);
            } else if ("door_choice".equals(choice.getMiniGame())) {
                intent = new Intent(this, DoorChoiceActivity.class);
            } else {
                showScene(choice.getNextSceneId());
                return;
            }
            intent.putExtra("nextSceneId", choice.getNextSceneId());
            startActivityForResult(intent, 1);
        } else {
            showScene(choice.getNextSceneId());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK && data != null) {
                String nextSceneId = data.getStringExtra("nextSceneId");
                showScene(nextSceneId);
            } else {
                showScene("surch_key_fail");
            }
        }
    }

    private void backToMainMenu() {
        Intent intent = new Intent(this, StartWindowActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}