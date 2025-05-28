package com.example.noturningback;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.noturningback.ui.main.GameOverActivity;

public class PillarActivity extends AppCompatActivity {
    private ImageView[] stones;
    private Button checkButton, continueButton;
    private String nextSceneId;
    private boolean[] selectedStones = new boolean[7];
    private boolean[] correctStones = {false, false, true, false, true, false, true};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pillar);

        stones = new ImageView[]{
                findViewById(R.id.stone1),
                findViewById(R.id.stone2),
                findViewById(R.id.stone3),
                findViewById(R.id.stone4),
                findViewById(R.id.stone5),
                findViewById(R.id.stone6),
                findViewById(R.id.stone7)
        };
        checkButton = findViewById(R.id.checkButton);
        continueButton = findViewById(R.id.continueButton);

        nextSceneId = getIntent().getStringExtra("nextSceneId");
        Log.d("PillarActivity", "Received nextSceneId: " + nextSceneId);

        for (int i = 0; i < stones.length; i++) {
            final int index = i;
            stones[i].setOnClickListener(v -> {
                if (!selectedStones[index]) {
                    stones[index].setBackgroundColor(Color.YELLOW);
                    selectedStones[index] = true;
                } else {
                    stones[index].setBackgroundColor(Color.TRANSPARENT);
                    selectedStones[index] = false;
                }
            });
        }

        checkButton.setOnClickListener(v -> checkAnswers());

        continueButton.setVisibility(View.GONE);
        continueButton.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("nextSceneId", nextSceneId);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    private void checkAnswers() {
        boolean allCorrect = true;
        for (int i = 0; i < stones.length; i++) {
            if (selectedStones[i]) {
                if (correctStones[i]) {
                    stones[i].setBackgroundColor(Color.GREEN);
                } else {
                    stones[i].setBackgroundColor(Color.RED);
                    allCorrect = false;
                }
            }
        }

        if (allCorrect && countSelected() == 3) {
            checkButton.setVisibility(View.GONE);
            continueButton.setVisibility(View.VISIBLE);
        } else {
            for (int i = 0; i < stones.length; i++) {
                if (selectedStones[i] && !correctStones[i]) {
                    Intent intent = new Intent(this, GameOverActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        }
    }

    private int countSelected() {
        int count = 0;
        for (boolean selected : selectedStones) {
            if (selected) count++;
        }
        return count;
    }
}