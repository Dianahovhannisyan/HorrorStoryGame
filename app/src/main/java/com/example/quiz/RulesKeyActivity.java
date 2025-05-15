package com.example.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quiz.ui.main.SearchKeyActivity;

public class RulesKeyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules_key);

        Button startGameButton = findViewById(R.id.start_game_button);
        String nextSceneId = getIntent().getStringExtra("nextSceneId");

        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RulesKeyActivity.this, SearchKeyActivity.class);
                intent.putExtra("nextSceneId", nextSceneId);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            setResult(resultCode, data);
            finish();
        }
    }
}