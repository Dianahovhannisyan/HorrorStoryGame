package com.example.quiz;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.transition.Scene;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.quiz.ui.main.GameLogic;


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
        showCurrentScene();


        btnChoice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { processChoice(0); }
        });
        btnChoice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { processChoice(1); }
        });
        btnChoice3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { processChoice(2); }
        });
    }

    private void processChoice(int choiceIndex) {
        // Получаем результат выбора (текст последствий)
        String resultText = gameLogic.processChoice(choiceIndex);

        new AlertDialog.Builder(this)
                .setTitle("Результат выбора")
                .setMessage(resultText)
                .setCancelable(false)
                .setPositiveButton("Продолжить", (dialog, which) -> {
                    if (gameLogic.hasNextScene()) {
                        showCurrentScene();
                    } else {
                        showFinalResult();
                    }
                })
                .show();
    }

    private void showCurrentScene() {
        Scene scene = gameLogic.getCurrentScene();
        if (scene != null) {
            tvTitle.setText(scene.getTitle());
            tvText.setText(scene.getText());

            int choiceCount = scene.getChoices().size();
            if (choiceCount > 0) {
                btnChoice1.setVisibility(View.VISIBLE);
                btnChoice1.setText(scene.getChoices().get(0).getText());
            } else {
                btnChoice1.setVisibility(View.GONE);
            }
            if (choiceCount > 1) {
                btnChoice2.setVisibility(View.VISIBLE);
                btnChoice2.setText(scene.getChoices().get(1).getText());
            } else {
                btnChoice2.setVisibility(View.GONE);
            }
            if (choiceCount > 2) {
                btnChoice3.setVisibility(View.VISIBLE);
                btnChoice3.setText(scene.getChoices().get(2).getText());
            } else {
                btnChoice3.setVisibility(View.GONE);
            }
        }
    }

    private void showFinalResult() {
        int score = gameLogic.getScore();
        String ending;
        if (score >= 90) {
            ending = "Выживший героически";
        } else if (score >= 60) {
            ending = "Выжил, но не без потерь";
        } else if (score >= 30) {
            ending = "Неполное выживание";
        } else {
            ending = "Неудачник";
        }
        new AlertDialog.Builder(this)
                .setTitle("Конец игры")
                .setMessage("Ваш итоговый счет: " + score + "\n" + ending)
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> finish())
                .show();
    }
}
