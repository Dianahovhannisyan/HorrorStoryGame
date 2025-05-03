package com.example.quiz.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quiz.GameActivity;
import com.example.quiz.R;

public class GameOverActivity extends AppCompatActivity {

    Button retryButton;
    TextView quitText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        retryButton = findViewById(R.id.retryButton);
        quitText = findViewById(R.id.quitText);

        retryButton.setOnClickListener(view -> {
            Intent intent = new Intent(GameOverActivity.this, GameActivity.class);
            startActivity(intent);
            finish();
        });

        quitText.setOnClickListener(view -> {
            finishAffinity();
        });
    }
}
