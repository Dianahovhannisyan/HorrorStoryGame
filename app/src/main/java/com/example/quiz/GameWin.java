package com.example.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quiz.ui.main.GameOverActivity;

public class GameWin extends AppCompatActivity {

    Button retryButton;
    TextView quitText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        retryButton = findViewById(R.id.retryButton);
        quitText = findViewById(R.id.quitText);

        retryButton.setOnClickListener(view -> {
            Intent intent = new Intent(GameWin.this, GameActivity.class);
            startActivity(intent);
            finish();
        });

        quitText.setOnClickListener(view -> {
            finishAffinity();
        });
    }
}