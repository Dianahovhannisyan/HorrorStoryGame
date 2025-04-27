package com.example.quiz.ui.main;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quiz.GameActivity;
import com.example.quiz.R;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

public class RulesActivity extends AppCompatActivity {

    private ImageView swordsIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        swordsIcon = findViewById(R.id.swordsIcon);

        swordsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });
    }

    private void startGame() {
        Intent intent = new Intent(RulesActivity.this, GameActivity.class);
        startActivity(intent);
        finish();
    }
}
