package com.example.quiz;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.example.quiz.ui.main.SearchKeyActivity;

public class RulesKeyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules_key);

        Button startGameButton = findViewById(R.id.start_game_button);

        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RulesKeyActivity.this, SearchKeyActivity.class);
                startActivity(intent);
            }
        });
    }
}
