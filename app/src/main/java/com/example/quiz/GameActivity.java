package com.example.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quiz.ui.main.Choice;
import com.example.quiz.ui.main.GameLogic;
import com.example.quiz.ui.main.GameOverActivity;
import com.example.quiz.ui.main.PicturesSeeActivity;
import com.example.quiz.ui.main.DoorChoiceActivity;
import com.example.quiz.ui.main.StartWindowActivity;
import com.example.quiz.ui.main.StoryScene;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

        // Инициализация UI
        tvTitle = findViewById(R.id.tvTitle);
        tvText = findViewById(R.id.tvText);
        btnChoice1 = findViewById(R.id.btnChoice1);
        btnChoice2 = findViewById(R.id.btnChoice2);
        btnChoice3 = findViewById(R.id.btnChoice3);
        playBackIcon = findViewById(R.id.play_back_icon);

        // Загрузка логики и начальной сцены
        gameLogic = new GameLogic(this);
        String sceneId = getIntent().getStringExtra("startSceneId");
        if (sceneId == null) sceneId = "start";
        showScene(sceneId);

        playBackIcon.setOnClickListener(v -> backToMainMenu());
    }

    private void showScene(String sceneId) {
        currentScene = gameLogic.getScene(sceneId);

        if (currentScene == null) {
            Log.e("GameActivity", "Scene not found: " + sceneId);
            tvTitle.setText("Ошибка");
            tvText.setText("Сцена не найдена: " + sceneId);
            return;
        }

        // Сохраняем текущую сцену в Firebase
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users")
                    .child(user.getUid())
                    .child("currentSceneId");
            ref.setValue(currentScene.getId());
        }

        tvTitle.setText(currentScene.getTitle());
        tvText.setText(currentScene.getText());

        if ("true_continue".equals(currentScene.getId())) {
            Intent intent = new Intent(GameActivity.this, FirsEndActivity.class);
            intent.putExtra("sceneText", currentScene.getText());
            startActivity(intent);
            finish();
            return;
        }

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
            animateTextPulse();
            return;
        } else if (currentScene.isGameWin()) {
            tvText.setClickable(true);
            tvText.setOnClickListener(v -> {
                Intent intent = new Intent(GameActivity.this, GameWinActivity.class);
                startActivity(intent);
                finish();
            });
            animateTextPulse();
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

    private void animateTextPulse() {
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
            switch (choice.getMiniGame()) {
                case "flashlight_key":
                    intent = new Intent(this, RulesKeyActivity.class);
                    break;
                case "door_choice":
                    intent = new Intent(this, DoorChoiceActivity.class);
                    break;
                case "pictures_see":
                    intent = new Intent(this, PicturesSeeActivity.class);
                    break;
                case "pillar_riddle":
                    intent = new Intent(this, PillarActivity.class);
                    break;
                case "true":
                    intent = new Intent(this, TrueActivity.class);
                    break;
                case "anketa":
                    intent = new Intent(this, AnketaActivity.class);
                    intent.putExtra("sceneText", currentScene.getText());
                    intent.putExtra("nextSceneId", choice.getNextSceneId());
                    break;
                case "gloves":
                    intent = new Intent(this, GlovesActivity.class);
                    break;
                case "find_book":
                    intent = new Intent(this, FindBookActivity.class);
                    intent.putExtra("nextSceneId", choice.getNextSceneId());
                    break;
                case "parol":
                    intent = new Intent(this, ParolActivity.class);
                    break;
                case "diagram":
                    intent = new Intent(this, DiagramActivity.class);
                    break;
                case "mirror":
                    intent = new Intent(this, MirrorActivity.class);
                    break;
                case "picture_key":
                    intent = new Intent(this, PicturesKeyActivity.class);
                    break;
                case "chasi":
                    intent = new Intent(this, ChasiActivity.class);
                    break;
                default:
                    Log.d("GameActivity", "Unknown miniGame: " + choice.getMiniGame());
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
