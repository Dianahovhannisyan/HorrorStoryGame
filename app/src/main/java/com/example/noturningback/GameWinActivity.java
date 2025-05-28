package com.example.noturningback;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameWinActivity extends AppCompatActivity {

    private Button retryButton;
    private TextView quitText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_win);

        retryButton = findViewById(R.id.retryButton);
        quitText = findViewById(R.id.quitText);

        StatsManager.updateStats(true);

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