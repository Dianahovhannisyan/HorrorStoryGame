package com.example.noturningback.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.noturningback.GameActivity;
import com.example.noturningback.R;
import com.example.noturningback.StatsManager;

public class GameOverActivity extends AppCompatActivity {

    private Button retryButton;
    private TextView quitText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        retryButton = findViewById(R.id.retryButton);
        quitText = findViewById(R.id.quitText);

        StatsManager.updateStats(false);

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