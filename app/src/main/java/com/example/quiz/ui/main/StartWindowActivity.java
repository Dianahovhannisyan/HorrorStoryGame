package com.example.quiz.ui.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quiz.GameActivity;
import com.example.quiz.R;


public class StartWindowActivity extends AppCompatActivity {
    TextView back_titleText;
    TextView window_titleText1;
    TextView window_titleText2;
    Button startButton;
    Button settingsButton;
    Button rulesButton;
    Button exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_window);

        back_titleText = findViewById(R.id.windowback_titleText);
        window_titleText1 = findViewById(R.id.window_titleText1);
        window_titleText2 = findViewById(R.id.window_titleText2);
        startButton = findViewById(R.id.windowstartButton);
        rulesButton = findViewById(R.id.windowrulesButton);
        exitButton = findViewById(R.id.windowexitButton);

        // Кнопка "Начать игру"
        startButton.setOnClickListener(v -> {
            Intent intent = new Intent(StartWindowActivity.this, GameActivity.class);
            startActivity(intent);
        });



        // Кнопка "Правила"
        rulesButton.setOnClickListener(v -> {
            Intent intent = new Intent(StartWindowActivity.this, RulesActivity.class);
            startActivity(intent);
        });

        // Кнопка "Выход"
        exitButton.setOnClickListener(v -> showExitDialog());
    }

    private void showExitDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Выход")
                .setMessage("Ты уверен, что хочешь выйти?")
                .setPositiveButton("Да", (dialog, which) -> finish())
                .setNegativeButton("Отмена", null)
                .show();
    }
}
