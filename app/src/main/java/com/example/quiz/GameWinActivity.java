package com.example.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameWinActivity extends AppCompatActivity {

    Button retryButton;
    TextView quitText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_win);

        retryButton = findViewById(R.id.retryButton);
        quitText = findViewById(R.id.quitText);

        retryButton.setOnClickListener(view -> {
            Intent intent = new Intent(GameWinActivity.this, GameActivity.class);
            startActivity(intent);
            finish();
        });

        quitText.setOnClickListener(view -> {
            finishAffinity();
        });
    }
}